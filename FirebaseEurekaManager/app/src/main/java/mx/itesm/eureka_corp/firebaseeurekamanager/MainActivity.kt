package mx.itesm.eureka_corp.firebaseeurekamanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun usersDatabase(v:View) {
        val intEnter = Intent(this, Users::class.java)
        startActivityForResult(intEnter, 500)
    }

    fun paintingsDatabase(v:View) {
        val intEnter = Intent(this, Paintings::class.java)
        startActivityForResult(intEnter, 500)
    }


}