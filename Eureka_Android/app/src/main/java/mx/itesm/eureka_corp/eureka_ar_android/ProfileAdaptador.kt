package mx.itesm.eureka_corp.eureka_ar_android


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.painting_card.view.*

//import kotlinx.android.synthetic.main.renglon_bandera.view.*

class ProfileAdaptador(private val arrDatos: Array<Painting>):
    RecyclerView.Adapter<ProfileAdaptador.VistaRenglon>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaRenglon {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.painting_card, parent, false)
        return VistaRenglon(vista)
    }

    override fun getItemCount(): Int {
        return arrDatos.size
    }

    override fun onBindViewHolder(holder: VistaRenglon, position: Int) {
        val bandera = arrDatos[position]
        holder.set(bandera)

    }

    class VistaRenglon ( val vistaRenglon:View) : RecyclerView.ViewHolder(vistaRenglon){

        fun set(painting: Painting){
            vistaRenglon.imgBandera.setImageResource(painting.idImagen)
        }
    }

}