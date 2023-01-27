package dev.iconpln.mims.ui.role.up3.pnerimaan

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import dev.iconpln.mims.R
import dev.iconpln.mims.databinding.ActivityCameraxPortraitBinding
import dev.iconpln.mims.utils.*
import id.zelory.compressor.Compressor
import org.joda.time.DateTime
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity(){
    private var mFileName: String? = null
    private var mFileGroup: String? = null
    private var mExtra1: Int = 0
    private var mExtra2: Int = 0

    private var mOrientation: String? = ""
    private var mIsFlashOn: Boolean = false
    private var mIsSwitchActive: Boolean = false
    private var mIsFlashActive: Boolean = false
    private var mIsDetectBlurActive: Boolean = false
    private var mIsDetectDarkActive: Boolean = false
    private var mIsImageBlur: Boolean = false
    private var mIsImageDark: Boolean = false
    private var mIsCompressActive: Boolean = false
    private var mSizeWidth: Int = 0
    private var mSizeHeight: Int = 0
    private var mDetectBlurThreshold: Double = 0.0
    private var mDetectDarkThreshold: Double = 0.0
    private var mCompressQuality: Int = 0
    private var mIsTagLocationActive: Boolean = false

    private var imageCapture: ImageCapture? = null
    private lateinit var mCameraSelector: CameraSelector
    private lateinit var mSize: Size
    private lateinit var outputDirectory: File
    private lateinit var binding: ActivityCameraxPortraitBinding
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraxPortraitBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())


        // Set up data intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initData()
        }
        Log.i("kesini","1")



        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()


        binding.cameraBtnTakePicture.setOnClickListener {
            Log.i("kesini","3")
            takePhoto()
        }
        with(binding){
            Log.i("kesini","2")
            // Set up the listener for take photo button


            cameraImgFlash.visibility = View.INVISIBLE
            cameraLblFlash.visibility = View.INVISIBLE
            // Camera Switch Feature
            cameraImgSwitch.visibility = View.INVISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initData() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Get Intent Data
        mExtra1 = intent.extras!!.getInt("Extra1", 0)
        mExtra2 = intent.extras!!.getInt("Extra2", 0)
        mFileName = intent.extras!!.getString("Filename")
        mFileGroup = intent.extras!!.getString("FileGroup", "Others")
        mOrientation = intent.extras!!.getString("CameraOrientation", "portrait")
        mSizeWidth = intent.extras!!.getInt("CameraResolutionWidth", 480)
        mSizeHeight = intent.extras!!.getInt("CameraResolutionHeight", 640)
        mIsSwitchActive = intent.extras!!.getBoolean("IsCameraSwitchActive", false)
        mIsFlashActive = intent.extras!!.getBoolean("IsCameraFlashActive", true)
        mIsDetectBlurActive = intent.extras!!.getBoolean("IsCameraDetectBlurActive", false)
        mIsDetectDarkActive = intent.extras!!.getBoolean("IsCameraDetectDarkActive", false)
        mIsCompressActive = intent.extras!!.getBoolean("IsCameraCompressActive", true)
        mDetectBlurThreshold = intent.extras!!.getDouble("CameraDetectBlurThreshold", 100.toDouble())
        mDetectDarkThreshold = intent.extras!!.getDouble("CameraDetectDarkThreshold", 50.toDouble())
        mCompressQuality = intent.extras!!.getInt("CameraCompressQuality", 90)
        mIsTagLocationActive = intent.extras!!.getBoolean("IsTagLocationActive", false)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_camerax_portrait)
        mSize = Size(mSizeWidth, mSizeHeight)
        mCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    }


    private fun takePhoto() {
        Log.i("kesini","3")

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(outputDirectory, "$mFileName")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.i("kesini","4")

                    // Saved image path
                    val savedUri = Uri.fromFile(photoFile)
                    var filePath = savedUri.path
                    Log.d(TAG, "Photo capture succeeded: ${savedUri.path}")

                    // Compress image
                    if(mIsCompressActive) {
                        try {
                            val compressedImage = Compressor(this@CameraXActivity).setQuality(mCompressQuality).setDestinationDirectoryPath("${outputDirectory.absolutePath}/Compressed").compressToFile(photoFile)

                            if (compressedImage != null) {
                                filePath = compressedImage.path

                                // Delete original file after compress success
                                if (photoFile.exists()) {
                                    photoFile.delete()
                                }
                                Log.d(TAG, "Photo compress succeeded: ${compressedImage.path}")
                            }
                        } catch (err: Exception) {
                            Log.d(TAG, "Photo compress failed: ${err.message}")
                        }
                    }

                    Log.i("kesini","5")

                    // Set intent data
                    val i = Intent()
                    i.putExtra("Path", filePath)
                    i.putExtra("TakenTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
                    i.putExtra("Extra1", mExtra1)
                    i.putExtra("Extra2", mExtra2)

                    setResult(RESULT_OK, i)
                    finish()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                //                .setTargetResolution(mSize)
                .build().also {
                    it.setSurfaceProvider(binding.cameraViewFinder.surfaceProvider)
                }

            // Image Capture
            val imageCaptureBuilder = ImageCapture.Builder()
            imageCapture = imageCaptureBuilder
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setTargetResolution(mSize).build()

            // Select back/front camera as a default
            val cameraSelector = mCameraSelector

            // Add HDR Extension
            //            val hdrImageCapture = HdrImageCaptureExtender.create(imageCaptureBuilder)
            //            if (hdrImageCapture.isExtensionAvailable(cameraSelector)) {
            //                hdrImageCapture.enableExtension(cameraSelector)
            //            }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                // Getting the CameraControl instance from the camera
                val cameraControl = camera.cameraControl

                //Set up TouchListener for focus preview
                binding.cameraViewFinder.setOnTouchListener { view: View, motionEvent: MotionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> true
                        MotionEvent.ACTION_UP -> {
                            // Get the MeteringPointFactory from PreviewView
                            val factory = binding.cameraViewFinder.meteringPointFactory

                            // Create a MeteringPoint from the tap coordinates
                            val point = factory.createPoint(motionEvent.x, motionEvent.y)

                            // Create a MeteringAction from the MeteringPoint, you can configure it to specify the metering mode
                            val action = FocusMeteringAction.Builder(point).build()

                            // Trigger the focus and metering. The method returns a ListenableFuture since the operation
                            // is asynchronous. You can use it get notified when the focus is successful or if it fails.
                            cameraControl.startFocusAndMetering(action)

                            true
                        }
                        else -> false
                    }
                }

                // Set up torch / flash
                binding.cameraImgFlash.setOnClickListener {
                    // Use the CameraControl instance to enable the torch
                    mIsFlashOn = !mIsFlashOn
                    val enableTorchLF: ListenableFuture<Void>
                    if (mIsFlashOn) {
                        enableTorchLF = cameraControl.enableTorch(true)
                        binding.cameraImgFlash.setImageResource(R.drawable.ic_baseline_flash_on_24)
                        binding.cameraLblFlash.text = getString(R.string.label_flash_on)
                    } else {
                        enableTorchLF = cameraControl.enableTorch(false)
                        binding.cameraImgFlash.setImageResource(R.drawable.ic_baseline_flash_off_24)
                        binding.cameraLblFlash.text = getString(R.string.label_flash_off)
                    }

                }
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = "${StorageUtils.getDirectory(StorageUtils.DIRECTORY_IMAGE)}/$mFileGroup"

        // if folder FileGroup not exist then create folder
        if(!File(mediaDir).exists()) {
            File(StorageUtils.getDirectory(StorageUtils.DIRECTORY_IMAGE), mFileGroup!!).mkdir()
        }

        return File(mediaDir)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}