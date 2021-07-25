package com.bo.helper.presentation.base.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bo.helper.R
import com.bo.helper.common.event.UIEvent
import com.bo.helper.common.extention.observe
import com.bo.helper.common.navigation.Command
import com.bo.helper.common.type.ContentType
import com.bo.helper.data.ErrorResult
import com.bo.helper.data.INTERNET_CONNECTION_ERROR
import com.bo.helper.presentation.base.BaseActivity
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : DaggerFragment() {

    protected val viewBinding: VB get() = _viewBinding!!
    protected val router get() = baseActivity?.router

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _viewBinding: VB? = null
    private var baseActivity: BaseActivity<*>? = null

    abstract fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as? BaseActivity<*>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = bindViewBinding(inflater, container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseViewModel()?.apply {
            observe(uiEvent, { handleUIEvent(it) })
            observe(navigationEvent, { handleGlobalNavigation(it) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    open fun handleGlobalNavigation(command: Command) {
        baseActivity?.handleGlobalNavigation(command)
    }

    open fun getBaseViewModel(): BaseViewModel? = null

    protected fun setWindowBackground(@DrawableRes res: Int) {
        activity?.window?.setBackgroundDrawableResource(res)
    }

    fun showErrorToast(error: String?) {
        baseActivity?.showErrorToast(error)
    }

    fun showErrorToast(@StringRes resId: Int) {
        baseActivity?.showErrorToast(getString(resId))
    }

    fun verifyPermission(
        vararg permission: String,
        accepted: () -> Unit? = { null },
        decline: () -> Unit? = { null },
    ) {
        baseActivity?.verifyPermission(
            *permission,
            accepted = { accepted() },
            decline = { decline() })
    }

    protected fun notImplementedToast() {
        activity?.let { Toast.makeText(it, R.string.not_implemented, Toast.LENGTH_SHORT).show() }
    }

    protected fun showAppBar() = baseActivity?.showAppBar()

    protected fun hideAppBar() = baseActivity?.hideAppBar()

    protected fun setAppBarTitle(@StringRes titleResId: Int) =
        baseActivity?.setAppBarTitle(titleResId)

    protected fun setAppBarTitle(title: String) = baseActivity?.setAppBarTitle(title)

    protected fun lockDrawerLayout() = baseActivity?.lockDrawerLayout()

    protected fun unlockDrawerLayout() = baseActivity?.unlockDrawerLayout()

    protected open fun handleUIEvent(uiEvent: UIEvent) {
        when (uiEvent) {
            is UIEvent.LoadingUIEvent -> handleProgress(uiEvent)
            is UIEvent.FailureUIEvent<*> -> {
                if (uiEvent.errorResult.code == INTERNET_CONNECTION_ERROR) {
                    baseActivity?.showNoInternetConnectionError()
                } else {
                    handleError(uiEvent.errorResult)
                }
            }
            is UIEvent.NetworkUIEvent -> TODO()
            is UIEvent.AlertEvent -> TODO()
            is UIEvent.ErrorUIEvent -> handleErrorUIEvent(uiEvent)
        }
    }

    open fun handleError(errorResult: ErrorResult<out Any?>) {
        baseActivity?.handleError(errorResult)
    }

    open fun handleErrorUIEvent(event: UIEvent.ErrorUIEvent) {
        when (event.errors.firstOrNull()) {
            else -> Unit
        }
    }

    private fun handleProgress(event: UIEvent.LoadingUIEvent) {
        when (event.isLoading) {
            true -> baseActivity?.showProgressBar()
            false -> baseActivity?.hideProgressBar()
        }
    }

    //region dialogs
    protected fun showErrorDialog(@StringRes messageId: Int, @StringRes titleId: Int = R.string.error) {
        baseActivity?.showErrorDialog(getString(messageId), getString(titleId))
    }

    protected fun showErrorDialog(errorMessage: String, title: String = getString(R.string.error)) {
        baseActivity?.showErrorDialog(errorMessage, title)
    }
    //endregion

    protected fun openVideoPlayer(path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(path), ContentType.VIDEO.type)
        startActivity(intent)
    }

    protected fun openImage(path: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(path), ContentType.IMAGE.type)
        startActivity(intent)
    }
}