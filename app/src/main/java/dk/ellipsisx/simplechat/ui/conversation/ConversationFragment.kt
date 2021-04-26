package dk.ellipsisx.simplechat.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dk.ellipsisx.simplechat.App
import dk.ellipsisx.simplechat.R
import dk.ellipsisx.simplechat.data.model.Message
import dk.ellipsisx.simplechat.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_conversation.*

class ConversationFragment : BaseFragment() {

    private val viewModel: ConversationViewModel by viewModels()
    private val adapter = ConversationAdapter(App.appCore.session.user!!)

    //region Lifecycle
    //************************************************************

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_conversation, container, false)
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

        viewModel.messageListLiveData.observe(viewLifecycleOwner, Observer<List<Message>> { list ->
            adapter.setData(list)
        })
    }

    private fun initViews() {
        initMessageList()
        send_button.setOnClickListener { onSendMessageClicked() }
    }

    private fun initMessageList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true

        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = linearLayoutManager
        recyclerview.adapter = adapter

        input_edittext.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    onSendMessageClicked()
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

    override fun showError(stringRes: Int) {
        Snackbar.make(root_layout, stringRes, Snackbar.LENGTH_LONG).show()
    }

    private fun onSendMessageClicked() {
        val input = input_edittext.text.toString()
        if (input.trim().isEmpty()) {
            return
        }
        viewModel.sendMessage(input)
        input_edittext.setText("")
    }

    //endregion
}