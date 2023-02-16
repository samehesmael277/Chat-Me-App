package com.sameh.chatme.data.model

data class LatestUserMessage(
    var message: Message = Message("", "", "", ""),
    var user: User = User("", "", "", null, null)
)