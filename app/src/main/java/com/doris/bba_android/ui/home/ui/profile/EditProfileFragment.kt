package com.doris.bba_android.ui.home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.doris.bba_android.R
import com.doris.bba_android.databinding.FragmentEditProfileBinding
import com.doris.bba_android.databinding.FragmentProfileBinding
import com.doris.bba_android.model.UserModel
import com.doris.bba_android.network.users.UserManager
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.ui.home.HomeUserActivity
import com.doris.bba_android.utils.Constants
import com.doris.bba_android.utils.SharedPreferencesManager


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var _dialog: DialogUi? = null
    private var selectedBloodType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionButtons()
        initialGetProfileData()
    }


    private fun actionButtons() {
        binding.btnEditMyData.setOnClickListener { updateMyData() }
    }

    private fun updateMyData() {
        if (formIsValid()) {
            setupAlert(Constants.STATUS_LOADING) {}
            val userManager = UserManager()
            val prefs = SharedPreferencesManager(requireContext())
            val idUser = prefs.getPref("idUser", "empty").toString()
            val userUpdated = UserModel(
                userName = binding.txtName.text.toString(),
                userDateBirth = binding.txtBirthDate.text.toString(),
                userBloodType = selectedBloodType,
                userAddress = binding.txtAddress.text.toString(),
                userPhone = binding.txtPhone.text.toString(),
            )
            userManager.updateUserColl(idUser, userUpdated) {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        "Información actualizada con exito",
                        Toast.LENGTH_SHORT
                    ).show()
                    _dialog?.dismissAlert()
                    val intent = Intent(requireContext(), HomeUserActivity::class.java)
                    startActivity(intent)
                } else {
                    setupAlert(Constants.STATUS_ERROR) {
                        _dialog?.dismissAlert()
                    }
                }
            }
        }
    }

    private fun formIsValid(): Boolean {
        var response: Boolean

        if (binding.txtName.text.isNullOrEmpty()) {
            binding.txtName.error = "Nombre es obligatorio"
            response = false
        } else {
            binding.txtName.error = null
            response = true
        }

        if (binding.txtBirthDate.text.isNullOrEmpty()) {
            binding.txtBirthDate.error = "Fecha de nacimiento es obligatorio"
            response = false
        } else {
            binding.txtBirthDate.error = null
            response = true
        }

        if (binding.txtAddress.text.isNullOrEmpty()) {
            binding.txtAddress.error = "El peso al nacer es obligatorio"
            response = false
        } else {
            binding.txtAddress.error = null
            response = true
        }

        if (binding.txtPhone.text.isNullOrEmpty()) {
            binding.txtPhone.error = "El peso actual es obligatorio"
            response = false
        } else {
            binding.txtPhone.error = null
            response = true
        }


        return response
    }

    private fun setupBloodTypesSpinner() {
        val bloodType = resources.getStringArray(R.array.blood_types)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bloodType)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinBloodType.adapter = adapter

        binding.spinBloodType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedBloodType = bloodType[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initialGetProfileData() {
        try {
            setupAlert(Constants.STATUS_LOADING) {}
            val userManager = UserManager()
            val prefs = SharedPreferencesManager(requireContext())
            val idUser = prefs.getPref("idUser", "empty").toString()
            userManager.getOneUserColl(idUser) {
                _dialog?.dismissAlert()
                if (it != null) {
                    initialPasteInfoForm(it)
                    setupBloodTypesSpinner()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "initialGetProfileData: ${e.message}")
            Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_SHORT).show()
            setupAlert(Constants.STATUS_ERROR) {}
        }
    }

    private fun initialPasteInfoForm(user: UserModel) {
        try {
            Glide.with(binding.imgSelected).load(user.userPhoto).into(binding.imgSelected)
            binding.txtName.setText(user.userName)
            binding.txtBirthDate.setText(user.userDateBirth)
            binding.txtAddress.setText(user.userAddress)
            binding.txtPhone.setText(user.userPhone)
        } catch (e: Exception) {
            Log.e("TAG", "initialGetProfileData: ${e.message}")
            setupAlert(Constants.STATUS_ERROR) {}
            Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAlert(case: Int, action: () -> Unit) {
        if (_dialog == null) {
            _dialog = DialogUi(requireContext())
        }

        when (case) {
            Constants.STATUS_LOADING -> {
                _dialog?.update(
                    R.raw.anim_loading,
                    R.string.loading_hint,
                    R.string.message_process_information,
                    actionCode = 1
                )
            }

            Constants.STATUS_SUCCESS -> {
                _dialog?.update(
                    R.raw.anim_success,
                    R.string.success_hint,
                    R.string.message_success,
                    actionCode = 1
                )
            }

            Constants.STATUS_ERROR -> {
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