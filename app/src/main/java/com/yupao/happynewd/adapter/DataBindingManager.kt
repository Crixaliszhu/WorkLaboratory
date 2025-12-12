package com.yupao.happynewd.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


/**
 * 封装一下DataBinding相关的操作，避免忘记内存泄露
 */
class DataBindingManager<T : ViewDataBinding> constructor(private var _binding: T? = null) :
    DefaultLifecycleObserver {

    companion object {
        fun <T : ViewDataBinding> inflateActivity(
            @LayoutRes
            layoutId: Int,
            activity: FragmentActivity,
            bindingInit: ((T) -> Unit)? = null
        ): DataBindingManager<T> {
            val binding = DataBindingUtil.setContentView<T>(activity, layoutId)
            bindingInit?.let {
                it.invoke(binding)
            }
            return DataBindingManager(binding).also {
                activity.lifecycle.addObserver(it)
            }
        }

        fun <T : ViewDataBinding> inflateFragment(
            @LayoutRes
            layoutId: Int,
            inflater: LayoutInflater,
            container: ViewGroup?,
            owner: LifecycleOwner,
            bindingInit: ((T) -> Unit)? = null
        ): DataBindingManager<T> {
            val binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
            bindingInit?.let {
                it.invoke(binding)
            }
            return DataBindingManager(binding).also {
                owner.lifecycle.addObserver(it)
            }
        }

        fun <T : ViewDataBinding> inflate(
            binding: T?,
            owner: LifecycleOwner,
            bindingInit: ((T) -> Unit)? = null
        ): DataBindingManager<T> {
            binding ?: return DataBindingManager(binding)
            bindingInit?.let {

                it.invoke(binding)
            }
            return DataBindingManager(binding).also {
                owner.lifecycle.addObserver(it)
            }
        }
    }


    val binding = _binding

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        _binding?.lifecycleOwner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        _binding?.unbind()
        _binding = null
    }
}