package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class Profile : AppCompatActivity() {
    lateinit var arrPaintings: Array<Painting>

    lateinit var arrInfo : MutableList<String>
    lateinit var arr_painting_data : MutableList<String>
    lateinit var painting_meta : MutableList<MutableList<String>>
    lateinit var arr_paintings : MutableList<Painting>
    lateinit var user: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var type: String

    private lateinit var mGoogleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        arrPaintings = arrayOf()
        arrInfo = mutableListOf()
        arr_painting_data= mutableListOf()
        painting_meta = mutableListOf()
        arr_paintings = mutableListOf()
        user = intent.getStringExtra("user").toString()

        configurarRecyclerView()

        mAuth = FirebaseAuth.getInstance()
        type = intent.getStringExtra("type").toString()
    }

    override fun onStart() {
        super.onStart()
        ivLogOut.setOnClickListener{
            logOutButton()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        arrPaintings = arrayOf()
        arrInfo.clear()
        arr_painting_data.clear()
        painting_meta.clear()
        arr_paintings.clear()
        actualizarInformacion()

    }

    private fun logOutButton() {
        if(type == "Eureka") {
            mAuth.signOut()
            val intent = Intent(this, Loading::class.java).apply {
            }
            startActivity(intent)
        }else{
            mGoogleSignInClient.signOut()
            val intent = Intent(this, Loading::class.java).apply {
            }
            startActivity(intent)
        }

    }

    private fun actualizarInformacion() {

        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Users/${user}")
        referencia.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                TODO("Not yet implemented")
                println("jokoj")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                arrInfo.clear()
                for(dato in snapshot.children){
                    val info = dato.getValue()
                    arrInfo.add(info as String)
                }
                tvName.text = arrInfo[1]
                tvUser.text = "@" + arrInfo[3]
            }

        })

        getPainting(baseDatos.getReference("/Paintings/1"))
        getPainting(baseDatos.getReference("/Paintings/2"))
        getPainting(baseDatos.getReference("/Paintings/3"))
        getPainting(baseDatos.getReference("/Paintings/4"))
        getPainting(baseDatos.getReference("/Paintings/5"))

        val referencia2 = baseDatos.getReference("/Watched/${user}")
        referencia2.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                TODO("Not yet implemented")
                println("jokoj")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    arrInfo.clear()
                    var count: Int = 0
                    for(dato in snapshot.children){
                        val info = dato.getValue()
                        count +=  info.toString().toInt()
                        arrInfo.add(info.toString())
                    }

                    if(count==1){
                        tvTextoPintura.text = "PINTURA"
                        tvTextoEscaneada.text = "ESCANEADA"
                    }else{
                        tvTextoPintura.text = "PINTURAS"
                        tvTextoEscaneada.text = "ESCANEADAS"
                    }

                    if(count == 0){
                        tvCountPaintings.text = count.toString()
                    }else if (count<10){
                        tvCountPaintings.text = "0" + count.toString()
                    }else{
                        tvCountPaintings.text = count.toString()
                    }

                    for(i in 0 until painting_meta.size){

                        if(arrInfo[i].toInt() == 1){
                            val artist = painting_meta[i][0]
                            val code = painting_meta[i][1].toInt() -1
                            val name = painting_meta[i][2]
                            val technique = painting_meta[i][3]
                            val year = painting_meta[i][4]

                            if(code == 0){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.merry))

                            }else if(code == 1){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.cena))

                            }else if(code == 2){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.libertad))

                            }else if(code == 3){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.adan))

                            }else if(code == 4){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.piazza))

                            }

                        }
                    }


                } else {
                    val watched = Watched(0,0,0,0,0)
                    referencia2.setValue(watched)

                }
            }
        })

    }

    private fun getPainting(reference: DatabaseReference) {
        reference.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var arr : MutableList<String> = mutableListOf()
                for(dato in snapshot.children){
                    var info = dato.getValue()
                    arr.add(info.toString())
                }

                painting_meta.add(arr)

            }
        })
    }


    fun cameraar(v: View){
        val camera = Intent(this, CameraAR:: class.java)
        startActivityForResult(camera, 500)
    }

    private fun configurarRecyclerView() {
        val admLayout = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)


        val adaptador = ProfileAdaptador(arr_paintings)
        rvPaintings.layoutManager = admLayout
        rvPaintings.adapter = adaptador

    }

    fun changeToMuseum(view: View) {
        val intent = Intent(this, Museums2::class.java).apply {
        }
        startActivity(intent)
    }

    fun changeToInfo(view: View) {
        val intent = Intent(this, AcercaDe::class.java).apply {
        }
        startActivity(intent)
    }



}