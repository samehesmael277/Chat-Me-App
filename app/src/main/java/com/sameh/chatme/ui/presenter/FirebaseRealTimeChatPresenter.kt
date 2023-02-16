package com.sameh.chatme.ui.presenter

interface FirebaseRealTimeChatPresenter {

    fun ifSaveMessageToDatabaseSuccess(ifSuccess: Boolean, state: String)

}