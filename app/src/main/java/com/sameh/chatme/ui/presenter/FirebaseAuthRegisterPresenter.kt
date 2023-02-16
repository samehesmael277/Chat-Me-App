package com.sameh.chatme.ui.presenter

interface FirebaseAuthRegisterPresenter {

    fun ifCreateNewUserAccountSuccess(ifSuccess: Boolean, state: String)

    fun ifSendEmailVerificationSuccessRegister(ifSuccess: Boolean, state: String)

}