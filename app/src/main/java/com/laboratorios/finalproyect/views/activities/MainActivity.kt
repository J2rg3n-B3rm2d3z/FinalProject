package com.laboratorios.finalproyect.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigation()
    }

    private fun setupNavigation()
    {
        //Navigation controller
        val menuItems: BottomNavigationView = findViewById(R.id.bttmNavMenu)
        NavigationUI.setupWithNavController(menuItems,
            Navigation.findNavController(this, R.id.frg_nav))
    }
}