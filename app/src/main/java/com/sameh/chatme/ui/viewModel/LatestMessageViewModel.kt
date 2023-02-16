package com.sameh.chatme.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.LatestUserMessage
import com.sameh.chatme.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LatestMessageViewModel @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    // live data
    private var _currentUserProfileImageMutableLiveData: MutableLiveData<String?> =
        MutableLiveData<String?>()
    val currentUserProfileImageLiveData: LiveData<String?> get() = _currentUserProfileImageMutableLiveData

    val latestUserAndMessagesLiveData: LiveData<ArrayList<LatestUserMessage?>?> get() = repo.latestUserAndMessageLiveData

    val searchOnUsersLiveData: LiveData<ArrayList<LatestUserMessage?>?> get() = repo.searchOnUsersLiveData

    // methods
    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repo.getCurrentUser()
                _currentUserProfileImageMutableLiveData.postValue(user?.profileImgUrl)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }

    fun getLatestUserAndMessages(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getLatestUserAndMessages(userId)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getLatestUserAndMessages: ${e.message}")
            }
        }
    }

    fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchOnLatestUserAndMessages(senderId, name)
        }
    }

}