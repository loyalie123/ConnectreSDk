package com.happinest.happiedge.misc;

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.RequiresApi
import com.loyalie.connectre.R
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal


class CryptoUtil(private val mContext: Context) {

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class,
            IOException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class,
            UnrecoverableEntryException::class, NoSuchPaddingException::class, InvalidKeyException::class)
    private fun initKeys() {
        val keyStore = KeyStore.getInstance(mContext.getString(R.string.android))
        keyStore.load(null)
        if (!keyStore.containsAlias(mContext.getString(R.string.minealias))) {
            initValidKeys()
        } else {
            var keyValid = false
            try {
                val keyEntry = keyStore.getEntry(mContext.getString(R.string.minealias), null)
                if (keyEntry is KeyStore.SecretKeyEntry &&
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyValid = true
                }
                if (keyEntry is KeyStore.PrivateKeyEntry && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val secretKey = secretKeyFromSharedPreferences
                    // When doing "Clear data" on Android 4.x it removes the shared preferences (where
                    // we have stored our encrypted secret key) but not the key entry. Check for existence
                    // of key here as well.
                    if (!TextUtils.isEmpty(secretKey)) {
                        keyValid = true
                    }
                }
            } catch (e: NullPointerException) {
                // Bad to catch null pointer exception, but looks like Android 4.4.x
                // pin switch to password Keystore bug.
                // https://issuetracker.google.com/issues/36983155
//                Log.e(LOG_TAG, "Failed to get key store entry", e)
            } catch (e: UnrecoverableKeyException) {
//                Log.e(LOG_TAG, "Failed to get key store entry", e)
            }
            if (!keyValid) {
                synchronized(s_keyInitLock) {

                    // System upgrade or something made key invalid
                    removeKeys(keyStore)
                    initValidKeys()
                }
            }
        }
    }

    @Throws(KeyStoreException::class)
    protected fun removeKeys(keyStore: KeyStore) {
        keyStore.deleteEntry(mContext.getString(R.string.minealias))
        removeSavedSharedPreferences()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class, CertificateException::class, UnrecoverableEntryException::class, NoSuchPaddingException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    private fun initValidKeys() {
        synchronized(s_keyInitLock) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                generateKeysForAPIMOrGreater()
            } else {
                generateKeysForAPILessThanM()
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun removeSavedSharedPreferences() {
        val sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_key_name), Context.MODE_PRIVATE)
        val clearedPreferencesSuccessfully = sharedPreferences.edit().clear().commit()
//        Log.d(LOG_TAG, String.format("Cleared secret key shared preferences `%s`", clearedPreferencesSuccessfully))
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class, CertificateException::class, UnrecoverableEntryException::class, NoSuchPaddingException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    private fun generateKeysForAPILessThanM() {
        // Generate a key pair for encryption
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 30)
        val spec = KeyPairGeneratorSpec.Builder(mContext)
                .setAlias(mContext.getString(R.string.minealias))
                .setSubject(X500Principal("CN=$mContext.getString(R.string.minealias)"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
        val kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM_NAME, mContext.getString(R.string.android))
        kpg.initialize(spec)
        kpg.generateKeyPair()
        saveEncryptedKey()
    }

    @SuppressLint("ApplySharedPref")
    @Throws(CertificateException::class, NoSuchPaddingException::class, InvalidKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, UnrecoverableEntryException::class, IOException::class)
    private fun saveEncryptedKey() {
        val pref = mContext.getSharedPreferences(mContext.getString(R.string.shared_key_name), Context.MODE_PRIVATE)
        var encryptedKeyBase64encoded = pref.getString( mContext.getString(R.string.enc_name), null)
        if (encryptedKeyBase64encoded == null) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncryptKey(key)
            encryptedKeyBase64encoded = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString( mContext.getString(R.string.enc_name), encryptedKeyBase64encoded)
            val successfullyWroteKey = edit.commit()
            if (successfullyWroteKey) {
//                Log.d(LOG_TAG, "Saved keys successfully")
            } else {
//                Log.e(LOG_TAG, "Saved keys unsuccessfully")
                throw IOException("Could not save keys")
            }
        }
    }

    @get:Throws(CertificateException::class, NoSuchPaddingException::class, InvalidKeyException::class,
            NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class,
            UnrecoverableEntryException::class, IOException::class)
    private val secretKeyAPILessThanM: Key
        private get() {
            val encryptedKeyBase64Encoded = secretKeyFromSharedPreferences
            if (TextUtils.isEmpty(encryptedKeyBase64Encoded)) {
                throw InvalidKeyException("Saved key missing from shared preferences")
            }
            val encryptedKey = Base64.decode(encryptedKeyBase64Encoded, Base64.DEFAULT)
            val key = rsaDecryptKey(encryptedKey)
            return SecretKeySpec(key, "AES")
        }

    private val secretKeyFromSharedPreferences: String
        private get() {
            val sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.shared_key_name), Context.MODE_PRIVATE)
            return sharedPreferences.getString( mContext.getString(R.string.enc_name), "")?:""
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    protected fun generateKeysForAPIMOrGreater() {
        val keyGenerator: KeyGenerator
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, mContext.getString(R.string.android))
        keyGenerator.init(
                KeyGenParameterSpec.Builder(mContext.getString(R.string.minealias),
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE) // NOTE no Random IV. According to above this is less secure but acceptably so.
                        .setRandomizedEncryptionRequired(false)
                        .build())
        // Note according to [docs](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html)
        // this generation will also add it to the keystore.
        keyGenerator.generateKey()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, UnrecoverableEntryException::class, CertificateException::class, KeyStoreException::class, IOException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, NoSuchProviderException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun encryptData(stringDataToEncrypt: String?): String {

        initKeys()
        requireNotNull(stringDataToEncrypt) { "Data to be decrypted must be non null" }
        val cipher: Cipher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cipher = Cipher.getInstance(mContext.getString(R.string.gcmenc))
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyAPIMorGreater,
                    GCMParameterSpec(128, FIXED_IV))
        } else {
            cipher = Cipher.getInstance( mContext.getString(R.string.ecbenc), CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_AES)
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeyAPILessThanM)
            } catch (e: InvalidKeyException) {
                // Since the keys can become bad (perhaps because of lock screen change)
                // drop keys in this case.
                removeKeys()
                throw e
            } catch (e: IOException) {
                removeKeys()
                throw e
            } catch (e: IllegalArgumentException) {
                removeKeys()
                throw e
            }
        }
        val encodedBytes = cipher.doFinal(stringDataToEncrypt.toByteArray(charset(CHARSET_NAME)))
       val e=Base64.encodeToString(encodedBytes, Base64.DEFAULT)

        return e
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class, UnrecoverableEntryException::class, CertificateException::class, KeyStoreException::class, IOException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, NoSuchProviderException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun decryptData(encryptedData: String?): String {
        initKeys()
        requireNotNull(encryptedData) { "Data to be decrypted must be non null" }
        val encryptedDecodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val c: Cipher
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c = Cipher.getInstance(mContext.getString(R.string.gcmenc))
                c.init(Cipher.DECRYPT_MODE, secretKeyAPIMorGreater, GCMParameterSpec(128, FIXED_IV))
            } else {
                c = Cipher.getInstance( mContext.getString(R.string.ecbenc), CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_AES)
                c.init(Cipher.DECRYPT_MODE, secretKeyAPILessThanM)
            }
        } catch (e: InvalidKeyException) {
            // Since the keys can become bad (perhaps because of lock screen change)
            // drop keys in this case.
            removeKeys()
            throw e
        } catch (e: IOException) {
            removeKeys()
            throw e
        }
        val decodedBytes = c.doFinal(encryptedDecodedData)
        return String(decodedBytes, Charset.forName(CHARSET_NAME))
    }

    @get:Throws(CertificateException::class, NoSuchAlgorithmException::class, IOException::class, KeyStoreException::class, UnrecoverableKeyException::class)
    private val secretKeyAPIMorGreater: Key
        private get() {
            val keyStore = KeyStore.getInstance(mContext.getString(R.string.android))
            keyStore.load(null)
            return keyStore.getKey(mContext.getString(R.string.minealias), null)
        }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, NoSuchProviderException::class, NoSuchPaddingException::class, UnrecoverableEntryException::class, InvalidKeyException::class)
    private fun rsaEncryptKey(secret: ByteArray): ByteArray {
        val keyStore = KeyStore.getInstance(mContext.getString(R.string.android))
        keyStore.load(null)
        val privateKeyEntry = keyStore.getEntry(mContext.getString(R.string.minealias), null) as KeyStore.PrivateKeyEntry
        val inputCipher = Cipher.getInstance(mContext.getString(R.string.rsaEnc), CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA)
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()
        return outputStream.toByteArray()
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, UnrecoverableEntryException::class, NoSuchProviderException::class, NoSuchPaddingException::class, InvalidKeyException::class)
    private fun rsaDecryptKey(encrypted: ByteArray): ByteArray {
        val keyStore = KeyStore.getInstance(mContext.getString(R.string.android))
        keyStore.load(null)
        val privateKeyEntry = keyStore.getEntry(mContext.getString(R.string.minealias), null) as KeyStore.PrivateKeyEntry
        val output = Cipher.getInstance(mContext.getString(R.string.rsaEnc), CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA)
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(
                ByteArrayInputStream(encrypted), output)
        val values = ArrayList<Byte>()
        var nextByte: Int
        while (cipherInputStream.read().also { nextByte = it } != -1) {
            values.add(nextByte.toByte())
        }
        val decryptedKeyAsBytes = ByteArray(values.size)
        for (i in decryptedKeyAsBytes.indices) {
            decryptedKeyAsBytes[i] = values[i]
        }
        return decryptedKeyAsBytes
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class)
    fun removeKeys() {
        synchronized(s_keyInitLock) {
            val keyStore = KeyStore.getInstance(mContext.getString(R.string.android))
            keyStore.load(null)
            removeKeys(keyStore)
        }
    }
    fun getAlphaNumericString(n: Int): String? {


        var n = n
        val array = ByteArray(256)
        Random().nextBytes(array)
        val randomString = String(array, Charset.forName("UTF-8"))


        val r = StringBuffer()


        for (k in 0 until randomString.length) {
            val ch = randomString[k]
            if ((ch in 'a'..'z'       || ch in 'A'..'Z'
                            || ch in '0'..'9')
                    && n > 0) {
                r.append(ch)
                n--
            }
        }

        // return the resultant string
        return r.toString()
    }
    companion object {
//        private const val ANDROID_KEY_STORE_NAME = R.string.android
//        private const val AES_MODE_M_OR_GREATER = mContext.getString(R.string.gcmenc)
//        private const val AES_MODE_LESS_THAN_M = mContext.getString(R.string.ecbenc)
//        private const val KEY_ALIAS = mContext.getString(R.string.minealias)
        private var instance: CryptoUtil? = null

        private val FIXED_IV = byteArrayOf(55, 54, 53, 52, 51, 50,
                49, 48, 47,
                46, 45, 44)
        private val CHARSET_NAME: String = "UTF-8"
        private const val RSA_ALGORITHM_NAME = "RSA"
//        private const val RSA_MODE = mContext.getString(R.string.rsaEnc)
        private const val CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_RSA = "AndroidOpenSSL"
        private const val CIPHER_PROVIDER_NAME_ENCRYPTION_DECRYPTION_AES = "BC"
//        private const val SHARED_PREFERENCE_NAME = mContext.getString(R.string.shared_key_name)
//        private const val ENCRYPTED_KEY_NAME = mContext.getString(R.string.key_name)
        private val LOG_TAG = CryptoUtil::class.java.name
        private val s_keyInitLock = Any()
        fun getInstance(contxt: Context): CryptoUtil {
            if (instance == null) {
                instance = CryptoUtil(contxt)
            }
            return instance as CryptoUtil
        }
    }

}