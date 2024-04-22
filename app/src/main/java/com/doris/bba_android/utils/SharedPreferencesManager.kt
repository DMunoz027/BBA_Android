package com.doris.bba_android.utils

import android.content.Context
import android.content.SharedPreferences
import java.lang.IllegalArgumentException

/**
 * Shared preferences manager
 *
 * Esta clase sirve para manejar las preferencias del usuario, permite
 * crear, obtener, eliminar y limpiar las preferencias del usuario,
 * haciendo uso de SharedPreferences
 *
 * @property context
 * @constructor Create empty Shared preferences manager
 */
class SharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("BBA_Prefs", Context.MODE_PRIVATE)
    }

    /**
     * Save pref
     * Almacena una preferencia en la base de datos de la aplicacion, puede almacenar diferentes tipos
     * de datos, (String, Int, Boolean, Float, Long)
     * @param key: Es la llave con la que se va a guardar en la base de datos para luego poder reciperar dicho dato
     * @param value: Es el valor literal que se va a almacenar
     */
    fun savePref(key: String, value: Any) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw IllegalArgumentException("Ocurrio un error guardando la preferencia")
        }
        editor.apply()
    }

    /**
     * Get pref
     * Obtiene una preferencia en especifico mediante la llave que se haya guardado en la aplicacion
     * @param key: Es la llave mediante la cual se desea buscar un dato guardado para obtenerlo
     * @param value: Es el valor por defecto que se devolvera en caso de que no exista,
     * tambien sirve para poder identificar que tipo de dato es el que se desea recuperar
     */
    fun getPref(key: String, value: Any) {
        when (value) {
            is String -> sharedPreferences.getString(key, value)
            is Int -> sharedPreferences.getInt(key, value)
            is Boolean -> sharedPreferences.getBoolean(key, value)
            is Float -> sharedPreferences.getFloat(key, value)
            is Long -> sharedPreferences.getLong(key, value)
            else -> throw IllegalArgumentException("Ocurrio un error obteniendo la preferencia")
        }
    }

    /**
     * Remove pref
     * Elimina mediante la llave una preferencia que haya sido guardada con anterioridad en la aplicacion
     * @param key: Es la llave que conrtiene el valor que se desea borrar
     */
    fun removePref(key: String) {
        sharedPreferences
            .edit()
            .remove(key)
            .apply()
    }

    /**
     * Clean prefs
     * Limpia todas las preferencias dejando ningun dato guardado en la aplicacion
     */
    fun cleanPrefs() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }
}