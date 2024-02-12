package com.example.gomokuee.Domain

data class UserInfo(val id: String, val username: String, val token: String) {
    init {
        require(validateUserInfoParts(id, username, token))
    }
}

fun validateUserInfoParts(id: String, username: String, token: String): Boolean =
    id.isNotBlank() && username.isNotBlank() && token.isNotBlank()