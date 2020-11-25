package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_museums2.*

class Museums2 : AppCompatActivity(), ClickListener {
    lateinit var arrMuseo: Array<Museo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_museums2)

        configurarRecyclerView()
    }

    private fun configurarRecyclerView() {
        val admLayout = LinearLayoutManager(this)
        //val admLayout= GridLayoutManager(this, 2)
        arrMuseo = crearArrTarjetas()
        val adaptador = Adaptador(arrMuseo)
        rvMuseo.layoutManager = admLayout
        rvMuseo.adapter = adaptador
        //Listener
        adaptador.listener = this
    }

        private fun crearArrTarjetas(): Array<Museo> {
            return arrayOf(
                Museo("Museo del Palacio de Bellas Artes", "Av. Juárez S/N, Centro Histórico de la Cdad. de México, Centro, Cuauhtémoc, 06050 Ciudad de México, CDMX", R.drawable.museo_bellas_artes),
                Museo("Museo Nacional de Antropología", "Av. Paseo de la Reforma s/n, Polanco, Bosque de Chapultepec I Secc, Miguel Hidalgo, 11560 Ciudad de México, CDMX", R.drawable.museo_de_antroplogia),
                Museo("Museo Soumaya", "Blvd. Miguel de Cervantes Saavedra, Granada, Miguel Hidalgo, 11529 Ciudad de México, CDMX", R.drawable.museo_soumaya)

            )
        }

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

}