package com.bo.helper.presentation.splash

import com.bo.helper.common.navigation.Command
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import com.bo.helper.presentation.home.HomeActivity
import kotlinx.coroutines.delay
import javax.inject.Inject

class SplashViewModel @Inject constructor() : BaseViewModel() {

    fun navigate() {
        executeOnUI {
            delay(200)
            navigationEvent.value = Command.Navigate(HomeActivity::class.java)
        }
    }
}