package com.sameh.chatme.data.network

import com.google.firebase.auth.FirebaseAuth
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.ui.presenter.FirebaseAuthLoginPresenter
import com.sameh.chatme.ui.presenter.FirebaseAuthRegisterPresenter
import javax.inject.Inject

class FirebaseAuthentication @Inject constructor(
    private val auth: FirebaseAuth
) {

    // Presenters
    var firebaseAuthRegisterPresenter: FirebaseAuthRegisterPresenter? = null
    var firebaseAuthLoginPresenter: FirebaseAuthLoginPresenter? = null

    // ********** Auth Register **********
    fun createNewUserAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthRegisterPresenter!!.ifCreateNewUserAccountSuccess(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthRegisterPresenter!!.ifCreateNewUserAccountSuccess(
                        false,
                        it.exception!!.message!!
                    )
                }
            }
    }

    fun sendEmailVerificationRegister() {
        val currentUser = auth.currentUser
        currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthRegisterPresenter!!.ifSendEmailVerificationSuccessRegister(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthRegisterPresenter!!.ifSendEmailVerificationSuccessRegister(
                        true,
                        it.exception!!.message!!
                    )
                }
            }
    }

    // ********** Auth Login **********
    fun singInWithEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthLoginPresenter!!.ifSingInWithEmailPasswordComplete(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthLoginPresenter!!.ifSingInWithEmailPasswordComplete(
                        false,
                        it.exception!!.message!!
                    )
                }
            }
    }

    fun checkEmailVerification() {
        val currentUser = auth.currentUser
        if (currentUser!!.isEmailVerified) {
            firebaseAuthLoginPresenter!!.ifEmailVerificationLogin(true)
        } else {
            firebaseAuthLoginPresenter!!.ifEmailVerificationLogin(false)
        }
    }

    fun sendEmailVerificationLogin() {
        val currentUser = auth.currentUser
        currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseAuthLoginPresenter!!.ifSendEmailVerificationSentSuccessLogin(
                        true,
                        Constants.SUCCESS_MESSAGE
                    )
                } else {
                    firebaseAuthLoginPresenter!!.ifSendEmailVerificationSentSuccessLogin(
                        true,
                        it.exception!!.message!!
                    )
                }
            }
    }

}