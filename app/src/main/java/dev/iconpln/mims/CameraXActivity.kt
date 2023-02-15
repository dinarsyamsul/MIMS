package dev.iconpln.mims

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import dev.iconpln.mims.databinding.ActivityCameraXactivityBinding
import dev.iconpln.mims.utils.StorageUtils
import id.zelory.compressor.Compressor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraXactivityBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService
    private var fotoName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fotoName = intent.getStringExtra("fotoName")

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        with(binding){
            imgCaptureBtn.setOnClickListener{
                imgCaptureBtn.isEnabled = false
                takePhoto()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    animateFlash()
                }
            }
        }


    }

    private val cameraProviderResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissionGranted->
        if(permissionGranted){
            // cut and paste the previous startCamera() call here.
            startCamera()
        }else {
            Snackbar.make(binding.root,"The camera permission is required", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun startCamera(){
        // listening for data from the camera
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // connecting a preview use case to the preview in the xml file.
            val preview = Preview.Builder().build().also{
                it.setSurfaceProvider(binding.preview.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try{
                // clear all the previous use cases first.
                cameraProvider.unbindAll()
                // binding the lifecycle of the camera to the lifecycle of the application.
                cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            } catch (e: Exception) {
                Log.d("CameraX", "Use case binding failed")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        imageCapture?.let{
            //Create a storage location whose fileName is timestamped in milliseconds.
            val file_path = StorageUtils.getDirectory(StorageUtils.DIRECTORY_ROOT) +
                    "/Images"
            val dir = File(file_path)
            if (!dir.exists()) dir.mkdirs()
            val fileName = "Pictures-${fotoName}${System.currentTimeMillis()}.png"
            val file = File(dir,fileName)

            // Save the image in the above file
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            /* pass in the details of where and how the image is taken.(arguments 1 and 2 of takePicture)
            pass in the details of what to do after an image is taken.(argument 3 of takePicture) */

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(file)
                        var filePath = savedUri.path

                        // Compress image
                        try {
                            val compressedImage =
                                Compressor(this@CameraXActivity).setQuality(90)
                                    .setDestinationDirectoryPath("${dir.absolutePath}/Compressed")
                                    .compressToFile(file)

                            if (compressedImage != null) {
                                filePath = compressedImage.path

                                // Delete original file after compress success
                                if (file.exists()) {
                                    file.delete()
                                }
                                Log.d(
                                    "CameraX",
                                    "Photo compress succeeded: ${compressedImage.path}"
                                )
                            }
                        } catch (err: Exception) {
                            Log.d("CameraX", "Photo compress failed: ${err.message}")
                        }

                        Log.i("CameraX", "The image has been saved in ${file.toUri()}")

                        val i = Intent()
                        i.putExtra("Path", filePath)

                        setResult(RESULT_OK, i)
                        finish()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(
                            binding.root.context,
                            "Error taking photo",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("CameraX", "Error taking photo:$exception")
                    }


                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun animateFlash() {
        binding.root.postDelayed({
            binding.root.foreground = ColorDrawable(Color.WHITE)
            binding.root.postDelayed({
                binding.root.foreground = null
            }, 50)
        }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        imgCaptureExecutor.shutdown()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }
}