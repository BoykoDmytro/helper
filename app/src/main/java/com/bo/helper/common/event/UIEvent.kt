package com.bo.helper.common.event

import com.bo.helper.common.type.FieldErrorType
import com.bo.helper.data.ErrorResult

sealed class UIEvent {
    class LoadingUIEvent(val isLoading: Boolean) : UIEvent()
    class FailureUIEvent<T : Any?>(val errorResult: ErrorResult<T>) : UIEvent()
    class NetworkUIEvent(val isNetworkEnabled: Boolean) : UIEvent()
    class AlertEvent(val error: Throwable) : UIEvent()
    class ErrorUIEvent(val errors: List<FieldErrorType>) : UIEvent()
}