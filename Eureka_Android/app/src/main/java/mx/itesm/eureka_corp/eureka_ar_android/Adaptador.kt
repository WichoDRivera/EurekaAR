package mx.itesm.eureka_corp.eureka_ar_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.renglon_museo.view.*

class Adaptador(private val arrDatos: Array<Museo>):
    RecyclerView.Adapter<Adaptador.VistaRenglon>(){
    var listener:ClickListener?=null
    class VistaRenglon(val vistaRenglon: View) : RecyclerView.ViewHolder(vistaRenglon) {
        fun set(museo: Museo){
            vistaRenglon.tvMuseo.text=museo.nombreMuseo
            vistaRenglon.tvDireccion.text=museo.direccion
            vistaRenglon.imgMuseo.setImageResource(museo.idImagen)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaRenglon {
        val vista= LayoutInflater.from(parent.context)
            .inflate(R.layout.renglon_museo, parent, false)
        return VistaRenglon(vista)
    }

    override fun onBindViewHolder(holder: VistaRenglon, position: Int) {
        val tarjeta= arrDatos[position]
        holder.set(tarjeta)
        //Listener
        holder.vistaRenglon.setOnClickListener {
            listener?.clicked(position)
        }
    }

    override fun getItemCount(): Int {
        return arrDatos.size
    }
}