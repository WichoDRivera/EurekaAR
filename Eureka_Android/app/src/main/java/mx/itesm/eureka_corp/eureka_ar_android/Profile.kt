package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    lateinit var arrPaintings: Array<Painting>

    lateinit var arrInfo : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        arrInfo = mutableListOf()

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
                    println(info)
                    arrInfo.add(info as String)
                }
                tvName.text = arrInfo[1]
                tvUser.text = "@" + arrInfo[3]

            }


        })

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
                        tvCountPaintings.text = "0"+count.toString()
                    }else{
                        tvCountPaintings.text = count.toString()
                    }



                } else {
                    val watched = Watched(0,0,0,0,0)
                    referencia2.setValue(watched)

                }

            }


        })

    }


    fun cameraar(v: View){
        val camera = Intent(this, CameraAR:: class.java)
        startActivityForResult(camera, 500)
    }

    private fun configurarRecyclerView() {
        val admLayout = LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        arrPaintings = createPaintingArray()

        val adaptador = ProfileAdaptador(arrPaintings)
        rvPaintings.layoutManager = admLayout
        rvPaintings.adapter = adaptador

    }

    private fun createPaintingArray(): Array<Painting> {
        return  arrayOf(
            Painting("Dos Fridas", "Frida Kahlo", R.drawable.frida),
            Painting("Dinner", "Van Gogh", R.drawable.van),
            Painting("Hidalgo", "Miguel Orozco", R.drawable.orozco)
        )
    }
}