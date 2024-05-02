package com.doris.bba_android.network.auth

import com.doris.bba_android.model.request.LoginRequest
import com.doris.bba_android.model.request.RegisterRequest
import com.doris.bba_android.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

/**
 * Auth firebase manager
 * Esta clase sirve para manejar la autenticacion del usuario, donde va a tener las diferentes
 * funcionalidades, como login, registro, cerrar sesion
 * @constructor Create empty Auth firebase manager
 */
class AuthFirebaseManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Login with email and password
     * Esta funcion se encarga de realizar la autenticacion de un usuario, donde se recibe a travez de
     * una data class un usuario y una contraseña, la funcion retorna la informacion del usuario en caso de ir correcto
     * o si no, retorna una excepcion o un error
     * @param data: Es la dataClass que se crea en los modelos, en la carpeta de request (Solicitud)
     * @param completion: Es el resultado de
     * @return
     */
    fun loginWithEmailAndPassword(data: LoginRequest, completion: (UserModel?) -> Unit) {
        auth.signInWithEmailAndPassword(data.user, data.password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    val userModelResponse = UserModel(
                        userId = user?.uid ?: "",
                        userName = user?.displayName ?: "",
                        userEmail = user?.email ?: "",
                        userVerified = user?.isEmailVerified ?: false,
                        userToken = user?.getIdToken(false)?.result?.token ?: ""
                    )
                    completion(userModelResponse)
                } else {
                    completion(null)
                }
            }
    }

    /**
     * Register with email and password
     * Esta funcion sirve para ralizar el registro de un usuario en la aplición,
     * recibe por parametro el correo y la contraseña, esta funcion retorna la informacion del usuario
     * si no, retorna un error o una excepcion
     * @param data: Es la dataClass que se crea en los modelos, en la carpeta de request (Solicitud)
     * @return
     */
    fun registerWithEmailAndPassword(data: RegisterRequest, completion: (UserModel?) -> Unit) {
        auth.createUserWithEmailAndPassword(data.userEmail, data.userPassword)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val user = task.result!!.user
                    val userModelResponse = UserModel(
                        userId = user?.uid ?: "",
                        userName = user?.displayName ?: "",
                        userEmail = user?.email ?: "",
                        userVerified = user?.isEmailVerified ?: false,
                        userPhone = user?.phoneNumber ?: "",
                        userAddress = "Null",
                        userToken = user?.getIdToken(false)?.result?.token ?: ""
                    )
                    completion(userModelResponse)
                } else {
                    completion(null)
                }
            }
    }

    /**
     * Logout session
     * Sirve para cerra la sesion del usaurio actual
     */
    fun logoutSession() = auth.signOut()

}