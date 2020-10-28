package mx.itesm.eureka_corp.firebaseeurekamanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_users.*

class Users : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
    }


    fun grabarDatos(v: View){
        val user = userId.text.toString()
        val name = name.text.toString()
        val password = password.text.toString()
        val email = email.text.toString()

        escribirDatosNube(user, name, password, email)
    }


    fun escribirDatosNube(user:String, name:String,  password:String,  email:String) {
        val usuario = User(user, name, email, password)
        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Users/$user")
        referencia.setValue(usuario)

    }
}