package com.bo.helper.common.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.bo.helper.BuildConfig
import com.bo.helper.common.extention.getName
import com.bo.helper.common.extention.toMegabytes
import java.io.File
import java.io.FileOutputStream
import java.util.*

object FileUtil {

    const val FILE_PROVIDER = BuildConfig.APPLICATION_ID + ".fileProvider"
    private const val FILES = "files"
    private const val VIDEO_PREFIX_FILE_NAME = "video_"
    private const val PHOTO_PREFIX_FILE_NAME = "photo_"
    private const val VIDEO_FILE_MIME = ".mp4"
    private const val PHOTO_FILE_MIME = ".jpg"
    private const val MAX_FILE_SIZE_IN_MB = 10L

    fun copyFile(context: Context, uri: Uri): File? {
        val fileName = uri.getName(context) ?: Date().time.toString()
        val dir = File(context.cacheDir, FILES)
        if (!dir.exists()) dir.mkdir()
        try {
            val resolver: ContentResolver = context.contentResolver
            val stream = resolver.openInputStream(uri)
            val file = File(dir, fileName)
            val out = FileOutputStream(file)
            out.write(stream?.readBytes())
            out.flush()
            out.close()
            stream?.close()
            return file
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun getCameraFilePathByName(context: Context, title: String?): File? {
        return title?.let { nonNullTitle ->
            getCacheDir(context)
                .takeIf { it.exists() }
                ?.let { File(it, nonNullTitle) }
        }
    }

    fun createFile(context: Context, isPhoto: Boolean = false): File {
        val directory = getCacheDir(context)
        val prefix = if (isPhoto) PHOTO_PREFIX_FILE_NAME else VIDEO_PREFIX_FILE_NAME
        val mime = if (isPhoto) PHOTO_FILE_MIME else VIDEO_FILE_MIME
        if (!directory.exists()) directory.mkdirs()
        return File.createTempFile(prefix, mime, directory)
    }

    fun isValidFileSize(file: File): Boolean = file.length().toMegabytes() < MAX_FILE_SIZE_IN_MB

    private fun getCacheDir(context: Context): File = File(context.cacheDir, FILES)
}