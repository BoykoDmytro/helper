package com.bo.helper.presentation.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bo.helper.databinding.ActivitySplashBinding
import com.bo.helper.presentation.base.BaseActivity
import com.bo.helper.presentation.base.viewmodel.BaseViewModel

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigate()
    }

    override fun getBaseViewModel(): BaseViewModel = viewModel

    override fun inflateViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
}