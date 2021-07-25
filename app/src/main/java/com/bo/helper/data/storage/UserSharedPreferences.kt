package com.bo.helper.data.storage

import android.content.Context
import javax.inject.Inject

class UserSharedPreferences @Inject constructor(context: Context) :
    BaseEncryptedSharedPreferences(context, USER) {

    companion object {
        const val USER = "user"
    }

    fun logOut() {
        sharedPreferences.edit().clear().apply()
    }
}