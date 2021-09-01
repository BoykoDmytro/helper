package com.bo.helper.data.entity.database.dao

import com.bo.helper.data.entity.presentation.discount.IDiscount
import java.util.*

class DiscountUI(
    override val uid: Long,
    override val companyName: String,
    override val cardUID: String,
    override val photo: String? = null,
    override val order: Long,
    override val createdAt: Date,
    override val updatedAt: Date
) : IDiscount