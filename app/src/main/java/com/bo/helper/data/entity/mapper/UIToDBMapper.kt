package com.bo.helper.data.entity.mapper

import com.bo.helper.data.entity.database.dto.DiscountDTO
import com.bo.helper.data.entity.presentation.discount.IDiscount

object UIToDBMapper {

    fun map(discountUI: IDiscount): DiscountDTO = with(discountUI) {
        DiscountDTO(
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