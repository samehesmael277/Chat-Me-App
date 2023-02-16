package com.sameh.chatme.data.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.User
import com.sameh.chatme.ui.presenter.FirebaseRetrieveFromFireStoreNewMessagePresenter
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRetrieveFromFireStore @Inject constructor(
    private val myRef: CollectionReference,
    private val auth: FirebaseAuth
) {

    var firebaseRetrieveFromFireStoreNewMessagePresenter: FirebaseRetrieveFromFireStoreNewMessagePresenter? =
        null

    // live data
    var searchOnUsersMutableLiveData: MutableLiveData<ArrayList<User?>> = MutableLiveData<ArrayList<User?>>()

    // methods
    suspend fun getAllUser(): List<User>? {
        return try {
            firebaseRetrieveFromFireStoreNewMessagePresenter!!.ifRetrieveFromFirebaseSuccess(
                true,
                Constants.SUCCESS_MESSAGE
            )
            myRef.get().await().toObjects(User::class.java)
        } catch (e: Exception) {
            firebaseRetrieveFromFireStoreNewMessagePresenter!!.ifRetrieveFromFirebaseSuccess(
                false,
                e.message!!
            )
            null
        }
    }

    suspend fun getCurrentUser(): User? {
        val myRef = Firebase.firestore.collection("users")
        return try {
            myRef.document(auth.currentUser?.uid!!).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun searchOnUsers(name: String) {
        val usersList = ArrayList<User?>()
        myRef.get().addOnSuccessListener { result ->
            usersList.clear()
            result.forEach {
                val res = it.toObject(User::class.java)
                if (name.lowercase() in res.username.lowercase()) {
                    usersList.add(res)
                }
            }
            searchOnUsersMutableLiveData.postValue(usersList)
        }
            .addOnFailureListener {
                Log.d(Constants.TAG, "search: ${it.message}")
            }
    }

}