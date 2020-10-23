package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth

class StartFrag : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var globalContext : Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Loading){
            globalContext = context
        }
    }



}
