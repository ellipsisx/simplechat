package dk.ellipsisx.simplechat.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dk.ellipsisx.simplechat.core.arch.EventObserver

abstract class BaseFragment : Fragment() {

    open fun initViewModel(baseViewModel: BaseViewModel) {
        baseViewModel.waitingLiveData.observe(viewLifecycleOwner, Observer<Boolean> { waiting ->
            waiting?.let {
                showWaiting(waiting)
            }
        })
        baseViewModel.errorLiveData.observe(viewLifecycleOwner, object : EventObserver<Int>() {
            override fun onUnhandledEvent(value: Int) {
                showError(value)
            }
        })
    }

    open fun showWaiting(waiting: Boolean) {

    }

    open fun showError(stringRes: Int) {

    }
}