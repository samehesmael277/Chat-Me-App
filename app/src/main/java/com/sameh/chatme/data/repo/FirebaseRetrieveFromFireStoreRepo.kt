package com.sameh.chatme.data.repo

import androidx.lifecycle.LiveData
import com.sameh.chatme.data.model.User

interface FirebaseRetrieveFromFireStoreRepo {

    suspend fun getAllUser(): List<User>?

    suspend fun getCurrentUser(): User?

    suspend fun searchOnUsers(name: String)

    val searchUsersLiveData: LiveData<ArrayList<User?>>

}