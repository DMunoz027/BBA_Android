package com.doris.bba_android.network.users

import com.doris.bba_android.model.request.RegisterRequest
import com.doris.bba_android.model.response.UserResponse
import com.doris.bba_android.utils.Constants.Companion.STATUS_ERROR
import com.doris.bba_android.utils.Constants.Companion.STATUS_ERROR_NETWORK
import com.doris.bba_android.utils.Constants.Companion.STATUS_LOADING
import com.doris.bba_android.utils.Constants.Companion.STATUS_SUCCESS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val nameColl = "users_system"

    fun saveUserColl(data: RegisterRequest): Int {
        var resultRes = STATUS_LOADING
        db.collection(nameColl)
            .add(data)
            .addOnCompleteListener { result ->
                resultRes = if (result.isSuccessful) {
                    STATUS_SUCCESS
                } else {
                    STATUS_ERROR
                }
            }.addOnFailureListener {
                resultRes = STATUS_ERROR_NETWORK
            }
        return resultRes
    }

    suspend fun getAllUsersColl(): List<UserResponse> {
        val listUsers: MutableList<UserResponse> = mutableListOf()

        try {
            val snapshot = db.collection(nameColl).get().await()
            for (doc in snapshot.documents) {
                val userData = doc.toObject(UserResponse::class.java)
                if (userData != null) {
                    listUsers.add(userData)
                }
            }
        } catch (e: Exception) {
            //
        }

        return listUsers
    }

    fun getOneUserColl() {

    }

    fun deleteOneUserColl(): Int {
        val resultRes = STATUS_LOADING
        return resultRes
    }

    fun editOneUserColl(): Int {
        val resultRes = STATUS_LOADING
        return resultRes
    }
}
