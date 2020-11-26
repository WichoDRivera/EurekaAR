package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_museums2.*

class Museums2 : AppCompatActivity(), ClickListener {
    lateinit var arrMuseo: Array<Museo>

    lateinit var arrInfoMuseos : MutableList<HashMap<String, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_museums2)

        configurarRecyclerView()
    }

    private fun configurarRecyclerView() {
        val admLayout = LinearLayoutManager(this)
        arrInfoMuseos = mutableListOf()
        descargarMuseosInfo(admLayout, this)
    }


    override val info: Any
        get() = TODO("Not yet implemented")

    override fun clicked(position: Int) {

            when(position){
                0->{println("Hizo click sobre: $position")
                    var museo= arrMuseo[position]
                    val url= Uri.parse("http://museopalaciodebellasartes.gob.mx/")
                    val intInfo= Intent(Intent.ACTION_VIEW, url)
                startActivity(intInfo)
                }
                1->{println("Hizo click sobre: $position")
                    var museo= arrMuseo[position]
                    val url= Uri.parse("https://www.mna.inah.gob.mx/")
                    val intInfo= Intent(Intent.ACTION_VIEW, url)
                    startActivity(intInfo)

                }
                2->{println("Hizo click sobre: $position")
                    var museo= arrMuseo[position]
                    val url= Uri.parse("http://www.museosoumaya.org/")
                    val intInfo= Intent(Intent.ACTION_VIEW, url)
                    startActivity(intInfo)

                }

            }

        }
    override fun onBackPressed() {
        val intent = Intent(this, Profile::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }


    private fun descargarMuseosInfo(admLayout: LinearLayoutManager, context: Museums2) {
        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Museums")
        referencia.addListenerForSingleValueEvent(object  : ValueEventListener {
            override fun onCancelled(snapshot: DatabaseError) {
                TODO("Not yet implemented")
                println("jokoj")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                arrInfoMuseos.clear()
                for(dato in snapshot.children){
                    var info: HashMap<String, Int> = dato.getValue() as HashMap<String, Int>
                    arrInfoMuseos.add(info)
                }

                arrMuseo = arrayOf(Museo("name", "address", R.drawable.museo_bellas_artes), Museo("name", "address", R.drawable.museo_bellas_artes), Museo("name", "address", R.drawable.museo_bellas_artes))
                var i = 0
                for(info in arrInfoMuseos){
                    val address = info["address"].toString()
                    val name = info["name"].toString()
                    when(i){
                        0->{
                            arrMuseo[i] = Museo(name, address, R.drawable.museo_bellas_artes)
                        }
                        1->{
                            arrMuseo[i] = Museo(name, address, R.drawable.museo_de_antroplogia)
                        }
                        2->{
                            arrMuseo[i] = Museo(name, address, R.drawable.museo_soumaya)
                        }

                    }
                    i+=1
                }

                val adaptador = Adaptador(arrMuseo)
                rvMuseo.layoutManager = admLayout
                rvMuseo.adapter = adaptador
                //Listener
                adaptador.listener = context
            }

        })

    }

}

