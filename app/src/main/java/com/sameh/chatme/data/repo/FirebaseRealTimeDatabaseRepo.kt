package com.sameh.chatme.data.repo

import androidx.lifecycle.LiveData
import com.sameh.chatme.data.model.LatestUserMessage
import com.sameh.chatme.data.model.Message
import com.sameh.chatme.data.model.User

interface FirebaseRealTimeDatabaseRepo {

    suspend fun sendMessage(message: String, senderRoom: String, receiverRoom: String, userReceiver: User, userSender: User)

    suspend fun getMessages(senderRoom: String)

    val messagesLiveData: LiveData<ArrayList<Message?>?>

    suspend fun getLatestUserAndMessages(senderId: String)

    val latestUserAndMessageLiveData: LiveData<ArrayList<LatestUserMessage?>?>

    suspend fun searchOnLatestUserAndMessages(senderId: String, name: String)

    val searchOnUsersLiveData: LiveData<ArrayList<LatestUserMessage?>?>

}