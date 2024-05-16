package com.doris.bba_android.ui.home

import FirebaseStorageHelper
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.doris.bba_android.R
import com.doris.bba_android.databinding.ActivityRegisterBabyBinding
import com.doris.bba_android.databinding.AlertMediaOrCameraBinding
import com.doris.bba_android.model.BabyModel
import com.doris.bba_android.network.users.BabiesManager
import com.doris.bba_android.ui.auth.LoginActivity
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.utils.Constants
import com.doris.bba_android.utils.SharedPreferencesManager
import java.util.UUID

class RegisterBabyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBabyBinding
    private var selectedImageUri: Uri? = null
    private var selectedImageBitmap: Bitmap? = null
    private var selectedGender: String? = null
    private var selectedBloodType: String? = null
    private lateinit var babyData: BabyModel
    private var _dialog: DialogUi? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                selectImageFromGallery()
            } else {
                Toast.makeText(
                    this,
                    "Permiso denegado para acceder a la galería",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    selectedImageUri = imageUri
                    binding.imgSelected.setImageURI(imageUri)
                }
            }
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    selectedImageBitmap = imageBitmap
                    binding.imgSelected.setImageBitmap(imageBitmap)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBabyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionButtons()
        setupGenderSpinner()
        setupBloodTypesSpinner()
    }

    private fun actionButtons() {
        binding.btnUploadImage.setOnClickListener { initAlertOptionForTakePhoto() }
        binding.btnRegister.setOnClickListener { initRegisterBaby() }

    }

    private fun setupGenderSpinner() {
        val genders = resources.getStringArray(R.array.genders)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinGender.adapter = adapter

        binding.spinGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupBloodTypesSpinner() {
        val bloodType = resources.getStringArray(R.array.blood_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodType)
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

        if (binding.txtAge.text.isNullOrEmpty()) {
            binding.txtAge.error = "Edad es obligatorio"
            response = false
        } else {
            binding.txtAge.error = null
            response = true
        }
        if (binding.txtBirthWeight.text.isNullOrEmpty()) {
            binding.txtBirthWeight.error = "El peso al nacer es obligatorio"
            response = false
        } else {
            binding.txtBirthWeight.error = null
            response = true
        }

        if (binding.txtWeightCurrent.text.isNullOrEmpty()) {
            binding.txtWeightCurrent.error = "El peso actual es obligatorio"
            response = false
        } else {
            binding.txtWeightCurrent.error = null
            response = true
        }

        if (binding.txtBirthSize.text.isNullOrEmpty()) {
            binding.txtBirthSize.error = "La altura al nacer es obligatorio"
            response = false
        } else {
            binding.txtSizeCurrent.error = null
            response = true
        }

        if (binding.txtSizeCurrent.text.isNullOrEmpty()) {
            binding.txtSizeCurrent.error = "La altura actual es obligatorio"
            response = false
        } else {
            binding.txtSizeCurrent.error = null
            response = true
        }



        return response
    }

    private fun initRegisterBaby() {
        try {
            if (formIsValid()) {
                if (selectedImageUri != null) {
                    val storage = FirebaseStorageHelper()
                    showAlertState(Constants.STATUS_LOADING) {}
                    babyData = BabyModel(
                        babyName = binding.txtName.text.toString(),
                        babyDateBirth = binding.txtBirthDate.text.toString(),
                        babyAge = binding.txtAge.text.toString(),
                        babyBloodType = selectedBloodType,
                        babyGender = selectedGender,
                        babyBirthWeight = binding.txtBirthWeight.text.toString(),
                        babyWeightCurrent = binding.txtWeightCurrent.text.toString(),
                        babyBirthSize = binding.txtBirthSize.text.toString(),
                        babySizeCurrent = binding.txtSizeCurrent.text.toString(),
                    )
                    val prefs = SharedPreferencesManager(this)
                    val idParent = prefs.getPref("idUser", "emptyUser")
                    babyData.babyIdParent = idParent.toString()
                    storage.uploadUriImage(
                        selectedImageUri!!,
                        "babies",
                        "${babyData.babyName}_$idParent"
                    ) {
                        if (it != null) {
                            babyData.babyPhoto = it
                        } else {
                            babyData.babyPhoto = "null"
                        }
                        register(babyData)
                    }
                }
                if (selectedImageBitmap != null) {
                    val storage = FirebaseStorageHelper()
                    val prefs = SharedPreferencesManager(this)
                    val idParent = prefs.getPref("idUser", "emptyUser")
                    babyData.babyIdParent = idParent.toString()

                    storage.uploadBitmapImage(
                        selectedImageBitmap!!,
                        "babies",
                        "${babyData.babyName}_$idParent"
                    ) {
                        if (it != null) {
                            babyData.babyPhoto = it
                        } else {
                            babyData.babyPhoto = "null"
                        }
                        register(babyData)
                    }
                }

            }
        } catch (e: Exception) {
            Log.e("error", "saveBabyColl1: ${e.message}")
        }
    }

    private fun register(data: BabyModel) {
        try {
            val userManager = BabiesManager()
            userManager.saveBabyColl(data) { success ->
                if (success) {
                    showAlertState(Constants.STATUS_SUCCESS) {
                        if (_dialog != null)
                            _dialog?.dismissAlert()

                        jumpNextActivity(HomeUserActivity(), true)
                    }
                } else {
                    showAlertState(Constants.STATUS_ERROR) {
                        if (_dialog != null)
                            _dialog?.dismissAlert()
                    }

                }
                if (_dialog != null)
                    _dialog?.dismissAlert()
            }
        } catch (e: Exception) {
            if (_dialog != null)
                _dialog?.dismissAlert()
        }
    }

    private fun showAlertState(case: Int, action: () -> Unit) {
        if (_dialog == null) {
            _dialog = DialogUi(this)
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

    private fun jumpNextActivity(activity: Activity, finish: Boolean = false) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        if (finish)
            finish()
    }

    private fun initAlertOptionForTakePhoto() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val bindingAlert: AlertMediaOrCameraBinding = AlertMediaOrCameraBinding.inflate(inflater)

        alertDialogBuilder.setView(bindingAlert.root)
        val alertDialog = alertDialogBuilder.create()

        bindingAlert.btnFromCamera.setOnClickListener { takePhotoFromCamera(); alertDialog.dismiss() }
        bindingAlert.btnFromGallery.setOnClickListener { selectImageFromGallery(); alertDialog.dismiss() }

        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        alertDialog.show()
    }

    private fun selectImageFromGallery() {
        if (hasStoragePermission()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoFromCamera() {
        if (hasCameraPermission()) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                takePictureLauncher.launch(takePictureIntent)
            } else {
                Toast.makeText(
                    this,
                    "No se encontró ninguna aplicación de cámara",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED


    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)


    private fun hasStoragePermission(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

}
