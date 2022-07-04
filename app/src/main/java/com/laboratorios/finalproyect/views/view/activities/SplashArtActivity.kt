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


    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_art)

      

        val animlogo = AnimationUtils.loadAnimation(this, R.anim.animation)
        val imgLogo: ImageView = findViewById(R.id.imgLogo)
        val imgLogoBanco: ImageView = findViewById(R.id.imgLogoBanc)
        imgLogo.startAnimation(animlogo)
        imgLogoBanco.startAnimation(animlogo)



        val intent = Intent(this, MainActivity::class.java)
        animlogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {



                if(!isLocationPermissionGranted())
                {


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



            val builder = AlertDialog.Builder(this)
            builder.setTitle("Permiso de ubicacion").setMessage("Ve a ajuste y acepta los permisos")
                .setNeutralButton("Ok"){dialogInterface, it -> finish() }
                .setCancelable(false).show()

        }
        else{



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
                builder.setTitle("Permiso de ubicacion").setMessage("Ve a ajuste y acepta los permisos")
                    .setNeutralButton("Ok"){dialogInterface, it -> finish()}
                    .setCancelable(false).show()

            }
            else -> {}
        }
    }


}