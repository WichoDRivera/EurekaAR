package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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
        if (checkAllFieldsFilled()){
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
                            }else{
                                val path = email.substringBeforeLast(".") + "_" + email.substringAfterLast(".")
                                val referencia2 = database.getReference("/Email/$path")
                                referencia2.addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot2: DataSnapshot) {
                                        if(snapshot2.child("email").getValue() == email){
                                            Toast.makeText(
                                                globalContext, "El correo ya está ocupado, elige otro.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }else {
                                            escribirDatosDB(nombre, usuario, email, "")
                                            createAccount(email, password)
                                            enterApp(usuario)
                                        }
                                    }

                                    override fun onCancelled(snapshot2: DatabaseError) {
                                        Toast.makeText(
                                            globalContext, "Error con nuestros servidores. Inténtalo mas tarde.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                })
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



    }

    private fun checkAllFieldsFilled(): Boolean {
        val nombre = etNombre.text.toString()
        val usuario = etUsuario.text.toString()
        val email = etCorreo.text.toString()
        val password = etConstraseña.text.toString()
        val confPassword = etConfContraseña.text.toString()
        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(
                globalContext, "Debes de proporcionar un nombre.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (TextUtils.isEmpty(usuario)){
            Toast.makeText(
                globalContext, "Debes de proporcionar un nombre de usuario.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (TextUtils.isEmpty(email)){
            Toast.makeText(
                globalContext, "Debes de proporcionar un correo.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (TextUtils.isEmpty (password) || TextUtils.isEmpty(nombre)){
            Toast.makeText(
                globalContext, "Debes de proporcionar una contraseña.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }else if (TextUtils.isEmpty (confPassword) || TextUtils.isEmpty(nombre)){
            Toast.makeText(
                globalContext, "Es necesario la confirmación de la contraseña.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }else{
            return true
        }

    }

    private fun enterApp(usuario: String) {
        val intEnter = Intent(globalContext, Profile::class.java)
        intEnter.putExtra("user", usuario)
        startActivity(intEnter)
    }

    private fun escribirDatosDB(nombre: String, username: String, email: String, photoURL: String) {
        val usuario = Usuario(nombre, username, email, photoURL)

        //User
        val referencia = database.getReference("/Users/$username")
        referencia.setValue(usuario)

        // PAINTING
        val referencia2 = database.getReference("/Watched/$username")
        val watched = Watched(0,0,0,0,0)
        referencia2.setValue(watched)

        //MAIL
        val path = email.substringBeforeLast(".") + "_" + email.substringAfterLast(".")
         val referencia3 = database.getReference("/Email/$path")
        referencia3.setValue(usuario)
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