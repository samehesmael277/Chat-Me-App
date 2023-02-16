package com.sameh.chatme.data.repo

import com.sameh.chatme.data.model.User

interface FirebaseFireStoreSaveDataRepo {

    suspend fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?)

    suspend fun updateUserInFireStoreDB(user: User)

}