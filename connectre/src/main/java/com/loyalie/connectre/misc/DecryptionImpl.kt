package com.happinest.happiedge.misc

import com.loyalie.connectre.misc.CryptoStrategy

class DecryptionImpl : CryptoStrategy {







    override fun decrypt(data: String?, b: String?): String? {
        return CryptoJavaUtil.Decrypt(data,b)
    }

    override fun encrypt(body: String?): String? {
        return null
    }

    override fun decryptL(data: String?, b: String?): String? {
        return CryptoJavaUtil.DecryptL(data,b)!!
    }
}