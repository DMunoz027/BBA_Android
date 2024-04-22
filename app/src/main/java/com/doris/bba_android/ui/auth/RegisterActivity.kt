package com.doris.bba_android.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.doris.bba_android.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityRegisterBinding
    private val binding get() = _binding

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

    private fun initRegister() {
        Toast.makeText(this, "Iniciando registro", Toast.LENGTH_SHORT).show()
    }

    private fun jumpNextActivity(activity: Activity, finish: Boolean = false) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        if (finish)
            finish()
    }
}