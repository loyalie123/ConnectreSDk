package com.loyalie.connectre.data

sealed class ViewState<out R> {

    data class Success<out T>(val data: T,val position:Int=0) : ViewState<T>()
    data class Error(val exception: Throwable) : ViewState<Nothing>()
    data class Loading(val isInitial: Boolean = true, val isReload: Boolean = false) :
        ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
        }
    }
}

/**
 * `true` if [ViewState] is of type [Success] & holds non-null [Success.data].
 */
val ViewState<*>.succeeded
    get() = this is ViewState.Success && data != null


data class ApiResponse<T>(val status: Int, val message: String, val data: T?)


data class ApiResponse2<T>(var status: Int, var message: String, var tokenStatus: Int, var result: T)
data class ApiException(
    val code: Int,
    val description: String
) : RuntimeException(description)