package com.bo.helper.domain.usecase.base

abstract class BasePageUseCase<T> : BaseUseCase<T>() {

    companion object {
        const val PER_PAGE = 20L
        private const val START_PAGE = 1L
        private const val BIG_PER_PAGE = 500L
    }

    protected var page: Long = START_PAGE

    open fun getPerPage(): Long = PER_PAGE

    fun getBigPerPage(): Long = BIG_PER_PAGE

    fun increasePage(): Long = ++page

    fun resetPage() {
        page = START_PAGE
    }
}