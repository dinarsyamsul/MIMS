package dev.iconpln.mims.data.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dev.iconpln.mims.databinding.ActivityScannerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScannerBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        barcodeBoxView = BarcodeBoxView(this)
        addContentView(
            barcodeBoxView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // Complete activity setup...
        checkCameraPermission()
    }

    // This is for responsible to request the required CAMERA permission
    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    // This will check if the CAMERA permission has been granted.
    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted
            startCamera()
        } else {
            // Permission denied
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission required")
                .setMessage("This application needs to access the camera to proccess barcodes")
                .setPositiveButton("Ok") { _, _ ->
                    // Keep asking for permission until granted
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

    // This is executed once the user has granted or denied the missing permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    // This is responsible for the setup of the camera preview and the image analyzer.
    private fun startCamera() {
        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)

        cameraProvideFuture.addListener(
            {
                val cameraProvider = cameraProvideFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.previewView.surfaceProvider)
                    }

                // Image analyzer
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            QrCodeAnalyzer(
                                this,
                                barcodeBoxView,
                                binding.previewView.width.toFloat(),
                                binding.previewView.height.toFloat()
                            )
                        )
                    }

                // Select back camera as default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind usecases before rebinding
                    cameraProvider.unbindAll()

                    // Bind usecases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(this)
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        cameraExecutor.shutdown()
    }

    inner class QrCodeAnalyzer(
        private val context: Context,
        private val barcodeBoxView: BarcodeBoxView,
        private val previewViewWidth: Float,
        private val previewViewHeight: Float
    ) : ImageAnalysis.Analyzer {

        private var scaleX = 1f
        private var scaleY = 1f

        private fun translateX(x: Float) = x * scaleX
        private fun translateY(y: Float) = y * scaleY

        private fun adjustBoundingRect(rect: Rect) = RectF(
            translateX(rect.left.toFloat()),
            translateY(rect.top.toFloat()),
            translateX(rect.right.toFloat()),
            translateY(rect.bottom.toFloat())
        )

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(image: ImageProxy) {
            val img = image.image
            if (img != null) {

                scaleX = previewViewWidth / img.height.toFloat()
                scaleY = previewViewHeight / img.width.toFloat()
                val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

                // Process image searching for barcodes
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_CODE_128)
                    .build()

                val scanner = BarcodeScanning.getClient(options)

                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->

                        if (barcodes.isNotEmpty()) {
                            for (barcode in barcodes) {
                                Toast.makeText(
                                    context,
                                    "value: " + barcode.rawValue,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                // update bounding rect
                                barcode.boundingBox?.let { rect ->
                                    barcodeBoxView.setRect(
                                        adjustBoundingRect(
                                            rect
                                        )
                                    )
                                }
                            }
                        } else {
                            barcodeBoxView.setRect(RectF())
                        }
                        for (barcode in barcodes) {
                            Toast.makeText(
                                this@ScannerActivity,
                                "Hasil scan : ${barcode.displayValue}",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
//                                    this@ScannerActivity,
//                                    ResponseScanActivity::class.java
                                ).also {
//                                    it.putExtra(ResponseScanActivity.EXTRA_SN, barcode.displayValue)
                                })
                        }
                    }
                    .addOnFailureListener { }
            }

            image.close()
        }
    }

    class BarcodeBoxView(
        context: Context
    ) : View(context) {

        private val paint = Paint()

        private var mRect = RectF()

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)

            val cornerRadius = 10f

            paint.style = Paint.Style.STROKE
            paint.color = Color.GREEN
            paint.strokeWidth = 5f

            canvas?.drawRoundRect(mRect, cornerRadius, cornerRadius, paint)
        }

        fun setRect(rect: RectF) {
            mRect = rect
            invalidate()
            requestLayout()
        }
    }

//    class QrCodeAnalyzer(
//        private val context: Context,
//        private val barcodeBoxView: BarcodeBoxView,
//        private val previewViewWidh: Float,
//        private val previewViewHeight: Float
//    ) : ImageAnalysis.Analyzer {
//        private var scaleX = 1f
//        private var scaleY = 1f
//
//        private fun translateX(x: Float) = x * scaleX
//        private fun translateY(y: Float) = y * scaleY
//
//        private fun adjustBoundingRect(rect: Rect) = RectF(
//            translateX(rect.left.toFloat()),
//            translateY(rect.top.toFloat()),
//            translateX(rect.right.toFloat()),
//            translateY(rect.bottom.toFloat())
//        )
//
//        @SuppressLint("UnsafeOptInUsageError")
//        override fun analyze(image: ImageProxy) {
//            val img = image.image
//            if (img != null) {
//                scaleX = previewViewWidh / img.height.toFloat()
//                scaleY = previewViewHeight / img.width.toFloat()
//
//                val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)
//
//                val options = BarcodeScannerOptions.Builder()
//                    .build()
//
//                val scanner = BarcodeScanning.getClient(options)
//                scanner.process(inputImage)
//                    .addOnSuccessListener { barcodes ->
//                        if (barcodes.isNotEmpty()) {
//                            for (barcode in barcodes) {
//                                Toast.makeText(
//                                    context,
//                                    "value: " + barcode.rawValue,
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                                // update bounding rect
//
//                                barcode.boundingBox?.let { rect ->
//                                    barcodeBoxView.setRect(
//                                        adjustBoundingRect(
//                                            rect
//                                        )
//                                    )
//                                }
//                            }
//                        } else {
//                            // remove bounding rect
//                            barcodeBoxView.setRect(RectF())
//                        }
//                    }
//                    .addOnFailureListener {  }
//            }
//            image.close()
//        }
//    }
}