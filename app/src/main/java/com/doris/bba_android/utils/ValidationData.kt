package com.doris.bba_android.utils

class ValidationData {

    fun validate(value: Any): HashMap<String, String> {
        val responseMap = HashMap<String, String>()
        when (value) {
            is String -> {
                if (value.isBlank()) {
                    responseMap["valueEmpty"] = "El campo esta vacion"
                }

            }
        }
        return responseMap
    }
    
}