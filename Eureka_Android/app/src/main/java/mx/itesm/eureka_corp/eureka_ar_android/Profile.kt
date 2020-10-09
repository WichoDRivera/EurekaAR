package mx.itesm.eureka_corp.eureka_ar_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {
    lateinit var arrPaintings: Array<Painting>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        configurarRecyclerView()
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