package com.bo.helper.presentation.home.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bo.helper.data.entity.presentation.discount.IDiscount
import com.bo.helper.databinding.FragmentHomeMenuBinding
import com.bo.helper.presentation.base.fragment.BasePageFragment
import com.bo.helper.presentation.base.viewmodel.BasePageViewModel

class DiscountListFragment : BasePageFragment<IDiscount, FragmentHomeMenuBinding>() {

    private val viewModel: DiscountListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DiscountListViewModel::class.java)
    }

    override fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeMenuBinding =
        FragmentHomeMenuBinding.inflate(inflater, container, false)

    override fun getBasePageViewModel(): BasePageViewModel<IDiscount> = viewModel

    override val recyclerView: RecyclerView get() =  viewBinding.fDiscountListRv
    override val emptyView: View get() = viewBinding.fDiscountListEmptyView
}