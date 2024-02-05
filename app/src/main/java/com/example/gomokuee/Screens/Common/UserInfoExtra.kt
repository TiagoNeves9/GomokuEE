package com.example.gomokuee.Screens.Common

import android.content.Intent
import android.os.Parcelable
import com.example.gomokuee.Domain.UserInfo
import kotlinx.parcelize.Parcelize


const val USER_INFO_EXTRA = "UserInfo"
@Parcelize
data class UserInfoExtra(val id: String, val username: String, val token: String) : Parcelable {
    constructor(userInfo: UserInfo) : this(userInfo.id, userInfo.username, userInfo.token)
}

/**
 * Converts this [UserInfoExtra] to a [UserInfo].
 */
fun UserInfoExtra.toUserInfo() = UserInfo(id, username, token)

@Suppress("DEPRECATION")
fun getUserInfoExtra(intent: Intent): UserInfoExtra? =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
        intent.getParcelableExtra(USER_INFO_EXTRA, UserInfoExtra::class.java)
    else
        intent.getParcelableExtra(USER_INFO_EXTRA)