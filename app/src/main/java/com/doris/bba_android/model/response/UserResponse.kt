package com.doris.bba_android.model.response

data class UserResponse(
    val userId: String? = null,
    val userName: String,
    val userEmail: String,
    val userVerified: Boolean,
    val userPhone: String,
    val userAddress: String,
    val userToken: String,
)
