package com.sameh.chatme.data.repo

interface FirebaseAuthRepo {

    // ********** Auth Register **********
    suspend fun createNewUserAccount(email: String, password: String)

    suspend fun sendEmailVerificationRegister()

    // ********** Auth Login **********
    suspend fun singInWithEmailPassword(email: String, password: String)

    suspend fun checkEmailVerification()

    suspend fun sendEmailVerificationLogin()

}