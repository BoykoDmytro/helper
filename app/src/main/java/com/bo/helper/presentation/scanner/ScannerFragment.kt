package com.bo.helper.presentation.scanner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bo.helper.databinding.FragmentScannerBinding
import com.bo.helper.presentation.base.fragment.BaseFragment

class ScannerFragment : BaseFragment<FragmentScannerBinding>() {

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentScannerBinding =
        FragmentScannerBinding.inflate(inflater, container, false)
}