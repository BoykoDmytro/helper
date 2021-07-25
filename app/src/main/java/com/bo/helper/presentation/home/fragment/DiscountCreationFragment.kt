package com.bo.helper.presentation.home.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bo.helper.databinding.FragmentDiscountCreationBinding
import com.bo.helper.presentation.base.fragment.BaseFragment

class DiscountCreationFragment : BaseFragment<FragmentDiscountCreationBinding>() {

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDiscountCreationBinding =
        FragmentDiscountCreationBinding.inflate(inflater, container, false)
}