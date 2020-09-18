package mx.itesm.eureka_corp.eureka_ar_android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun sign_up(v: View){
        val sign = Intent(this, CameraAR:: class.java)
        startActivityForResult(sign, 500)
    }

    fun log_in(v: View){
        val login = Intent(this, LogIn:: class.java)
        startActivityForResult(login, 500)
    }

}