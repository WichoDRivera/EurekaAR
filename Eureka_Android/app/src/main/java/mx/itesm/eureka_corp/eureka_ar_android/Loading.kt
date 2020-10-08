package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.android.synthetic.main.fragment_start.*

@Suppress("DEPRECATION")
class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        mlLoading.transitionToStart()
        mlLoading.transitionToEnd()

    }

    fun main_menu(v: View){
        val sign = Intent(this, CameraAR:: class.java)
        startActivityForResult(sign, 500)
    }

    fun signUp(v: View) {
        val fragmento = SignUpFrag()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment4, fragmento)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

    }
}




