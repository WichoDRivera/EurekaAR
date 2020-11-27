package mx.itesm.eureka_corp.eureka_ar_android

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_camera_a_r.*

class CameraAR : AppCompatActivity() {

    private var isText = false
    var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_a_r)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        bindCameraUseCases()
        captureButton.setOnClickListener {
            takePhoto()

        }

        /** Boton para elegir imagen de la galeria
        fab.setOnClickListener{
        //Elegir imagen de galeria
        pickImage()
        }
         */
    }

    /**
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if(resultCode == Activity.RESULT_OK){
    when(requestCode){
    IMAGE_PICK_CODE ->{
    val bitmap = getImageFromData(data)
    bitmap.apply {
    imageView.setImageBitmap(this)
    if(!isText){
    if (bitmap != null) {
    labelImage(bitmap)
    }
    }
    }
    }
    }
    }
    super.onActivityResult(requestCode, resultCode, data)
    }


    //Obtiene la imagen del celular
    private fun getImageFromData(data: Intent?):Bitmap?{
    val selectedImage = data?.data
    return MediaStore.Images.Media.getBitmap(
    this.contentResolver,
    selectedImage
    )
    }
     */

    /**
    private fun pickImage() {
    val intent = Intent().apply{
    action = Intent.ACTION_PICK
    type = "image/*"  //Todos los formatos: JPG, JPEG, PNG
    }
    startActivityForResult(Intent.createChooser(intent,"Seleccione la Imagen"), IMAGE_PICK_CODE)
    }

    */ */

    fun labelImage(bitmap: Bitmap){
        val options: FirebaseVisionOnDeviceImageLabelerOptions = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.8f)
            .build()

        val detector: FirebaseVisionImageLabeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options)

        detector.processImage(FirebaseVisionImage.fromBitmap(bitmap))
            .addOnSuccessListener { labels ->
                for(label: FirebaseVisionImageLabel in labels){
                    val text = label.text
                    val confidence = (label.confidence*100).toInt()
                    Toast.makeText(this,"Its a $confidence% $text",Toast.LENGTH_LONG).show()

                    var tipo = 0
                    //Set code for video
                    if(text.toString() == "Fun"){
                        tipo = 1
                        //Move to video
                        starVideo(tipo)
                    }else if(text.toString() == "Musical instrument"){
                        tipo = 2
                        //Move to video
                        starVideo(tipo)
                    }else if(text.toString() == "Muscle"){
                        tipo = 3
                        //Move to video
                        starVideo(tipo)
                    }else if(text.toString() == "Sky"){
                        tipo = 4
                        //Move to video
                        starVideo(tipo)
                    }


                }
            }.addOnFailureListener{exception ->
                Toast.makeText(this,exception.message,Toast.LENGTH_LONG).show()
            }

    }

    fun starVideo(tipo: Int) {
        val video = Intent(this, videoLoader:: class.java)
        video.putExtra("Tipo", tipo.toString())
        startActivityForResult(video, 500)
    }

    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {

        val buffer = imageProxy.planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        //Rotate bitmap
        val matrix = Matrix()
        matrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

    fun takePhoto() {
        imageCapture?.takePicture(ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = imageProxyToBitmap(image)
                //textRecognize(bitmap)
                labelImage(bitmap)
                super.onCaptureSuccess(image)
            }
        })
    }

    fun bindCameraUseCases() {
        val rotation = 0
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetRotation(rotation)
                .build()

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(960, 1280))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(rotation)
                .build()
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            preview.setSurfaceProvider(viewFinder.createSurfaceProvider(camera.cameraInfo))
        }, ContextCompat.getMainExecutor(this))
    }

    companion object{
        private var IMAGE_PICK_CODE = 180
    }

}