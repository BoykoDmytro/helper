package com.bo.helper.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.GsonBuilder

abstract class BaseEncryptedSharedPreferences(context: Context, preferencesName: String) {

    protected var sharedPreferences: SharedPreferences
    protected val gsonBuilder = GsonBuilder().create()

    companion object {
        private const val KEY_SIZE = 256
    }

    init {
        val builder = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()
        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(builder)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context, preferencesName, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}