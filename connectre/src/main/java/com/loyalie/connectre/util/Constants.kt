package com.loyalie.connectre.util

const val IMAGE_PATH = "PATH"
const val ROTATED_ANGLE = "ROTATED_ANGLE"
const val FINAL_IMAGE_PATH = "FINAL_IMAGE_PATH"
const val FROM_PUSH = "from_push"

const val TERMS_CONDITION_URL = "https://loyalie.in/terms_conditions.html"
const val PRIVACY_POLICY_URL = "https://loyalie.in/privacypolicy.html"
const val TERMS_SHAPOORJI_URL="https://shapoorji.loyalie.in/terms_conditions_referral.html"
 var BASE_URL="http://18.140.57.92:8080/Loyalie/"
// create hash for SMS retriever api
//class AppSignatureHelper(context: Context): ContextWrapper(context) {
//
//    companion object {
//        val TAG = AppSignatureHelper::class.java.simpleName
//        private val HASH_TYPE = "SHA-256"
//        val NUM_HASHED_BYTES = 9
//        val NUM_BASE64_CHAR = 11
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getAppSignatures(): ArrayList<String> {
//        val appCodes = ArrayList<String>()
//
//        return try {
//            // Get all package signatures for the current package
//            val packageName = packageName
//            val packageManager = packageManager
//            val signatures = packageManager.getPackageInfo(packageName,
//                PackageManager.GET_SIGNATURES).signatures
//
//            // For each signature create a compatible hash
//            signatures
//                .mapNotNull { hash(packageName, it.toCharsString()) }
//                .mapTo(appCodes) { it }
//            return appCodes
//        } catch (e: PackageManager.NameNotFoundException) {
//            Log.e(TAG, "Unable to find package to obtain hash.", e)
//            ArrayList<String>()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun hash(packageName: String, signature: String): String? {
//        val appInfo = packageName + " " + signature
//        return try {
//            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
//            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
//            var hashSignature = messageDigest.digest()
//
//            // truncated into NUM_HASHED_BYTES
//            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
//            // encode into Base64
//            var base64Hash = Base64.getEncoder().encodeToString(hashSignature)
//            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
//
//            Log.d(TAG, "pkg: $packageName -- hash: $base64Hash")
//            base64Hash
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e(TAG, "hash:NoSuchAlgorithm", e)
//            null
//        }
//    }
//
//}
