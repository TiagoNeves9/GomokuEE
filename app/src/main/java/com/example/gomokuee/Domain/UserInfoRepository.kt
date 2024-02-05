package com.example.gomokuee.Domain

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfo?

    suspend fun updateUserInfo(userInfo: UserInfo)

    suspend fun clearUserInfo()
}
