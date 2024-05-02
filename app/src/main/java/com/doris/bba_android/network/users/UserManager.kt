package com.doris.bba_android.network.users

import com.doris.bba_android.model.UserModel
import com.doris.bba_android.utils.Constants.Companion.STATUS_LOADING
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val nameColl = "users_system"

    /**
     * Save user coll
     * Esta funcion sirve para alamacenar la información basica de un usuario, por defecto se le agrega el rol
     * default, por que se crea un usuario normal
     * @param data: es la informacion que se va a alamacenar
     * @param resultResponse: es la respuesta para saber si se almaceno o no se almaceno el usuario
     * @receiver
     */
    fun saveUserColl(data: UserModel, resultResponse: (Boolean) -> Unit) {

        db.collection(nameColl)
            .add(data)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    resultResponse(true)
                } else {
                    resultResponse(false)
                }

            }.addOnFailureListener {
                resultResponse(false)
            }
    }

    suspend fun getAllUsersColl(): List<UserModel> {
        val listUserModels: MutableList<UserModel> = mutableListOf()


        val snapshot = db.collection(nameColl).get().await()
        for (doc in snapshot.documents) {
            val userModelData = doc.toObject(UserModel::class.java)
            if (userModelData != null) {
                listUserModels.add(userModelData)
            }
        }

        return listUserModels
    }

    /**
     * Get one user coll
     * Esta funcion sirve para obtener la infortmacin de un usuario en especifico
     * devuelve la informacion del usuario en caso de existir o un null en caso de no existir dicho usuario
     * @param userId: es el Id del usuario que se deseaa cosnultar
     * @param callback: es la informacion del usuario o null segun corresponda
     * @receiver
     */
    fun getOneUserColl(userId: String, callback: (UserModel?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val collRef = db.collection(nameColl)

        collRef.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val userModel = documentSnapshot.toObject(UserModel::class.java)
                    callback(userModel)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
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
