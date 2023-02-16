package com.sameh.chatme.data.repo

import android.net.Uri

interface FirebaseStorageSaveDataRepo {

    suspend fun uploadPhotoToFirebaseStorage(uri: Uri)
    
}