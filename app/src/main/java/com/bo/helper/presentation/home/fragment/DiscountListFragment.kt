package com.bo.helper.presentation.home.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bo.helper.databinding.FragmentHomeMenuBinding
import com.bo.helper.presentation.base.fragment.BaseFragment

class DiscountListFragment : BaseFragment<FragmentHomeMenuBinding>() {

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeMenuBinding =
        FragmentHomeMenuBinding.inflate(inflater, container, false)
}