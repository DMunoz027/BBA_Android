package com.doris.bba_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.ui.auth.DecideActionAuthActivity
import com.doris.bba_android.ui.home.HomeUserActivity
import com.doris.bba_android.utils.SharedPreferencesManager


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var _storeManager: SharedPreferencesManager
    private val storeManager get() = _storeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        _storeManager = SharedPreferencesManager(this)
        initUi()

    }

    private fun initUi() {
        handleSplash()
    }

    /**
     * Handle splash
     * Muestra la vista Splash por un determinado tiempo
     * Haciendo uso de un Lopper que dura 3.5 segundos(3500 milisegundos)
     * Se valida la sesion del usuario para evitar volver al login y ademas de eso
     * se valida el rol del usaurio que ha iniciado sesion
     */
    private fun handleSplash() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val sessionActive = storeManager.getPref("sessionState", false) as Boolean
                if (sessionActive) {
                    val roleUser = storeManager.getPref("roleUser", "").toString()
                    if (roleUser == "default") {
                        jumpNextActivity(activity = HomeUserActivity())
                    } else {
                        jumpNextActivity(activity = DecideActionAuthActivity())
                    }
                } else {
                    jumpNextActivity(activity = DecideActionAuthActivity())
                }
            }, 3500)
        }
    }


    /**
     * Jump next activity
     * Ir a una nueva pantalla (Nueva Actividad)
     * @param activity: Es la nueva vista, debe ser una actividad ejecutada,
     * (Nombre de la actividad a la que queremos ir seguido de () )
     * @param finish: Indica si se debe n¿finalizar la actividad anterior o no,
     * por defecto no finaliza la actividad
     */
    private fun jumpNextActivity(activity: Activity, finish: Boolean = false) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        if (finish)
            finish()
    }

}