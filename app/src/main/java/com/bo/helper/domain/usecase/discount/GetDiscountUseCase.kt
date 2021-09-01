package com.bo.helper.domain.usecase.discount

import com.bo.helper.data.PendingResult
import com.bo.helper.data.entity.presentation.discount.IDiscount
import com.bo.helper.domain.repository.IDiscountRepository
import com.bo.helper.domain.usecase.base.BasePageUseCase
import javax.inject.Inject

class GetDiscountUseCase @Inject constructor(private val iDiscountRepository: IDiscountRepository) :
    BasePageUseCase<List<IDiscount>>() {

    override suspend fun execute(): PendingResult<List<IDiscount>> {
        val perPage = getPerPage()
        val offset = perPage * page
        return iDiscountRepository.getDiscount(perPage, offset)
    }
}