package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    lateinit var arrPaintings: Array<Painting>

    lateinit var arrInfo : MutableList<String>
    lateinit var arr_painting_data : MutableList<String>
    lateinit var painting_meta : MutableList<MutableList<String>>
    lateinit var arr_paintings : MutableList<Painting>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        arrPaintings = arrayOf()
        arrInfo = mutableListOf()
        arr_painting_data= mutableListOf()
        painting_meta = mutableListOf()
        arr_paintings = mutableListOf()
        val user = intent.getStringExtra("user")
        actualizarInformacion(user)

        configurarRecyclerView()
    }



    private fun actualizarInformacion(user: String?) {

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
                            val name = painting_meta[i][2]
                            val technique = painting_meta[i][3]
                            val year = painting_meta[i][4]

                            if(i == 0){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.merry))

                            }else if(i == 1){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.cena))

                            }else if(i == 2){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.libertad))

                            }else if(i == 3){
                                arr_paintings.add(Painting(name, technique, artist, year.toInt(), R.drawable.adan))

                            }else if(i == 4){
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
                arr_painting_data.clear()

                for(dato in snapshot.children){
                    val info = dato.getValue()
                    arr_painting_data.add(info.toString())
                }

                painting_meta.add(arr_painting_data)
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


}