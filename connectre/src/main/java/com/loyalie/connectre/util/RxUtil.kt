package com.loyalie.connectre.util


import com.loyalie.connectre.data.ApiException
import com.loyalie.connectre.data.ApiResponse
import io.reactivex.Scheduler
import io.reactivex.Single

data class AppRxSchedulers(
    val io: Scheduler,
    val computation: Scheduler,
    val main: Scheduler
)

fun <T> Single<ApiResponse<T>>.applySchedulers(schedulers: AppRxSchedulers): Single<T> {

    return subscribeOn(schedulers.io).map {
        if (it.status == 1) {
            return@map it.data
        } else {
            throw ApiException(it.status, it.message)
        }
    }

}

fun <T> Single<ApiResponse<T>>.applySchedulersForStatus(schedulers: AppRxSchedulers): Single<String> {

    return subscribeOn(schedulers.io).map {
        if (it.status == 1) {
            return@map it.message
        } else {
            throw ApiException(it.status, it.message)
        }
    }

}