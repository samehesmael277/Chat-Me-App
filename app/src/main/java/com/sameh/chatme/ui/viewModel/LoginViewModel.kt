package com.sameh.chatme.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repo: FirebaseRepoImp
) : ViewModel() {

    // methods
    fun singInWithEmailPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.singInWithEmailPassword(email, password)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "singInWithEmailPassword: ${e.message}")
            }
        }
    }

    fun checkEmailVerification() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repo.checkEmailVerification()
            } catch (e: Exception) {
                Log.d(Constants.TAG, "checkEmailVerification: ${e.message}")
            }
        }
    }

    fun sendEmailVerificationLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.sendEmailVerificationLogin()
            } catch (e: Exception) {
                Log.d(Constants.TAG, "sendEmailVerificationLogin: ${e.message}")
            }
        }
    }

}