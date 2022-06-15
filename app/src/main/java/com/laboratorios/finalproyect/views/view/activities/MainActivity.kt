package com.laboratorios.finalproyect.views.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setup Main Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root


        //Toast.makeText(this,"Puedes activar tu GPS", Toast.LENGTH_LONG).show()
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


/* Some notes
    Validate if connected to the network at moment open the app and get de data in the database
 */
