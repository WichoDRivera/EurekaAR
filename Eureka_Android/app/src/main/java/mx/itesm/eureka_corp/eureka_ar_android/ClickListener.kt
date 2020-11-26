package mx.itesm.eureka_corp.eureka_ar_android

interface ClickListener {
    abstract val info: Any

    fun clicked(position: Int)

    //Opcionales
    fun longClicked(posicion: Int){

    }

}
