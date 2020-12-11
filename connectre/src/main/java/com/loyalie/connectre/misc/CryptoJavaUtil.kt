package com.happinest.happiedge.misc

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * A utitliy class for encryption and decryption logic
 *
 *
 * You can implement any kind of encryption and decryption logic per your security requirements
 *
 *
 * The Encryption/Decryption is not to be used for a production app.
 * This is just a demo, proper security guidelines should be followed
 */
object CryptoJavaUtil {
    @Throws(Exception::class)
    fun Decrypt(stringToEncode: String?, Key: String?): String? {
        return try {
            if (Key == ""||Key==null) {
                return null
            }


            val raw = Key.toByteArray(charset("UTF-8"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher: Cipher

//            byte[] encrypted1 = Base64.decode(stringToEncode, Base64.URL_SAFE);
//            val encrypted1 = Base64.decode(vj, Base64.NO_WRAP)
            val encrypted1 = Base64.decode(stringToEncode, Base64.NO_WRAP)
            //            if (null != IV) {
//                IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
//                SecretKeySpec skeySpec = getKey(Key);
//                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//                cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
//            } else {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            //            }
            val ret = cipher.doFinal(encrypted1)
            String(ret, Charset.forName("UTF-8"))
        } catch (ex: Exception) {
            println(ex.toString())
            ex.toString()
        }
    }
  fun DecryptL(stringToEncode: String?, Key: String?): String? {
        return try { /*from www  .  ja v  a 2s. c  o  m*/
            if (Key == null) {
                return null
            }
//            val vj=encrypt("{\"status\":1,\"message\":\"Success\",\"tokenStatus\":1,\"result\":{\"type1\":\"AIzaSyB_UgCRs99Z4h4YJ9Ihv5G73Xzv45BdJNw\",\"type2\":\"8be6ad7e541c15dc4a5767edd8e9e4b1\",\"type3\":\"AIzaSyDqhI-TfpaLuX0L5OaoBitUIQzwkiUYFds\",\"type4\":\"AIzaSyAtMbuX0dz0-8qH5Pv8Biat_3E36uPNg7s\",\"type5\":\"72e?5ve#7fa4*exi\"}}",WingmanApp.instance.getCin())
//         Log.d("OkHtppen", vj)

            val raw = Key.toByteArray(charset("UTF-8"))
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher: Cipher

//            byte[] encrypted1 = Base64.decode(stringToEncode, Base64.URL_SAFE);
//            val encrypted1 = Base64.decode(vj, Base64.NO_WRAP)
            val encrypted1 = Base64.decode(stringToEncode, Base64.URL_SAFE)
            //            if (null != IV) {
//                IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
//                SecretKeySpec skeySpec = getKey(Key);
//                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//                cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
//            } else {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            //            }
            val ret = cipher.doFinal(encrypted1)
            String(ret, Charset.forName("UTF-8"))
        } catch (ex: Exception) {
            println(ex.toString())
            ex.toString()
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getKey(Key: String): SecretKeySpec {
        val keyLength = 256
        val keyBytes = ByteArray(keyLength / 8)
        Arrays.fill(keyBytes, 0x0.toByte())
        val passwordBytes = Key.toByteArray(charset("UTF-8"))
        val length = if (passwordBytes.size < keyBytes.size) passwordBytes.size else keyBytes.size
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length)
        return SecretKeySpec(keyBytes, "AES")
    }

    fun encrypt(strToEncrypt: String, encryptKey: String): String {
    /*    return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey = SecretKeySpec(encryptKey.toByteArray(charset("UTF-8")), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            *//*Base64Utils.encodeUrlSafe(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));*//*
            Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8"))), Base64.DEFAULT)

        } catch (e: Exception) {
            e.printStackTrace()
            "Cannot Encrypt Data"
        }*/
     return try {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKey = SecretKeySpec(encryptKey.toByteArray(charset("UTF-8")), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        /*Base64Utils.encodeUrlSafe(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));*/
        Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8"))), Base64.DEFAULT)

    } catch (e: Exception) {
        e.printStackTrace()
        "Cannot Encrypt Data"
    }
    }
}