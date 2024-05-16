package com.doris.bba_android.model

data class UserModel(
    var userId: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userRole: String? = "default",
    var userVerified: Boolean? = false,
    var userPhone: String? = null,
    var userAddress: String? = null,
    var userDanger: String? = null,
    var userDateBirth: String? = null,
    var userBloodType: String? = null,
    var userToken: String? = null,
)
