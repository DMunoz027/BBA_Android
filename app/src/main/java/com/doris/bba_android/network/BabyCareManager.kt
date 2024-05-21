package com.doris.bba_android.network

import com.doris.bba_android.model.BabyCareModel
import com.google.firebase.firestore.FirebaseFirestore

class BabyCareManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val nameColl = "baby_care_coll"


    fun saveBabyCareData(
        babyCare: BabyCareModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(nameColl)
            .add(babyCare)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun getBabyCareData(onSuccess: (List<BabyCareModel>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(nameColl)
            .get()
            .addOnSuccessListener { result ->
                val babyCareList = result.mapNotNull { it.toObject(BabyCareModel::class.java) }
                onSuccess(babyCareList)
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

}