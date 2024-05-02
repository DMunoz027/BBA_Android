package com.doris.bba_android.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.doris.bba_android.R
import com.doris.bba_android.databinding.ActivityHomeUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home_user)
        navView.setupWithNavController(navController)
    }
}
