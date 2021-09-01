package com.bo.helper.data.entity.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["uid", "company_name", "createdAt", "updatedAt"])])
data class DiscountDTO(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "company_name") val companyName: String,
    @ColumnInfo(name = "card_uid") val cardUID: String,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "order") val order: Long,
    @ColumnInfo(name = "createdAt") val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updatedAt") val updatedAt: Date = Date(System.currentTimeMillis())
)
