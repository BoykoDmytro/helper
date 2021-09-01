package com.bo.helper.data.entity.database.dao

import androidx.room.*
import com.bo.helper.data.entity.database.dto.DiscountDTO

@Dao
interface DiscountDao {

    @Query("SELECT * FROM DiscountDTO  ORDER BY uid limit :limit offset :offset")
    fun getDiscount(limit: Long, offset: Long): List<DiscountDTO>

    @Query("SELECT * FROM DiscountDTO WHERE company_name like '%' || :name || '%' limit :limit offset :offset")
    fun getDiscountByName(name: String, limit: Long, offset: Long): List<DiscountDTO>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(discount: DiscountDTO)

    @Insert
    fun insert(discount: DiscountDTO)

    @Delete
    fun delete(discount: DiscountDTO)
}