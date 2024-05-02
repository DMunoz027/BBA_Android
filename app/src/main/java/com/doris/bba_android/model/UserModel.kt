package com.doris.bba_android.model

data class UserModel(
    var userId: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userRole: String? = "default",
    var userVerified: Boolean? = null,
    var userPhone: String? = null,
    var userAddress: String? = null,
    var userToken: String? = null,
)
