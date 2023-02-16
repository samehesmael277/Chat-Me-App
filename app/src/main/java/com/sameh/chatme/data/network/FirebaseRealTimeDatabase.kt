package com.sameh.chatme.data.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.LatestUserMessage
import com.sameh.chatme.data.model.Message
import com.sameh.chatme.data.model.User
import com.sameh.chatme.ui.presenter.FirebaseRealTimeChatPresenter
import com.sameh.chatme.utils.getCurrentDate
import javax.inject.Inject

class FirebaseRealTimeDatabase @Inject constructor(
    private val myRef: DatabaseReference
) {

    var firebaseRealTimeChatPresenter: FirebaseRealTimeChatPresenter? = null

    var messagesListMutableLiveData: MutableLiveData<ArrayList<Message?>?> =
        MutableLiveData<ArrayList<Message?>?>()

    var latestUserAndMessagesListMutableLiveData: MutableLiveData<ArrayList<LatestUserMessage?>?> =
        MutableLiveData<ArrayList<LatestUserMessage?>?>()

    var searchOnUsersMutableLiveData: MutableLiveData<ArrayList<LatestUserMessage?>?> =
        MutableLiveData<ArrayList<LatestUserMessage?>?>()

    fun sendMessage(
        message: String,
        senderRoom: String,
        receiverRoom: String,
        userReceiver: User,
        userSender: User
    ) {
        val theMessage = Message(message, userSender.id, userReceiver.id, getCurrentDate())
        val latestUserMessageForSender = LatestUserMessage(theMessage, userReceiver)
        val latestUserMessageForReceiver = LatestUserMessage(theMessage, userSender)
        myRef.child("chats").child(senderRoom).child("messages").push()
            .setValue(theMessage).addOnSuccessListener {
                myRef.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(theMessage).addOnSuccessListener {

                        myRef.child("latestUsersAndMessages").child(userSender.id)
                            .child(userReceiver.id)
                            .setValue(latestUserMessageForSender)
                            .addOnSuccessListener {
                                myRef.child("latestUsersAndMessages").child(userReceiver.id)
                                    .child(userSender.id)
                                    .setValue(latestUserMessageForReceiver)
                                    .addOnSuccessListener {
                                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                            true,
                                            Constants.SUCCESS_MESSAGE
                                        )
                                    }
                                    .addOnFailureListener {
                                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                            false,
                                            it.message!!
                                        )
                                    }
                            }
                            .addOnFailureListener {
                                firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                                    false,
                                    it.message!!
                                )
                            }
                    }
                    .addOnFailureListener {
                        firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                            false,
                            it.message!!
                        )
                    }
            }
            .addOnFailureListener {
                firebaseRealTimeChatPresenter?.ifSaveMessageToDatabaseSuccess(
                    false,
                    it.message!!
                )
            }
    }

    fun getMessages(senderRoom: String) {
        val messages = ArrayList<Message?>()
        myRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        messages.clear()
                        snapshot.children.forEach {
                            val message = it.getValue(Message::class.java)
                            messages.add(message)
                        }
                        messagesListMutableLiveData.postValue(messages)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

    fun getLatestUserAndMessages(senderId: String) {
        val latestUserAndMessageList = ArrayList<LatestUserMessage?>()
        myRef.child("latestUsersAndMessages").child(senderId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        latestUserAndMessageList.clear()
                        snapshot.children.forEach {
                            val latestUserMessage = it.getValue(LatestUserMessage::class.java)
                            latestUserAndMessageList.add(latestUserMessage)
                        }
                        latestUserAndMessagesListMutableLiveData.postValue(latestUserAndMessageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

    fun searchOnLatestUserAndMessages(senderId: String, name: String) {
        val latestUserAndMessageList = ArrayList<LatestUserMessage?>()
        myRef.child("latestUsersAndMessages").child(senderId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        latestUserAndMessageList.clear()
                        snapshot.children.forEach {
                            val latestUserMessage = it.getValue(LatestUserMessage::class.java)
                            if (name.lowercase() in latestUserMessage!!.user.username.lowercase()) {
                                latestUserAndMessageList.add(latestUserMessage)
                            }
                        }
                        searchOnUsersMutableLiveData.postValue(latestUserAndMessageList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(Constants.TAG, "onCancelled: ${error.message}")
                }
            })
    }

}