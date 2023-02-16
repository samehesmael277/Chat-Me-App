package com.sameh.chatme.data.repo

import android.net.Uri
import androidx.lifecycle.LiveData
import com.sameh.chatme.data.model.LatestUserMessage
import com.sameh.chatme.data.model.Message
import com.sameh.chatme.data.model.User
import com.sameh.chatme.data.network.*
import javax.inject.Inject

class FirebaseRepoImp @Inject constructor(
    val firebaseAuthentication: FirebaseAuthentication,
    val firebaseStorageSaveDataRegister: FirebaseStorageSaveDataRegister,
    val firebaseFireStoreSaveData: FirebaseFireStoreSaveData,
    val firebaseRetrieveFromFireStore: FirebaseRetrieveFromFireStore,
    val firebaseRealTimeDatabase: FirebaseRealTimeDatabase,
    val firebaseStorageProfile: FirebaseStorageProfile
) : FirebaseAuthRepo,
    FirebaseStorageSaveDataRepo,
    FirebaseFireStoreSaveDataRepo,
    FirebaseRetrieveFromFireStoreRepo,
    FirebaseRealTimeDatabaseRepo,
    FirebaseStorageProfileRepo {

    // ********** Firebase Auth Register **********
    override suspend fun createNewUserAccount(email: String, password: String) {
        firebaseAuthentication.createNewUserAccount(email, password)
    }

    override suspend fun sendEmailVerificationRegister() {
        firebaseAuthentication.sendEmailVerificationRegister()
    }

    // ********** Firebase Auth Login **********
    override suspend fun singInWithEmailPassword(email: String, password: String) {
        firebaseAuthentication.singInWithEmailPassword(email, password)
    }

    override suspend fun checkEmailVerification() {
        firebaseAuthentication.checkEmailVerification()
    }

    override suspend fun sendEmailVerificationLogin() {
        firebaseAuthentication.sendEmailVerificationLogin()
    }

    // ********** Firebase Storage Save Data **********
    override suspend fun uploadPhotoToFirebaseStorage(uri: Uri) {
        firebaseStorageSaveDataRegister.uploadPhotoToFirebaseStorage(uri)
    }

    // ********** Firebase FireStore Save Data **********
    override suspend fun insertUserToFireStoreDB(username: String, email: String, uri: String?, imageId: String?) {
        firebaseFireStoreSaveData.insertUserToFireStoreDB(username, email, uri, imageId)
    }

    override suspend fun updateUserInFireStoreDB(user: User) {
        firebaseFireStoreSaveData.updateUserInFireStoreDB(user)
    }

    // ********** Firebase FireStore Retrieve **********
    override suspend fun getAllUser(): List<User>? {
        return firebaseRetrieveFromFireStore.getAllUser()
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseRetrieveFromFireStore.getCurrentUser()
    }

    override suspend fun searchOnUsers(name: String) {
        firebaseRetrieveFromFireStore.searchOnUsers(name)
    }

    override val searchUsersLiveData: LiveData<ArrayList<User?>>
        get() = firebaseRetrieveFromFireStore.searchOnUsersMutableLiveData

    // ********** Firebase RealTime Database **********
    override suspend fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User) {
        firebaseRealTimeDatabase.sendMessage(message, senderRoom, receiverRoom, userReceiver, userSender)
    }

    override suspend fun getMessages(senderRoom: String) {
        firebaseRealTimeDatabase.getMessages(senderRoom)
    }

    override val messagesLiveData: LiveData<ArrayList<Message?>?>
        get() = firebaseRealTimeDatabase.messagesListMutableLiveData

    override suspend fun getLatestUserAndMessages(senderId: String) {
        firebaseRealTimeDatabase.getLatestUserAndMessages(senderId)
    }

    override val latestUserAndMessageLiveData: LiveData<ArrayList<LatestUserMessage?>?>
        get() = firebaseRealTimeDatabase.latestUserAndMessagesListMutableLiveData

    override suspend fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        firebaseRealTimeDatabase.searchOnLatestUserAndMessages(senderId, name)
    }

    override val searchOnUsersLiveData: LiveData<ArrayList<LatestUserMessage?>?>
        get() = firebaseRealTimeDatabase.searchOnUsersMutableLiveData

    // ********** Firebase Storage Profile **********
    override suspend fun uploadPhotoToFirebaseStorageProfile(uri: Uri) {
        firebaseStorageProfile.uploadPhotoToFirebaseStorage(uri)
    }

    override suspend fun deleteImageFromFireStorageProfile(fileName: String) {
        firebaseStorageProfile.deleteImageFromFireStorage(fileName)
    }

}