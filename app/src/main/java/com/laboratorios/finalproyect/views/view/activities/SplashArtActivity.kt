package com.laboratorios.finalproyect.views.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.laboratorios.finalproyect.R

class SplashArtActivity : AppCompatActivity() {

    //Value to use to request Permission
    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_art)

        //Animation Splash Screen

        val animlogo = AnimationUtils.loadAnimation(this, R.anim.animation)
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        imgLogo.startAnimation(animlogo)

        //Control the actions in the animation

        val intent = Intent(this, MainActivity::class.java)
        animlogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {

                //if Location's Permission is not accepted

                if(!isLocationPermissionGranted())
                {
                    //Request the permission

                    requestLocationPermission()
                }
                else{
                    //When is accepted

                    startActivity(intent)
                    finish()

                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }

    //Verification permission

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

    //Request the Permission

    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            //Rejected

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Permission Location").setMessage("Go to settings and accept location permissions")
                .setNeutralButton("Ok"){dialogInterface, it -> finish() }
                .setCancelable(false).show()

        }
        else{

            //First Time

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)

        }
    }

    //Override onRequest Function

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){

            //If requestCode is accepted

            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            else{

                //If request Code is not accepted

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission Location").setMessage("Go to settings and accept location permissions")
                    .setNeutralButton("Ok"){dialogInterface, it -> finish()}
                    .setCancelable(false).show()

            }
            else -> {}
        }
    }


}