package com.doris.bba_android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.R
import com.doris.bba_android.databinding.ActivityLoginBinding
import com.doris.bba_android.model.request.LoginRequest
import com.doris.bba_android.network.auth.AuthFirebaseManager
import com.doris.bba_android.network.users.UserManager
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.ui.home.HomeUserActivity
import com.doris.bba_android.utils.Constants.Companion.STATUS_ERROR
import com.doris.bba_android.utils.Constants.Companion.STATUS_LOADING
import com.doris.bba_android.utils.Constants.Companion.STATUS_SUCCESS
import com.doris.bba_android.utils.SharedPreferencesManager


class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding
    private var _dialog: DialogUi? = null
    private val binding get() = _binding
    private lateinit var authManager: AuthFirebaseManager
    private lateinit var _storePrefs: SharedPreferencesManager
    private val storePrefs get() = _storePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        _storePrefs = SharedPreferencesManager(this)
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
        val email = binding.txtEmailUser.text.toString()
        val password = binding.txtPassUser.text.toString()
        val loginRequest = LoginRequest(email, password)
        setupAlert(STATUS_LOADING) {}
        authManager
            .loginWithEmailAndPassword(loginRequest) { user ->
                if (user?.userId != null) {
                    val userManager = UserManager()
                    userManager.getOneUserColl(user.userId!!) {

                        storePrefs.savePref("sessionState", true)
                        it?.userRole?.let { role ->
                            storePrefs.savePref("roleUser", role)
                        }
                        it?.userId?.let { id -> storePrefs.savePref("idUser", id) }

                        setupAlert(STATUS_SUCCESS) {
                            jumpNextActivity(HomeUserActivity())
                        }
                    }
                } else {
                    setupAlert(STATUS_ERROR) {
                        _dialog?.dismissAlert()
                    }
                }
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
    private fun setupAlert(case: Int, action: () -> Unit) {
        if (_dialog == null) {
            _dialog = DialogUi(this)
        }

        when (case) {
            STATUS_LOADING -> {
                _dialog?.update(
                    R.raw.anim_loading,
                    R.string.loading_hint,
                    R.string.message_process_information,
                    actionCode = 1
                )
            }

            STATUS_SUCCESS -> {
                _dialog?.update(
                    R.raw.anim_success,
                    R.string.success_hint,
                    R.string.message_success,
                    actionCode = 1
                )
            }

            STATUS_ERROR -> {
                _dialog?.update(
                    R.raw.anim_error,
                    R.string.error_hint,
                    R.string.message_error,
                    actionCode = 1
                )
            }
        }
        _dialog?.show(action)
    }

}