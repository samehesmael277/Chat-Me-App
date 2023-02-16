package com.sameh.chatme.data.network

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.ui.presenter.FirebaseStorageRegisterPresenter
import java.util.*
import javax.inject.Inject

class FirebaseStorageSaveDataRegister @Inject constructor(
    private val firebaseStorageInstance: FirebaseStorage
) {

    var firebaseStorageRegisterPresenter: FirebaseStorageRegisterPresenter? = null

    fun uploadPhotoToFirebaseStorage(uri: Uri) {
        val fileName = UUID.randomUUID().toString()
        val myRef = firebaseStorageInstance.getReference("/images/$fileName")
        myRef.putFile(uri)
            .addOnSuccessListener {
                myRef.downloadUrl
                    .addOnSuccessListener {
                        firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                            true,
                            Constants.SUCCESS_MESSAGE,
                            it,
                            fileName
                        )
                    }
                    .addOnFailureListener {
                        firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                            false,
                            it.message!!,
                            null,
                            null
                        )
                    }
            }
            .addOnCanceledListener {
                firebaseStorageRegisterPresenter!!.ifImageUploadedSuccess(
                    false,
                    Constants.FAILED_MESSAGE,
                    null,
                    null
                )
            }
    }

}