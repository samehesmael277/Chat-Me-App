package com.sameh.chatme.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.User
import com.sameh.chatme.data.repo.FirebaseRepoImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repo: FirebaseRepoImp
): ViewModel() {

//    // repo
//    val firebaseAuthentication: FirebaseAuthentication = FirebaseAuthentication()
//    val firebaseStorageSaveDataRegister: FirebaseStorageSaveDataRegister = FirebaseStorageSaveDataRegister()
//    val firebaseFireStoreSaveData: FirebaseFireStoreSaveData = FirebaseFireStoreSaveData()
//    val firebaseRetrieveFromFireStore: FirebaseRetrieveFromFireStore =
//        FirebaseRetrieveFromFireStore()
//    val firebaseRealTimeDatabase: FirebaseRealTimeDatabase = FirebaseRealTimeDatabase()
//    val firebaseStorageProfile: FirebaseStorageProfile = FirebaseStorageProfile()
//    private val repo: FirebaseRepoImp =
//        FirebaseRepoImp(
//            firebaseAuthentication,
//            firebaseStorageSaveDataRegister,
//            firebaseFireStoreSaveData,
//            firebaseRetrieveFromFireStore,
//            firebaseRealTimeDatabase,
//            firebaseStorageProfile
//        )

    // live data
    private var _currentUserMutableLiveData: MutableLiveData<User?> = MutableLiveData<User?>()
    val currentUserLiveData: LiveData<User?> get() = _currentUserMutableLiveData

    // methods
    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentUser = repo.getCurrentUser()
                _currentUserMutableLiveData.postValue(currentUser)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "getCurrentUser: ${e.message}")
            }
        }
    }

    fun uploadPhotoToFirebaseStorageProfile(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.uploadPhotoToFirebaseStorageProfile(uri)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "uploadPhotoToFirebaseStorageProfile: ${e.message}")
            }
        }
    }

    fun deleteImageFromFireStorageProfile(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteImageFromFireStorageProfile(fileName)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "deleteImageFromFireStorageProfile: ${e.message}")
            }
        }
    }

    fun updateUserInFireStoreDB(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.updateUserInFireStoreDB(user)
            } catch (e: Exception) {
                Log.d(Constants.TAG, "updateUserInFireStoreDB: ${e.message}")
            }
        }
    }

}