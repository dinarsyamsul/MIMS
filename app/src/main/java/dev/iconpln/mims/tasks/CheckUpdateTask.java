package dev.iconpln.mims.tasks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dev.iconpln.mims.data.remote.service.ApiConfig;
import dev.iconpln.mims.ui.auth.LoginActivity;
import dev.iconpln.mims.utils.Config;
import dev.iconpln.mims.utils.DataController;
import dev.iconpln.mims.utils.Helper;

public class CheckUpdateTask extends AsyncTask<Void, String, Integer> {
    String returnString;
    LoginActivity activity;
    String path;
    String abi;

    public CheckUpdateTask(LoginActivity act, String _abi) {
        activity = act;
        abi = _abi;
    }

    public static final int MOST_UPDATED = -1, SUCCESS = 0, ERROR_API = 1, ERROR_DOWNLOAD = 2;

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        activity.setLoading(true, "Loading", values[0]);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        publishProgress("Sedang periksa versi aplikasi...");
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiConfig.INSTANCE.getHostUrl() + "/users/checkForUpdate");
            MultipartEntity me = new MultipartEntity();
            me.addPart("abi", new StringBody(abi));
            httpPost.setEntity(me);
            httpPost.addHeader("mims-schema-name", Config.SCHEMA_NAME);
            HttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            returnString = EntityUtils.toString(responseEntity);
            Log.d("debug", returnString);
            JSONObject jsonreturn = new JSONObject(returnString);
            String status = jsonreturn.getString("status");

            if (status.equals("success")) {
                JSONObject data_object = jsonreturn.getJSONObject("data");
                Log.i("data", data_object.toString());

                String version = data_object.getString("vers");
                publishProgress("Sedang periksa versi aplikasi...");
                if (checkVersion(version) != 0) {
                    String url = data_object.getString("url_link");
                    String nama_file = getFilenameFromPath(url);

                    if (url == null) return ERROR_DOWNLOAD;

                    publishProgress("Sedang mengunduh aplikasi...");
                    path = downloadFile(url, nama_file);
                    if (path != null) return SUCCESS;
                    else return ERROR_DOWNLOAD;
                } else {
                    return MOST_UPDATED;
                }
            } else {
                return ERROR_API;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ERROR_API;
    }

    private String getFilenameFromPath(String fullPath) {
        try {
            int index = fullPath.lastIndexOf("/");
            return fullPath.substring(index + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return "release.apk";
        }
    }

    int checkVersion(String version) {
        String cur_ver = Helper.INSTANCE.getVersion(activity);
        Log.d("debug", cur_ver + " , " + version);
        Log.d("debug", version.compareTo(cur_ver) + "");
        return version.compareTo(cur_ver);
    }

    String downloadFile(String surl, String nama_file) {
        String path_download = DataController.INSTANCE.getDirectory(DataController.FileType.MAIN_DIRECTORY) + "/" + nama_file;
        File downloadFile = new File(path_download);
        if (downloadFile.exists())
            downloadFile.delete();
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(surl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            output = new FileOutputStream(path_download);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress("Sedang mengunduh aplikasi: " + (int) (total * 100 / fileLength) + " %");
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
                ignored.printStackTrace();
                return null;
            }

            if (connection != null)
                connection.disconnect();
        }
        return path_download;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        activity.setLoading(false, "", "");
        Log.d("onPost","yes");
        if (integer == SUCCESS) {
            Log.d("success onpost", "yes");
            Log.d("path",path);
//            DataController.INSTANCE.updateAPK(activity, path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setDataAndType(FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }else{
                intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            activity.startActivity(intent);

        } else if (integer == MOST_UPDATED) {
            Toast.makeText(activity, "Kamu sudah menggunakan aplikasi terbaru", Toast.LENGTH_LONG).show();
        } else if (integer == ERROR_API) {
            Toast.makeText(activity, "Check for update error", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Error update", Toast.LENGTH_LONG).show();
        }
    }
}
