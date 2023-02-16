package com.sameh.chatme.data.model

data class Message(
    var message: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var messageTimeDate: String = ""
)