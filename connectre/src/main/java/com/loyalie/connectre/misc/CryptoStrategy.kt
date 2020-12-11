package com.loyalie.connectre.misc


interface CryptoStrategy {

    @Throws(Exception::class)
    fun   decrypt (data: String?, b: String?): String?

    @Throws(java.lang.Exception::class)
    fun encrypt(body: String?): String?



    @Throws(java.lang.Exception::class)
    fun decryptL(data: String?, b: String?): String?
}