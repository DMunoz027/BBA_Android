package com.doris.bba_android.network.users

import android.util.Log
import com.doris.bba_android.model.BabyModel
import com.google.firebase.firestore.FirebaseFirestore

class BabiesManager {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val nameColl = "babies_coll"

    fun saveBabyColl(baby: BabyModel, resultResponse: (Boolean) -> Unit) {
        try {
            db.collection(nameColl)
                .add(baby)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        resultResponse(true)
                    } else {
                        resultResponse(false)
                    }

                }.addOnFailureListener {
                    resultResponse(false)
                }
        } catch (e: Exception) {
            Log.e("error", "saveBabyColl: ${e.message}")
        }
    }

    fun getMyBaby(idParent: String, result: (List<BabyModel>) -> Unit) {
        try {
            db.collection(nameColl)
                .whereEqualTo("babyIdParent", idParent)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val babyList = mutableListOf<BabyModel>()
                        for (document in task.result!!) {
                            val baby = document.toObject(BabyModel::class.java)
                            babyList.add(baby)
                        }
                        result(babyList)
                    } else {
                        result(emptyList())
                    }
                }
        } catch (e: Exception) {
            Log.e("error", "getMyBaby: ${e.message}")
        }
    }
}