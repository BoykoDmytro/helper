package com.bo.helper.data.repository

import com.bo.helper.data.PendingResult
import com.bo.helper.data.entity.database.dao.DiscountDao
import com.bo.helper.data.entity.database.dto.DiscountDTO
import com.bo.helper.data.entity.mapper.DBToUIMapper
import com.bo.helper.data.entity.mapper.NetworkMapper
import com.bo.helper.data.entity.mapper.UIToDBMapper
import com.bo.helper.data.entity.presentation.discount.IDiscount
import com.bo.helper.data.repository.base.BaseRepositoryImpl
import com.bo.helper.domain.repository.IDiscountRepository
import javax.inject.Inject

class DiscountRepositoryImpl @Inject constructor(private val dao: DiscountDao) : BaseRepositoryImpl(), IDiscountRepository {

    private val mapper = object : NetworkMapper<List<DiscountDTO>, List<IDiscount>> {
        override suspend fun map(data: List<DiscountDTO>): List<IDiscount> = data.map { DBToUIMapper.map(it) }
    }

    override suspend fun getDiscount(limit: Long, offset: Long): PendingResult<List<IDiscount>> =
        execute(dao.getDiscount(limit, offset), mapper)

    override suspend fun delete(discountUI: IDiscount): PendingResult<Void> =
        execute(dao.delete(UIToDBMapper.map(discountUI)), null)

    override suspend fun update(discountUI: IDiscount): PendingResult<Void> =
        execute(dao.update(UIToDBMapper.map(discountUI)), null)
}