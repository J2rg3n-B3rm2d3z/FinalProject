package com.laboratorios.finalproyect.views.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.laboratorios.finalproyect.R

class SplashArtActivity : AppCompatActivity() {

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_art)

        val animlogo = AnimationUtils.loadAnimation(this, R.anim.animation)
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        imgLogo.startAnimation(animlogo)

        //Control the actions in the animation


        val intent = Intent(this, MainActivity::class.java)
        animlogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                //if Location's Permission are not accepted
                if(!isLocationPermissionGranted())
                {
                    //Request the permission
                    requestLocationPermission()
                }
                else{
                    startActivity(intent)
                    finish()
                }

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            //Rejected
            Toast.makeText(this,"Ve a ajustes y acepta los permisos de ubicacion", Toast.LENGTH_LONG).show()
            finish()

        }else{
            //First Time
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{

                Toast.makeText(this,"Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show()
                finish()

            }
            else -> {}
        }
    }


}