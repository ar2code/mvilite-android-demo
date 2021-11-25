package ru.ar2code.mvilite_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Fragment that uses viewBinding for inflating ui from xml.
 * You have access to [viewBinding] to work with UI elements that created and destroyed automatically.
 */
abstract class ViewBindingFragment<VB> :
    Fragment() where VB : ViewBinding {

    protected var viewBinding: VB? = null

    /**
     * Create and return [ViewBinding] instance for this fragment
     */
    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = createViewBinding(inflater, container, savedInstanceState)
        return viewBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}