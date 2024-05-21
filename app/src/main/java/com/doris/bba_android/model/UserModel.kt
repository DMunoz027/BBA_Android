package com.doris.bba_android.model

data class UserModel(
    var userId: String? = null,
    var userName: String? = "",
    var userEmail: String? = "",
    var userRole: String? = "default",
    var userVerified: Boolean? = false,
    var userPhone: String? = "",
    var userAddress: String? = "",
    var userDanger: String? = "",
    var userDateBirth: String? = "",
    var userBloodType: String? = "",
    var userPhoto: String? = "",
    var userToken: String? = "",
)
