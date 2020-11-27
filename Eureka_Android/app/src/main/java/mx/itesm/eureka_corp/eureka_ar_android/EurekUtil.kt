package mx.itesm.eureka_corp.eureka_ar_android

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_sign_up.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_museums2.*
import kotlin.system.exitProcess


object EurekUtil {


    // SignUpFrag

    lateinit var arrInfoMuseos : MutableList<HashMap<String, Int>>
    private val existingUsers = arrayOf("qiqueneri1999", "rmroman")
    private val existingEmail = arrayOf("qiqueneri1999@hotmail.com", "rmroman@tec.mx")
    private val arrmuseos = arrayOf("M1", "M2", "M3")


    fun validateUserInput(nombre: String, usuario : String, email: String,password: String, confPassword: String): Boolean {
        return password == confPassword && password.length>6 && usuario !in existingUsers && email !in existingEmail
    }


}