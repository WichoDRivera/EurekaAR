package mx.itesm.eureka_corp.firebaseeurekamanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_paintings.*
import kotlinx.android.synthetic.main.activity_users.*

class Paintings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paintings)
    }

    fun grabarDatos(v: View){
        val id = idPainting.text.toString()
        val name = namePainting.text.toString()
        val year = yearPainting.text.toString().toInt()
        val artist = artistName.text.toString()
        val technique = technique.text.toString()


        escribirDatosNube(id, name, year, artist, technique)
    }


    fun escribirDatosNube(id:String, name:String, year:Int, artist:String, technique:String) {
        val pintura = Painting(id, name, year, artist, technique)
        val baseDatos = FirebaseDatabase.getInstance()
        val referencia = baseDatos.getReference("/Paintings/$id")
        referencia.setValue(pintura)

    }
}