package com.doris.bba_android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.databinding.ActivityDecideActionAuthBinding

class DecideActionAuthActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityDecideActionAuthBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDecideActionAuthBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initUI()
    }

    private fun initUI() {
        setupClickListeners()
    }


    /**
     * Setup click listeners
     * Configura los clicks o eventos de la pantalla,
     * Agrupa todos los eventos click en una sola funcion
     * Cada click configurado aqui realiza una accion propia
     */
    private fun setupClickListeners() {
        binding.btnAlreadyAccount.setOnClickListener { jumpNextActivity(LoginActivity()) }
        binding.btnNotAccountYet.setOnClickListener { jumpNextActivity(RegisterActivity()) }
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