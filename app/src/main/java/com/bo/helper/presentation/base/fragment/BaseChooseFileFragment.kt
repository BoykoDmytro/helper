package com.bo.helper.presentation.base.fragment

import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.viewbinding.ViewBinding
import com.bo.helper.common.util.FileUtil
import com.bo.helper.presentation.base.contract.GetImagesContent
import com.bo.helper.presentation.base.contract.GetVideoContent
import com.bo.helper.presentation.base.contract.TakeImage
import com.bo.helper.presentation.base.contract.TakeVideo
import java.io.File

abstract class BaseChooseFileFragment<VB : ViewBinding> : BaseFragment<VB>() {

    protected object MIMES {
        val allowVideoMimeTypes = arrayOf(
            "video/mp4",
            "video/mpeg",
            "video/ogg",
            "video/quicktime",
            "video/webm",
            "video/x-m4v",
            "video/ms-asf",
            "video/x-ms-wmv",
            "video/x-msvideo"
        )

        val allowImageMimeTypes = arrayOf(
            "image/png",
            "image/jpg",
            "image/jpeg",
        )
    }


    private val activityVideoCameraResult = registerForActivityResult(TakeVideo()) { result ->
        if (result) {
            val file = FileUtil.getCameraFilePathByName(requireContext(), videoPath.lastPathSegment)
            handleVideoFromCamera(file)
        }
    }

    private val activityVideoGalleryResult =
        registerForActivityResult(GetVideoContent()) { result ->
            result?.let { uri ->
                val file = FileUtil.copyFile(requireContext(), uri)
                handleVideoFromGallery(file)
            }
        }

    private val activityImageCameraResult = registerForActivityResult(TakeImage()) { result ->
        if (result) {
            val file = FileUtil.getCameraFilePathByName(requireContext(), imagePath.lastPathSegment)
            handleImageFromCamera(file)
        }
    }

    private val activityImageGalleryResult =
        registerForActivityResult(GetImagesContent()) { result ->
            result?.let { uri ->
                val file = FileUtil.copyFile(requireContext(), uri)
                handleImageFromGallery(file)
            }
        }

    private lateinit var videoPath: Uri
    private lateinit var imagePath: Uri

    protected open fun handleImageFromCamera(file: File?) = Unit

    protected open fun handleImageFromGallery(file: File?) = Unit

    protected open fun handleVideoFromCamera(file: File?) {}

    protected open fun handleVideoFromGallery(file: File?) {}

    protected fun openPhotoCamera() {
        val photoFile = FileUtil.createFile(requireContext(), isPhoto = true)
        imagePath = photoFile.toUri()
        val photoUri = FileProvider.getUriForFile(
            requireContext(),
            FileUtil.FILE_PROVIDER,
            photoFile
        )
        activityImageCameraResult.launch(photoUri)
    }

    protected fun openImageExplorer(allowMime: Array<String> = MIMES.allowImageMimeTypes) {
        activityImageGalleryResult.launch(allowMime)
    }

    protected fun openVideoCamera() {
        val videoFile = FileUtil.createFile(requireContext())
        videoPath = videoFile.toUri()
        val videoUri =
            FileProvider.getUriForFile(requireContext(), FileUtil.FILE_PROVIDER, videoFile)
        activityVideoCameraResult.launch(videoUri)
    }

    protected fun openVideoExplorer(allowMime: Array<String> = MIMES.allowVideoMimeTypes) =
        activityVideoGalleryResult.launch(allowMime)
}