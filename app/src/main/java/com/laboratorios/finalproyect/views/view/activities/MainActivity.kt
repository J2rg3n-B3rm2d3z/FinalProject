package com.laboratorios.finalproyect.views.view.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.ActivityMainBinding
import com.laboratorios.finalproyect.views.AlertReceiver
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root


        Toast.makeText(this,"Puedes activar tu GPS", Toast.LENGTH_LONG).show()
        setContentView(view)
        setupNavigation()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,8)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE,1)
        }

        startAlarm(calendar)
    }


    private fun startAlarm(calendar: Calendar) {
        val alarmManager:AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val intent:Intent = Intent(this,AlertReceiver::class.java)
        var pending:PendingIntent? = null

        pending = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_MUTABLE)
        else
            PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_IMMUTABLE)


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
    }

    private fun setupNavigation()
    {

        val menuItems: BottomNavigationView = findViewById(R.id.bttmNavMenu)
        NavigationUI.setupWithNavController(menuItems,
            Navigation.findNavController(this, R.id.frg_nav))
    }
}
