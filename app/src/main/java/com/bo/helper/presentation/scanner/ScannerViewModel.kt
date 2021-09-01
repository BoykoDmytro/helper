package com.bo.helper.presentation.scanner

import android.util.SparseArray
import androidx.core.util.isEmpty
import com.bo.helper.common.SingleLiveData
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import com.google.android.gms.vision.barcode.Barcode
import javax.inject.Inject

class ScannerViewModel @Inject constructor() : BaseViewModel() {

    private var isProcessing = false
    private val showBarcodeEvent = SingleLiveData<Barcode>()

    fun setBarCode(detectedItems: SparseArray<Barcode>) {
        if (detectedItems.isEmpty() || isProcessing) return
        isProcessing = true
        detectedItems.valueAt(0)?.let {  showBarcodeEvent.value = it }
    }

}