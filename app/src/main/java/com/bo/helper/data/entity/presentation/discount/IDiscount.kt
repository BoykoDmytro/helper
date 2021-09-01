package com.bo.helper.data.entity.presentation.discount

import java.util.*

interface IDiscount {

    val uid: Long
    val companyName: String
    val cardUID: String
    val photo: String?
    val order: Long
    val createdAt: Date
    val updatedAt: Date
}