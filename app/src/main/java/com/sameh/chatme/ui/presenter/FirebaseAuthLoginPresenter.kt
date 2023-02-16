package com.sameh.chatme.ui.presenter

interface FirebaseAuthLoginPresenter {

    fun ifSingInWithEmailPasswordComplete(ifComplete: Boolean, state: String)

    fun ifEmailVerificationLogin(ifVerified: Boolean)

    fun ifSendEmailVerificationSentSuccessLogin(ifSuccess: Boolean, state: String)

}