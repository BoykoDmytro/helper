package com.bo.helper.presentation.home.fragment

import com.bo.helper.data.entity.presentation.discount.IDiscount
import com.bo.helper.domain.repository.IDiscountRepository
import com.bo.helper.domain.usecase.base.BasePageUseCase
import com.bo.helper.domain.usecase.discount.GetDiscountUseCase
import com.bo.helper.presentation.base.viewmodel.BasePageViewModel
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import javax.inject.Inject

class DiscountListViewModel @Inject constructor(private val getDiscountUseCase: GetDiscountUseCase) :
    BasePageViewModel<IDiscount>() {

    override fun getPageUseCase(): BasePageUseCase<List<IDiscount>> = getDiscountUseCase

}