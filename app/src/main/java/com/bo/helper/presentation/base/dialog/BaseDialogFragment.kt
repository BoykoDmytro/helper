package com.bo.helper.presentation.base.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bo.helper.common.event.UIEvent
import com.bo.helper.common.extention.observe
import com.bo.helper.data.INTERNET_CONNECTION_ERROR
import com.bo.helper.presentation.base.BaseActivity
import com.bo.helper.presentation.base.viewmodel.BaseViewModel
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

abstract class BaseDialogFragment<VB : ViewBinding> : DaggerDialogFragment() {

    open val viewModel: BaseViewModel? = null

    protected val viewBinding: VB get() = _viewBinding!!

    private var _viewBinding: VB? = null
    private var baseActivity: BaseActivity<*>? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract fun bindViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as? BaseActivity<*>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = let {
        _viewBinding = bindViewBinding(inflater, container)
        viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.apply { observe(uiEvent, ::handleUIEvent) }
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    open fun handleErrorUIEvent(event: UIEvent.ErrorUIEvent) {}

    private fun handleUIEvent(event: UIEvent) {
        when (event) {
            is UIEvent.LoadingUIEvent -> if (event.isLoading) baseActivity?.showProgressBar() else baseActivity?.hideProgressBar()
            is UIEvent.FailureUIEvent<*> -> {
                if (event.errorResult.code == INTERNET_CONNECTION_ERROR) {
                    baseActivity?.showNoInternetConnectionError()
                } else {
                    baseActivity?.handleError(event.errorResult)
                }
            }
            is UIEvent.ErrorUIEvent -> handleErrorUIEvent(event)
            else -> Unit
        }
    }
}