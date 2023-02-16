package com.sameh.chatme.data.network

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.ui.presenter.FirebaseStorageProfilePresenter
import java.util.*
import javax.inject.Inject

class FirebaseStorageProfile @Inject constructor(
    private val firebaseStorageInstance: FirebaseStorage
) {

    var firebaseStorageProfilePresenter: FirebaseStorageProfilePresenter? = null

    fun uploadPhotoToFirebaseStorage(uri: Uri) {
        val fileName = UUID.randomUUID().toString()
        val myRef = firebaseStorageInstance.getReference("/images/$fileName")
        myRef.putFile(uri)
            .addOnSuccessListener {
                myRef.downloadUrl
                    .addOnSuccessListener {
                        firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                            true,
                            Constants.TAG,
                            it,
                            fileName
                        )
                    }
                    .addOnFailureListener {
                        firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                            false,
                            it.message!!,
                            null,
                            null
                        )
                    }
            }
            .addOnCanceledListener {
                firebaseStorageProfilePresenter?.isUploadPhotoSuccessful(
                    false,
                    Constants.FAILED_MESSAGE,
                    null,
                    null
                )
            }
    }

    fun deleteImageFromFireStorage(fileName: String) {
        val myRef = firebaseStorageInstance.getReference("/images/$fileName")
        myRef.delete()
            .addOnSuccessListener {
                Log.d(Constants.TAG, "deleteImageFromFireStorage: ${Constants.SUCCESS_MESSAGE}")
            }
            .addOnFailureListener {
                Log.d(Constants.TAG, "deleteImageFromFireStorage: ${it.message}")
            }
    }

}