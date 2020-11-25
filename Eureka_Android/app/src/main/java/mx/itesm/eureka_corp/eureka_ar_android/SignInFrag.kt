package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.tvAcceder
import kotlinx.android.synthetic.main.fragment_sign_up.*


class StartFrag : Fragment() {

    private lateinit var database: FirebaseDatabase
    private val LOGIN_GOOGLE: Int = 500
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    lateinit var globalContext : Loading
    var account: GoogleSignInAccount?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance()

    }

    private fun configurarBtnGoogle() {
        IVSignInGoogle.setOnClickListener{
            val intGoogle = mGoogleSignInClient.signInIntent
            startActivityForResult(intGoogle, LOGIN_GOOGLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === LOGIN_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            // Signed in successfully, show authenticated UI.
            updateUIGoogle(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            println("signInResult:failed code= ${e.statusCode}")
            updateUIGoogle(null)
        }
    }

    private fun updateUIGoogle(account: GoogleSignInAccount?) {
        if(account != null){
            println("Login Google")
            println("Hello there!")
            val username = account?.account.toString().split("=", "@")[1]
            val referencia = database.getReference("/Users/$username")
            referencia.addListenerForSingleValueEvent(object  : ValueEventListener {
                override fun onCancelled(snapshot: DatabaseError) {
                    Toast.makeText(
                        globalContext, "Error con nuestros servidores. Inténtalo mas tarde.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    println(snapshot.child("usuario").getValue())
                    if(snapshot.child("usuario").getValue() == username){
                        enterAppGoogle(account)
                    }else {
                        sendInfoDatabase(account)
                        enterAppGoogle(account)
                    }


                }
            })
        }else{
            println("No login Google")
        }

    }

    private fun sendInfoDatabase(account: GoogleSignInAccount) {
        val nombre = account?.displayName.toString()
        val username = account?.account.toString().split("=", "@")[1]
        val email = account?.email.toString()
        val photo_url = account?.photoUrl.toString()
        println("Usuario: ${username}")

        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("/Users/$username")
        val usuario = Usuario(nombre, username, email,  photo_url)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onStart() {
        super.onStart()

        // configurar Auth Eureka
        val currentUser = mAuth.currentUser
        tvAcceder.setOnClickListener {
            SignInButton(it)
        }

        //configurar Auth Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(globalContext, gso);
        account = GoogleSignIn.getLastSignedInAccount(globalContext)
        configurarBtnGoogle()

        if(account != null) {
           updateUIGoogle(account)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?, usuario: String) {
        if(currentUser != null){
            println("Usuario: ${currentUser?.displayName}")
            println("Hello there!")
            enterApp(usuario)
        }else{
            println("No login")
        }
    }
    private fun enterApp(usuario: String) {
        val intEnter = Intent(globalContext, Profile::class.java)
        intEnter.putExtra("user", usuario)
        startActivity(intEnter)
    }

    private fun enterAppGoogle(account: GoogleSignInAccount) {
        val intEnter = Intent(globalContext, Profile::class.java)
        intEnter.putExtra("user", account?.account.toString().split("=", "@")[1])
        startActivity(intEnter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Loading){
            globalContext = context
        }
    }

    fun SignInButton(v: View){
        val usuario = tdUser.text.toString()
        val password = tePassword.text.toString()
        val referencia = database.getReference("/Users/$usuario")
        referencia.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                Toast.makeText(
                    globalContext, "Error con nuestros servidores. Inténtalo mas tarde.",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var email = snapshot.child("email").getValue() as String
                SignInFB(usuario, email, password)
            }
        })

    }

    fun SignInFB(usuario: String, email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(globalContext,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user, usuario)
                    } else {
                        // If sign in fails, display a message to the user.
                        println("signInWithEmail:failure")
                        Toast.makeText(
                            globalContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null, usuario)
                    }

                    // ...
                })
    }

}
