package com.bo.helper.data.entity.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bo.helper.common.util.DateConverters
import com.bo.helper.data.entity.database.dao.DiscountDao
import com.bo.helper.data.entity.database.dto.DiscountDTO

@Database(entities = [DiscountDTO::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun discountDao(): DiscountDao
}