package com.sameh.chatme.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var profileImgUrl: String? = null,
    var profileImageId: String? = null
): Parcelable