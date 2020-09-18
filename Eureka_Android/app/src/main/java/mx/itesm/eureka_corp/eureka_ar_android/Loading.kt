package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }
    fun signUp(v: View){
        val signUp = Intent(this, SignUp:: class.java)
        startActivityForResult(signUp, 500)
    }
}