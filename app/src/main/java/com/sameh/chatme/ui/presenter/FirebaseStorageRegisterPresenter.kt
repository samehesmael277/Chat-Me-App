package com.sameh.chatme.ui.presenter

import android.net.Uri

interface FirebaseStorageRegisterPresenter {

    fun ifImageUploadedSuccess(ifSuccess: Boolean, state: String, uri: Uri?, imageId: String?)

}