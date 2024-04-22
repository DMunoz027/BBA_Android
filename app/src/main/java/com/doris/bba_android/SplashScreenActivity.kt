package com.doris.bba_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.ui.auth.DecideActionAuthActivity

/**
 * Splash screen activity
 *
 * @constructor Create empty Splash screen activity
 */
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        initUi()

    }

    private fun initUi() {
        handleSplash()
    }

    /**
     * Handle splash
     * Muestra la vista Splash por un determinado tiempo
     * Haciendo uso de un Lopper que dura 3.5 segundos(3500 milisegundos)
     */
    private fun handleSplash() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                jumpNextActivity(activity = DecideActionAuthActivity())
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