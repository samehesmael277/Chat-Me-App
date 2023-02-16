package com.sameh.chatme.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.Message
import com.sameh.chatme.data.model.User
import com.sameh.chatme.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    // live data
    val messagesLiveData: LiveData<ArrayList<Message?>?>
        get() = repo.messagesLiveData

    private var _currentUserMutableLiveData: MutableLiveData<User?> = MutableLiveData()
    val currentUserLiveData: LiveData<User?> get() = _currentUserMutableLiveData

    // methods
    fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.sendMessage(message, senderRoom, receiverRoom, userReceiver, userSender)
        }
    }

    fun getMessages(senderRoom: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMessages(senderRoom)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repo.getCurrentUser()
                _currentUserMutableLiveData.postValue(user)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }

}