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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.tvAcceder
import kotlinx.android.synthetic.main.fragment_sign_up.*


class StartFrag : Fragment() {

    private val LOGIN_GOOGLE: Int = 500
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    lateinit var globalContext : Loading
    lateinit var account: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();

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
            sendInfoDatabase(account)
            enterApp()
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

        val usuario = Usuario(nombre, username, email, "", photo_url)
        val database = FirebaseDatabase.getInstance()
        val referencia = database.getReference("/Users/$username")
        referencia.setValue(usuario)

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

        //Auth Eureka
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
        tvAcceder.setOnClickListener {
            SignInButton(it)
        }

        //Auth Google
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(globalContext, gso);
        // Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(globalContext)!!
        updateUIGoogle(account)
        configurarBtnGoogle()

    }



    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            println("Usuario: ${currentUser?.displayName}")
            enterApp()
        }else{
            println("No login")
        }


    }
    private fun enterApp() {
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
        val email = tdUser.text.toString()
        val password = tePassword.text.toString()
        SignInFB(email, password)
    }

    fun SignInFB(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(globalContext,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        println("signInWithEmail:failure")
                        Toast.makeText(
                            globalContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                    // ...
                })
    }


}
