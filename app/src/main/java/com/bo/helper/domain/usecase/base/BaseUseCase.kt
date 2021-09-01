package com.bo.helper.domain.usecase.base

import com.bo.helper.data.PendingResult

abstract class BaseUseCase<K> {

    abstract suspend fun execute(): PendingResult<K>
}