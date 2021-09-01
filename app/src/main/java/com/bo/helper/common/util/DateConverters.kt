package com.bo.helper.common.util

import androidx.room.TypeConverter
import java.util.*

class DateConverters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}
