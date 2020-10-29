package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFrag : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    lateinit var globalContext : Loading


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
        
        tvAcceder.setOnClickListener {
            createAccountButton(it)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Loading){
            globalContext = context
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            println("Usuario: ${currentUser?.displayName}")
        } else {
            println("No ha hecho login")
        }
    }

    fun createAccountButton(v:View){
        val nombre = etNombre.text.toString()
        val usuario = etUsuario.text.toString()
        val email = etCorreo.text.toString()
        val password = etConstraseña.text.toString()
        val confPassword = etConfContraseña.text.toString()

        if(password == confPassword){
            if(password.length >= 6){
                val referencia = database.getReference("/Users/$usuario")
                referencia.addListenerForSingleValueEvent(object  : ValueEventListener {
                    override fun onCancelled(snapshot: DatabaseError) {
                        Toast.makeText(
                            globalContext, "Error con nuestros servidores. Inténtalo mas tarde.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        println(snapshot.child("usuario").getValue())
                        if(snapshot.child("usuario").getValue() == usuario){
                            Toast.makeText(
                                globalContext, "El usuario ya esta ocupado, elige otro.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else if(snapshot.child("email").getValue() == email){
                            Toast.makeText(
                                globalContext, "El correo ya esta ocupado, elige otro.",
                                Toast.LENGTH_LONG
                            ).show()
                        }else{
                            referencia.setValue(usuario)
                            createAccount(email,password)
                            escribirDatosDB(nombre, usuario, email, password)
                            enterApp()
                        }
                    }
                })

            }else{
                Toast.makeText(
                    globalContext, "La contraseña debe de tener al menos 6 caracteres",
                    Toast.LENGTH_LONG
                ).show()
            }
        }else{
            Toast.makeText(
                globalContext, "Las contraseñas no coinciden",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    private fun enterApp() {
        val intEnter = Intent(globalContext, Profile::class.java)
        startActivity(intEnter)
    }

    private fun escribirDatosDB(nombre: String, username: String, email: String, password: String) {
        val usuario = Usuario(nombre, username, email, password, "")
        val referencia = database.getReference("/Users/$username")
        referencia.setValue(usuario)
    }

    fun createAccount(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                globalContext
            ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("createUserWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        println("createUserWithEmail:failure ${task.exception}")
                        Toast.makeText(
                            globalContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                }
    }

}