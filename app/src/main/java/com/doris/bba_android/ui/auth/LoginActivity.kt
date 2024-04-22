package com.doris.bba_android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.R
import com.doris.bba_android.databinding.ActivityLoginBinding
import com.doris.bba_android.model.request.LoginRequest
import com.doris.bba_android.model.response.UserResponse
import com.doris.bba_android.network.auth.AuthFirebaseManager
import com.doris.bba_android.ui.common.DialogUi


class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding
    private lateinit var _dialog: DialogUi
    private lateinit var authManager: AuthFirebaseManager

    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(_binding.root)
        initUI()
    }

    private fun initUI() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSingIn.setOnClickListener { initLogin() }
        binding.btnSingUp.setOnClickListener { jumpNextActivity(RegisterActivity()) }
        binding.btnForgotPassword.setOnClickListener { jumpNextActivity(ForgotPasswordActivity()) }
        binding.btnBack.setOnClickListener { jumpNextActivity(DecideActionAuthActivity()) }
    }

    /**
     * Init login
     * Inicializa el evento de iniciar sesion, donde se autentican las credenciales
     * ingresadas por el usuario mediante firebaseAuth
     */
    private fun initLogin() {
        authManager = AuthFirebaseManager()
        val loginRequest = LoginRequest("doris@gmail.com", "12345678")
        setupAlert(1)
        authManager
            .loginWithEmailAndPassword(loginRequest) { user ->
                _dialog.cancel()
                if (user != null)
                    setupAlert(2)
                else
                    setupAlert(3)
            }
    }

    private fun jumpNextActivity(activity: Activity, finish: Boolean = false) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        if (finish)
            finish()
    }

    /**
     * Setup alert
     * Esta funcion sirve para configuarar las posibles alertas que se mostraran al usuario
     * puede ser una alñerta de exito, error, o procesando
     * se hace un when para decirdir que alerta mostrar segun el caso especifico:
     * 1:La solicitud esta siendo procesada
     * 2:La solicitud se proceso con exito
     * 3:La solicitud se proceso con errores
     *
     * @param case: Es el parametro que se va a evaluar (1,2,3...)
     */
    private fun setupAlert(case: Int) {
        when (case) {
            1 -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_loading,
                    R.string.loading_hint,
                    R.string.message_process_information
                ) { _dialog.dismiss() }
                _dialog.show()
            }

            2 -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_success,
                    R.string.success_hint,
                    R.string.message_success,
                    2
                ) { _dialog.cancel() }
                _dialog.show()

            }

            3 -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_error,
                    R.string.error_hint,
                    R.string.message_error,
                    3
                ) {
                    _dialog.cancel()
                    initLogin()
                }
                _dialog.show()
            }

        }
    }

}