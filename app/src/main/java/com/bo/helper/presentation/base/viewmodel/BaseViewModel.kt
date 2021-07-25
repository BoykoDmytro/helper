package com.bo.helper.presentation.base.viewmodel

import androidx.lifecycle.ViewModel
import com.bo.helper.common.SingleLiveData
import com.bo.helper.common.event.UIEvent
import com.bo.helper.common.navigation.Command
import com.bo.helper.common.type.FieldErrorType
import com.bo.helper.common.util.FileUtil
import com.bo.helper.data.ErrorResult
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    val navigationEvent = SingleLiveData<Command>()
    val uiEvent = SingleLiveData<UIEvent>()
    val emptyDataEvent = SingleLiveData<Void>()
    private val listProcess: MutableList<Any> = mutableListOf()
    private var job: Job = SupervisorJob()

    private var parentJob: Job = Job()
    private var backgroundContext: CoroutineContext = Dispatchers.IO

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val error = (exception as? HttpException)?.let {
            getError(it)
        } ?: ErrorResult(code = -1, message = exception.message)
        uiEvent.postValue(UIEvent.FailureUIEvent(error))
        Timber.e(exception)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + job + exceptionHandler

    protected fun showProgressBar(obj: Any) {
        listProcess.add(obj)
        takeIf { listProcess.isNotEmpty() }?.let {
            uiEvent.postValue(UIEvent.LoadingUIEvent(true))
        }
    }

    protected fun hideProgressBar(obj: Any) {
        listProcess.remove(obj)
        takeIf { listProcess.isEmpty() }?.let {
            uiEvent.postValue(UIEvent.LoadingUIEvent(false))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelParentJob()
        coroutineContext.cancel()
    }

    fun executeOnUI(block: suspend () -> Unit) {
        launch(coroutineContext) { block() }
    }

    suspend fun <T> executePending(block: suspend () -> T): T {
        try {
            showProgressBar(this)
            return withContext(Dispatchers.IO) { block() }
        } finally {
            hideProgressBar(this)
        }
    }

    private fun getError(ex: HttpException): ErrorResult<Any> {
        val responseBody = ex.response()?.errorBody()
        return takeIf { responseBody?.contentType()?.subtype == "json" }?.let {
            try {
                ErrorResult(ex.code(), ex.message())
            } catch (jsonEx: JsonSyntaxException) {
                Timber.e(jsonEx)
                ErrorResult(ex.code(), ex.message())
            }
        } ?: ErrorResult(ex.code(), ex.message())
    }

    fun onCameraClicked() {
        navigationEvent.value = Command.Camera
    }

    fun onGalleryClicked() {
        navigationEvent.value = Command.Video
    }

    fun logOut() {}

    protected fun execute(action: suspend () -> Unit?) {
        cancelParentJob()
        parentJob = Job()
        CoroutineScope(backgroundContext + parentJob).launch { action() }
    }

    protected fun cancelParentJob() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }

    open fun showError(result: ErrorResult<*>) {
        uiEvent.value = UIEvent.FailureUIEvent(result)
    }

    protected fun showFilePreview(file: File?, action: (File) -> Unit) {
        file?.let { nonNullFile ->
            if (FileUtil.isValidFileSize(nonNullFile)) {
                action.invoke(file)
            } else {
                uiEvent.value = UIEvent.ErrorUIEvent(listOf(FieldErrorType.InvalidFileSize))
            }
        } ?: run { uiEvent.value = UIEvent.ErrorUIEvent(listOf(FieldErrorType.InvalidFileSize)) }
    }
}