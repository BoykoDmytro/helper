package com.bo.helper.presentation.base

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bo.helper.R
import com.bo.helper.common.event.UIEvent
import com.bo.helper.common.extention.doOnApplyWindowInsets
import com.bo.helper.common.extention.observe
import com.bo.helper.common.navigation.Command
import com.bo.helper.common.navigation.Router
import com.bo.helper.data.ErrorResult
import com.bo.helper.data.INTERNET_CONNECTION_ERROR
import com.bo.helper.presentation.base.dialog.ProgressDialog
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : DaggerAppCompatActivity() {

    companion object {
        private const val PROGRESS_DIALOG_DELAY = 500L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var router: Router

    private var toolbar: Toolbar? = null
    private var keyboardListenersAttached = false
    private var progressDialog: ProgressDialog? = null
    private lateinit var contentRootView: View

    private var _viewBinding: VB? = null
    protected val viewBinding: VB get() = _viewBinding!!

    abstract fun inflateViewBinding(): VB

    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        contentRootView.let {
            var heightDiff = resources.displayMetrics.heightPixels - it.height
            val navBarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val statusBarId = resources.getIdentifier("status_bar_height", "dimen", "android")
            takeIf { navBarId > 0 }?.let { heightDiff -= resources.getDimensionPixelSize(navBarId) }
            takeIf { statusBarId > 0 }?.let {
                heightDiff -= resources.getDimensionPixelSize(statusBarId)
            }
            if (heightDiff <= 0) onHideKeyboard()
            else onShowKeyboard()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = inflateViewBinding()
        setContentView(viewBinding.root)

        contentRootView = (findViewById<View>(android.R.id.content).parent) as View
        getBaseViewModel()?.apply {
            observe(uiEvent, ::handleUIEvent)
            observe(navigationEvent, ::handleGlobalNavigation)
        }
        attachKeyboardListeners()
        viewBinding.root.doOnApplyWindowInsets { view, insets, padding ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.updatePadding(top = padding.top + top)
            insets
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        this.toolbar = toolbar
        super.setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        _viewBinding = null
        if (keyboardListenersAttached)
            contentRootView.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
        super.onDestroy()
    }

    open fun handleGlobalNavigation(event: Command) {
        when (event) {
            Command.Back -> TODO()
            Command.Camera -> TODO()
            Command.Files -> TODO()
            Command.Video -> TODO()
            is Command.Navigate -> router.navigateTo(this, event)
        }

    }

    open fun handleErrors(errorUIEvent: UIEvent.ErrorUIEvent) {}

    open fun getBaseViewModel(): BaseViewModel? = null

    open fun lockDrawerLayout() {
        //Do nothing
    }

    open fun unlockDrawerLayout() {
        //Do nothing
    }

    open fun showProgressBar() {
        if (progressDialog == null) progressDialog = ProgressDialog.show(supportFragmentManager)
    }

    open fun hideProgressBar() {
        contentRootView.postDelayed({
            if (this@BaseActivity.isFinishing.not()) {
                progressDialog?.dismiss()
                progressDialog = null
            }
        }, PROGRESS_DIALOG_DELAY)
    }

    open fun showNoInternetConnectionError() {}

    open fun handleError(networkErrorUIModel: ErrorResult<*>) {
        showErrorLog(networkErrorUIModel.message)
        showErrorToast(networkErrorUIModel.message)
    }

    fun verifyPermission(
        vararg permissions: String,
        accepted: () -> Unit? = { null },
        decline: () -> Unit? = { null }
    ) {
        Dexter.withContext(this)
            .withPermissions(permissions.toMutableList())
            .withListener(object : BaseMultiplePermissionsListener() {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    super.onPermissionRationaleShouldBeShown(permissions, token)
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    super.onPermissionsChecked(report)
                    report?.takeIf { it.areAllPermissionsGranted() }?.let { accepted.invoke() }
                        ?: decline.invoke()
                }
            })
            .onSameThread()
            .check()
    }

    fun showErrorToast(error: String?) {
        val errorText = error ?: getString(R.string.error_unknown)
        Toast.makeText(applicationContext, errorText, Toast.LENGTH_LONG).show()
    }

    fun showErrorLog(error: String?) {
        val errorText = error ?: getString(R.string.error_unknown)
        Timber.e(errorText)
    }

    fun showAppBar() = supportActionBar?.show()

    fun hideAppBar() = supportActionBar?.hide()

    fun setBackButton(isEnabled: Boolean) {
        if (isEnabled) showAppBar()
        supportActionBar?.setDisplayHomeAsUpEnabled(isEnabled)
        supportActionBar?.setDisplayShowHomeEnabled(isEnabled)
        getDrawerToggle()?.syncState()
//        if (!isEnabled) toolbar?.setNavigationIcon(R.drawable.ic_menu)
//        else supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    fun setAppBarTitle(@StringRes titleResId: Int) {
        supportActionBar?.setTitle(titleResId)
    }

    fun setAppBarTitleWithCloseIcon(@StringRes titleResId: Int) {
        supportActionBar?.setTitle(titleResId)
    }

    fun setAppBarTitle(title: String) {
        supportActionBar?.title = title
    }

    protected open fun getDrawerToggle(): ActionBarDrawerToggle? = null

    protected fun notImplementedToast() {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show()
    }

    protected fun attachKeyboardListeners() {
        if (keyboardListenersAttached) return
        contentRootView.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
        keyboardListenersAttached = true
    }

    open fun onShowKeyboard() {}

    open fun onHideKeyboard() {}

    //region dialogs
    open fun showErrorDialog(@StringRes messageId: Int, @StringRes titleId: Int = R.string.error) {
        showErrorDialog(getString(messageId), getString(titleId))
    }

    open fun showErrorDialog(errorMessage: String, title: String = getString(R.string.error)) {}
    //endregion

    private fun handleUIEvent(event: UIEvent) {
        when (event) {
            is UIEvent.LoadingUIEvent -> handleProgress(event)
            is UIEvent.FailureUIEvent<*> -> {
                if (event.errorResult.code == INTERNET_CONNECTION_ERROR) {
                    showNoInternetConnectionError()
                } else {
                    handleError(event.errorResult)
                }
            }
            is UIEvent.NetworkUIEvent -> Unit
            is UIEvent.ErrorUIEvent -> handleErrors(event)
            is UIEvent.AlertEvent -> Unit
        }
    }

    private fun handleProgress(uiEvent: UIEvent.LoadingUIEvent) {
        when (uiEvent.isLoading) {
            true -> showProgressBar()
            false -> hideProgressBar()
        }
    }
}

