package com.bo.helper.presentation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bo.helper.R
import com.bo.helper.databinding.ActivityHomeBinding
import com.bo.helper.presentation.base.BaseActivity
import com.bo.helper.presentation.home.fragment.DiscountListFragment

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        fun getNavigationCommand(context: Context): Intent = Intent(context, HomeActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.aHomeRoot, DiscountListFragment())
            .commit()
    }

    override fun inflateViewBinding(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

}