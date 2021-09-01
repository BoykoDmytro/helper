package com.bo.helper.presentation.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModelProvider
import com.bo.helper.databinding.FragmentScannerBinding
import com.bo.helper.presentation.base.fragment.BaseFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlin.math.min

class ScannerFragment : BaseFragment<FragmentScannerBinding>() {

    private val viewModel: ScannerViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ScannerViewModel::class.java)
    }

    private var cameraSource: CameraSource? = null
    private var barcodeDetector: BarcodeDetector? = null
    private val processor = object : Detector.Processor<Barcode> {
        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            viewModel.setBarCode(detections.detectedItems)
        }

        override fun release() {}
    }

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentScannerBinding =
        FragmentScannerBinding.inflate(inflater, container, false)

    private val surfaceHolderCallback: SurfaceHolder.Callback by lazy {
        object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                checkPermission()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int, width: Int, height: Int
            ) {
                val scanWindowSize = Size(width, height)
                val boundingBoxSize = (0.7 * min(width, height)).toInt()

                val boundingBox = Rect(
                    (scanWindowSize.width - boundingBoxSize) / 2,
                    (scanWindowSize.height - boundingBoxSize) / 2,
                    (scanWindowSize.width + boundingBoxSize) / 2,
                    (scanWindowSize.height + boundingBoxSize) / 2
                )
                viewBinding.fScannerBorder.rect = boundingBox
            }
        }
    }

    override fun onDestroyView() {
        releaseBarcode()
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission() {
        verifyPermission(Manifest.permission.CAMERA, accepted = { initCamera() })
    }

    @RequiresPermission("android.permission.CAMERA")
    private fun initCamera() {
        initBarcodeDetector()
        initCameraSource()
        cameraSource?.start(viewBinding.fScannerCamera.holder)
    }

    private fun initCameraSource() {
        viewBinding.fScannerCamera.apply {
            barcodeDetector?.let {
                cameraSource = CameraSource.Builder(context, it)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(height, width)
                    .build()
            }
        }
    }

    private fun initBarcodeDetector() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        barcodeDetector?.setProcessor(processor)
    }

    private fun releaseBarcode() {
        viewBinding.fScannerCamera.holder.removeCallback(surfaceHolderCallback)
        cameraSource?.release()
        barcodeDetector?.release()
    }
}