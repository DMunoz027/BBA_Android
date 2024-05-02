package com.doris.bba_android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.R
import com.doris.bba_android.databinding.ActivityRegisterBinding
import com.doris.bba_android.model.request.RegisterRequest
import com.doris.bba_android.network.auth.AuthFirebaseManager
import com.doris.bba_android.network.users.UserManager
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.utils.Constants


class RegisterActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityRegisterBinding
    private val binding get() = _binding
    private lateinit var _dialog: DialogUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnSingUp.setOnClickListener { initRegister() }
        binding.btnAlreadyAccount.setOnClickListener { jumpNextActivity(LoginActivity(), true) }
    }

    /**
     * Validate form
     * Esta funcion sirve para validar el formulario de registro para que no sea nulo ningun dato
     * Tambien sirve para validar si las contraseñas coinciden
     * Ademas valida que el correo sea valido y la contraseña tenga un minimo de 8 cacateres
     * @return
     */
    private fun validateForm(): Boolean {
        var resultState = false
        if (binding.txtNameUser.text.isNullOrEmpty()) {
            binding.txtNameUser.error = "El nombre requerido"
            resultState = false
        } else {
            binding.txtNameUser.error = null
            resultState = true
        }

        if (binding.txtEmailUser.text.isNullOrEmpty()) {
            binding.txtEmailUser.error = "El correo requerido"
            resultState = false
        } else {
            binding.txtEmailUser.error = null
            resultState = true
            if (isValidEmail(binding.txtEmailUser.text.toString())) {
                resultState = true
            } else {
                resultState = false
                binding.txtEmailUser.error = "Ingrese un correo valido"
            }
        }

        if (binding.txtPassUser.text!!.isBlank()) {
            binding.txtPassUser.error = "La contraseña es requerida"
            resultState = false
        } else {
            binding.txtPassUser.error = null
            resultState = true

            if (binding.txtPassUser.text.toString().length < 8) {
                resultState = false
                binding.txtPassUser.error = "La contraseña debe contener al menos 8 caracteres"
            } else {
                if (binding.txtPassUser.text.toString() != binding.txtConfirmPassUser.text.toString()) {
                    binding.txtPassUser.error = "Las contraseñas no coinciden"
                    binding.txtConfirmPassUser.error = "Las contraseñas no coinciden"
                    resultState = false
                } else {
                    binding.txtPassUser.error = null
                    binding.txtConfirmPassUser.error = null
                    resultState = true
                }
            }
        }

        return resultState
    }

    /**
     * Is valid email:
     * Esta función sirve para validar el que un correo sea valido haciendo uso de una expresion regular
     * para validar la estructura de un correo
     * @param email: es el correo que se pasa por parametro a la funcion para que sea validado
     * @return
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return emailRegex.matches(email)
    }


    /**
     * Iniciar registro
     * Iniciamos el registro de usuario solo si el formulario ha sido valido
     * Si es valido iniciamos con las interacciones del usuario
     */
    private fun initRegister() {
        if (validateForm()) {
            try {
                val authManger = AuthFirebaseManager()
                val data = RegisterRequest(
                    userEmail = binding.txtEmailUser.text.toString(),
                    userPassword = binding.txtPassUser.text.toString(),
                )
                showAlertState(Constants.STATUS_LOADING)

                authManger.registerWithEmailAndPassword(data) { userResponse ->
                    if (userResponse != null) {
                        val userManager = UserManager()
                        userResponse.userName = binding.txtNameUser.text.toString()
                        userResponse.userRole = "default"
                        userManager.saveUserColl(userResponse) {
                            if (it) {
                                showAlertState(Constants.STATUS_SUCCESS)
                            } else {
                                showAlertState(Constants.STATUS_ERROR)
                            }
                        }

                    } else {
                        showAlertState(Constants.STATUS_ERROR)
                    }
                }

            } catch (error: Exception) {
                showAlertState(Constants.STATUS_ERROR)
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
    private fun showAlertState(case: Int) {
        when (case) {
            Constants.STATUS_LOADING -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_loading,
                    R.string.loading_hint,
                    R.string.message_process_information
                ) {}
                _dialog.show()
            }

            Constants.STATUS_SUCCESS -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_success,
                    R.string.success_hint,
                    R.string.message_success,
                    Constants.STATUS_SUCCESS
                ) {
                    jumpNextActivity(LoginActivity())
                    _dialog.cancel()
                    _dialog.dismiss()
                }
                _dialog.show()
            }

            Constants.STATUS_ERROR -> {
                _dialog = DialogUi(
                    this,
                    R.raw.anim_error,
                    R.string.error_hint,
                    R.string.message_error,
                    Constants.STATUS_ERROR
                ) {
                    _dialog.cancel()
                    _dialog.dismiss()
                }
                _dialog.show()
            }

        }
    }
}