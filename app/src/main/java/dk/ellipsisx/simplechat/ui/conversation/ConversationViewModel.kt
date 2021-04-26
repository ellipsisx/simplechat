package dk.ellipsisx.simplechat.ui.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.ellipsisx.simplechat.App
import dk.ellipsisx.simplechat.R
import dk.ellipsisx.simplechat.core.arch.Event
import dk.ellipsisx.simplechat.data.model.Message
import dk.ellipsisx.simplechat.ui.base.BaseViewModel
import dk.ellipsisx.simplechat.util.Log
import java.util.*
import kotlin.collections.ArrayList

class ConversationViewModel : BaseViewModel() {

    private val firebaseDatabase = Firebase.firestore

    private val _messageListLiveData = MutableLiveData<List<Message>>()
    val messageListLiveData: LiveData<List<Message>>
        get() = _messageListLiveData

    //region Init
    //************************************************************

    fun init() {
        getMessages()
    }

    //region Message Repository
    //************************************************************

    private fun updateMessages(messageList: ArrayList<Message>) {
        messageList.sortByDescending { it.createdDateTime.time }
        _messageListLiveData.value = messageList
    }

    //endregion

    //region Message Repository
    //************************************************************

    fun sendMessage(messageText: String) {
        val fromUser = App.appCore.session.user ?: return

        val message = hashMapOf(
            Message.text to messageText,
            Message.createdDateTime to FieldValue.serverTimestamp(),
            Message.fromName to fromUser.name
        )

        firebaseDatabase.collection(Message.messages)
            .add(message)
            .addOnSuccessListener { documentReference ->
                Log.d("getMessages - DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("getMessages - Error adding document", e)
                _errorLiveData.value = Event(R.string.conversation_error_send)
            }
    }

    private fun getMessages() {
        firebaseDatabase.collection(Message.messages)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("getMessages - Listen failed", e)
                    _errorLiveData.value = Event(R.string.app_error_unknown)
                    return@addSnapshotListener
                }

                val messageList = ArrayList<Message>()
                for (doc in value!!) {
                    val text = doc.getString(Message.text) ?: ""
                    val createdDateTime = doc.getDate(Message.createdDateTime) ?: Date()
                    val fromName = doc.getString(Message.fromName) ?: ""
                    messageList.add(Message(doc.id, text, createdDateTime, fromName))
                }

                updateMessages(messageList)
            }
    }

    //endregion
}