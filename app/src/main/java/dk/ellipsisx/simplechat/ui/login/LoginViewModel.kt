package dk.ellipsisx.simplechat.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.ellipsisx.simplechat.App
import dk.ellipsisx.simplechat.R
import dk.ellipsisx.simplechat.core.arch.Event
import dk.ellipsisx.simplechat.data.model.User
import dk.ellipsisx.simplechat.ui.base.BaseViewModel
import dk.ellipsisx.simplechat.util.Log
import java.util.*

class LoginViewModel : BaseViewModel() {

    private val firebaseDatabase = Firebase.firestore

    private val _gotoConversationLiveData = MutableLiveData<Event<Boolean>>()
    val gotoConversationLiveData: LiveData<Event<Boolean>>
        get() = _gotoConversationLiveData

    //region Init
    //************************************************************

    fun init() {
    }

    //region Misc
    //************************************************************

    fun login(username: String) {
        _waitingLiveData.value = true
        getUser(username)
    }

    private fun login(user: User) {
        App.appCore.session.userLoggedIn(user)
        _gotoConversationLiveData.value = Event(true)
    }

    //endregion

    //region User Repository
    //************************************************************

    private fun getUser(username: String) {
        firebaseDatabase.collection("users")
            .whereEqualTo(User.name, username)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("getUser - success")
                val doc = documents.firstOrNull()
                if (doc != null) {
                    val name = doc.getString(User.name) ?: "N/A"
                    val createdDateTime = doc.getDate(User.createdDateTime) ?: Date()
                    val user = User(doc.id, name, createdDateTime)
                    login(user)
                    _waitingLiveData.value = false
                } else {
                    createUser(username)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("getUser - Error getting documents: ", exception)
                _waitingLiveData.value = false
                _errorLiveData.value = Event(R.string.app_error_unknown)
            }
    }

    private fun createUser(username: String) {
        val user = hashMapOf(
            User.name to username,
            User.createdDateTime to FieldValue.serverTimestamp()
        )

        firebaseDatabase.collection(User.users)
            .add(user)
            .addOnSuccessListener { documentReference ->
                val id = documentReference.id
                Log.d("createUser - DocumentSnapshot added with ID: $id")
                login(User(id, username, Date()))
                _waitingLiveData.value = false
            }
            .addOnFailureListener { e ->
                Log.w("createUser - Error adding document", e)
                _waitingLiveData.value = false
                _errorLiveData.value = Event(R.string.app_error_unknown)
            }
    }

    //endregion

}