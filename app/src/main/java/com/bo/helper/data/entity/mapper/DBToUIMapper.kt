package com.bo.helper.data.entity.mapper

import com.bo.helper.data.entity.database.dao.DiscountUI
import com.bo.helper.data.entity.database.dto.DiscountDTO
import com.bo.helper.data.entity.presentation.discount.IDiscount

object DBToUIMapper {

    fun map(discountDTO: DiscountDTO): IDiscount = with(discountDTO) {
        DiscountUI(
            uid = uid,
            companyName = companyName,
            cardUID = cardUID,
            photo = photo,
            order = order,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}