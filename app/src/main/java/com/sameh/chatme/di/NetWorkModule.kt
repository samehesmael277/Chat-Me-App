package com.sameh.chatme.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =
        Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseStorageInstance(): FirebaseStorage =
        FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore =
        Firebase.firestore

    @Singleton
    @Provides
    fun provideFirebaseFireStoreCollectionReference(): CollectionReference =
        Firebase.firestore.collection("users")

    @Singleton
    @Provides
    fun provideRealTimeDBReference(): DatabaseReference =
        FirebaseDatabase.getInstance().reference
}