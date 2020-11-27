package mx.itesm.eureka_corp.eureka_ar_android


import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.videoload.*

class videoLoader: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videoload)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)


        //Link if online
        val onlineUri = Uri.parse("")

        //Link offline
        val tipo = intent.getStringExtra("Tipo")


        if(tipo.toString() == "1"){
            var offlineUri = Uri.parse("android.resource://$packageName/${R.raw.cena}")
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(offlineUri)
            videoView.requestFocus()
            videoView.start()
        } else if(tipo.toString() == "2"){
            var offlineUri = Uri.parse("android.resource://$packageName/${R.raw.libertad}")
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(offlineUri)
            videoView.requestFocus()
            videoView.start()
        }
        else if(tipo.toString() == "3"){
            var offlineUri = Uri.parse("android.resource://$packageName/${R.raw.adan}")
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(offlineUri)
            videoView.requestFocus()
            videoView.start()
        }else if(tipo.toString() == "4"){
            var offlineUri = Uri.parse("android.resource://$packageName/${R.raw.pizza}")
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(offlineUri)
            videoView.requestFocus()
            videoView.start()
        }

    }
}