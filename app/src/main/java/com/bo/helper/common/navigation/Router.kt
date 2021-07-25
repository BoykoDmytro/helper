package com.bo.helper.common.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.bo.helper.presentation.home.HomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor(val context: Context) {

    private fun navigateToHome(context: Context) {
        context.startActivity(HomeActivity.getNavigationCommand(context))
    }

    fun navigateTo(context: Context,event: Command.Navigate) {
        when (event.data) {
            HomeActivity::class.java -> navigateToHome(context)
            else -> navigateToActivity(event.data)
        }
    }

    private fun navigateToActivity(data: Class<*>) {
        if (Activity::class.java.isAssignableFrom(data)) {
            context.startActivity(Intent(context, data).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}