@file:Suppress("DEPRECATION")
package com.sehentak.base.func.encryption

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import com.sehentak.base.R
import com.sehentak.base.helper.Date
import com.sehentak.base.helper.Log
import com.sehentak.base.helper.Preference
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by angger on 23/02/19.
 */

class BaseCrypto: OnCryptoExecute {
    private val tagClass = this::class.java.simpleName
    private lateinit var context: Context

    private var algorithmAES = ""
    private var algorithmMD5 = ""
    private var methodEncrypt = ""
    private var methodDecrypt = ""
    private var paddingFormat = ""
    private var paddingMethod = ""
    private var ivSize = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private lateinit var instance: BaseCrypto

        fun getInstance(context: Context): BaseCrypto {
            instance = BaseCrypto()
            instance.context = context
            instance.algorithmAES = context.getString(R.string.prop_enigma_algorithm_aes)
            instance.algorithmMD5 = context.getString(R.string.prop_enigma_algorithm_md5)
            instance.methodEncrypt = context.getString(R.string.prop_enigma_method_encrypt)
            instance.methodDecrypt = context.getString(R.string.prop_enigma_method_decrypt)
            instance.paddingFormat = context.getString(R.string.prop_enigma_digest)
            instance.paddingMethod = context.getString(R.string.prop_enigma_padding)
            instance.ivSize = context.resources.getInteger(R.integer.prop_enigma_iv_size)
            return instance
        }
    }

    override fun encrypt(data: String?): String? {
        val asyncTask = CryptoProcessing(methodEncrypt)
        return asyncTask.execute(data).get()
    }

    override fun decrypt(data: String?): String? {
        val asyncTask = CryptoProcessing(methodDecrypt)
        return asyncTask.execute(data).get()
    }

    override fun encrypt(key: String?, data: String?): String? {
        if (key != null && !TextUtils.isEmpty(key) && data != null && !TextUtils.isEmpty(data)) {
            val asyncTask = CryptoProcessing(methodEncrypt)
            var string = asyncTask.execute(data).get()
            if (string != null) {
                string = StringBuilder(string).reverse().toString().trim()
            }

            val repeat = "A".repeat(16)
            if (string != null && string.contains(repeat)) {
                val byte = "$key::".toByteArray()
                val build = Base64.encodeToString(byte, Base64.DEFAULT).trim()
                return string.replace(repeat, build)
            }
        }

        return null
    }

    override fun decrypt(key: String?, data: String?): String? {
        if (key != null && !TextUtils.isEmpty(key) && data != null && !TextUtils.isEmpty(data)) {
            var string = data
            val byte = "$key::".toByteArray()
            val build = Base64.encodeToString(byte, Base64.DEFAULT).trim()
            if (string.contains(build)) {
                string = string.replace(build, "A".repeat(16))
            }

            string = StringBuilder(string).reverse().toString().trim()
            val asyncTask = CryptoProcessing(methodDecrypt)
            return asyncTask.execute(string).get()
        }

        return null
    }

    @SuppressLint("StaticFieldLeak")
    private inner class CryptoProcessing(
        private val method: String
    ): AsyncTask<String, Void, String?>(){

        override fun doInBackground(vararg params: String): String? {
            val secretKeySpec = context.secretKey()
            return try {
                val data = params[0]
                return when (method) {
                    methodEncrypt -> {
                        val ivByteArray = ByteArray(ivSize)
                        val iv = IvParameterSpec(ivByteArray)
                        val cipher = Cipher.getInstance(paddingMethod)
                        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)
                        val cipherText = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
                        val ivAndCipherText = getCombinedArray(ivByteArray, cipherText)
                        Base64.encodeToString(ivAndCipherText, Base64.NO_WRAP)
                    }
                    methodDecrypt -> {
                        val ivAndCipherText = Base64.decode(data, Base64.NO_WRAP)
                        val cipherText = Arrays.copyOfRange(ivAndCipherText, ivSize, ivAndCipherText.size)
                        val ivByteArray = Arrays.copyOfRange(ivAndCipherText, 0, ivSize)
                        val iv = IvParameterSpec(ivByteArray)
                        val cipher = Cipher.getInstance(paddingMethod)
                        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)
                        cipher.doFinal(cipherText).toString(Charsets.UTF_8)
                    }
                    else -> null
                }
            } catch (e: Exception) {
                Log.debug(tagClass, "$method - $params: ${e.message}")
                null
            }
        }
    }

    private fun Context.secretKey(): SecretKeySpec {
        var key = Preference.getPrimaryKey(this)
        if (key == null || TextUtils.isEmpty(key)) {
            key = generateKey()
            Preference.savePrimaryKey(this, key)
        }

        val tokenBytes = key.toByteArray(Charsets.UTF_8)
        return SecretKeySpec(tokenBytes, algorithmAES)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds")
    private fun generateKey(): String {
        var token = "${Build.SERIAL}-${Date.millis(true)}"
        if (token.length < 128) {
            val data = token.toByteArray(Charsets.UTF_8)
            val md5 = MessageDigest.getInstance(algorithmMD5)
            md5.update(data)
            val digest = BigInteger(1, md5.digest())
            token = String.format(paddingFormat, digest)
            token = token.replace(" ", "0")
        }

        val bytes = Base64.decode(token, Base64.DEFAULT)
        token = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return token
    }

    private fun getCombinedArray(one: ByteArray, two: ByteArray): ByteArray {
        val combined = ByteArray(one.size + two.size)
        for (i in combined.indices) {
            combined[i] = if (i < one.size) one[i] else two[i - one.size]
        }
        return combined
    }
}