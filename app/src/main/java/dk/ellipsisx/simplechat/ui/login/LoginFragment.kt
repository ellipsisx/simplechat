package dk.ellipsisx.simplechat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dk.ellipsisx.simplechat.R
import dk.ellipsisx.simplechat.core.arch.EventObserver
import dk.ellipsisx.simplechat.ui.base.BaseFragment
import dk.ellipsisx.simplechat.util.KeyboardUtil
import kotlinx.android.synthetic.main.app_progress.*
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels()

    //region Lifecycle
    //************************************************************

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
    }

    //endregion

    //region Initialize
    //************************************************************

    private fun initViewModel() {
        super.initViewModel(viewModel)
        viewModel.init()

        viewModel.gotoConversationLiveData.observe(viewLifecycleOwner, object : EventObserver<Boolean>() {
            override fun onUnhandledEvent(value: Boolean) {
                gotToConversation()
            }
        })
    }

    private fun initViews() {
        showWaiting(false)

        login_button.setOnClickListener { onLoginClicked() }

        username_edittext.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onLoginClicked()
                    // Return false, so the keyboard will be automatically dismissed
                    false
                }
                else -> false
            }
        }
    }

    //endregion

    //region Misc
    //************************************************************

    override fun showWaiting(waiting: Boolean) {
        progress_layout.visibility = if (waiting) View.VISIBLE else View.GONE
    }

    override fun showError(stringRes: Int) {
        Snackbar.make(root_layout, stringRes, Snackbar.LENGTH_LONG).show()
    }

    private fun onLoginClicked() {
        val input = username_edittext.text.toString()
        if (input.trim().isEmpty()) {
            return
        }
        KeyboardUtil.hideKeyboard(this@LoginFragment)
        viewModel.login(input)
        username_edittext.setText("")
    }

    private fun gotToConversation() {
        findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationConversation())
    }

    //endregion
}