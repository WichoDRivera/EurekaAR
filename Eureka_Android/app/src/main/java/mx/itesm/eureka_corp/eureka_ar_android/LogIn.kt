package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }



    fun log_in(v: View){
        val login = Intent(this, CameraAR:: class.java)
        startActivityForResult(login, 500)
    }
}