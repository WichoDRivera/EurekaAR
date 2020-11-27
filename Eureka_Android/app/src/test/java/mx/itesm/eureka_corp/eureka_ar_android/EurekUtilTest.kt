package mx.itesm.eureka_corp.eureka_ar_android

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EurekUtilTest{

    @Test
    fun validUserInput(){
        val result = EurekUtil.validateUserInput("Carlos Slim", "cslim", "cslim@telmex.mx", "password", "password")
        assertThat(result).isTrue()
    }

    @Test
    fun registeredUser(){
        val result = EurekUtil.validateUserInput("Luis Neri", "qiqueneri1999", "neri@hotmail.com", "password", "password")
        assertThat(result).isFalse()
    }

    @Test
    fun registeredEmail(){
        val result = EurekUtil.validateUserInput("Roberto Martinez", "rmr", "rmroman@tec.mx", "password", "password")
        assertThat(result).isFalse()
    }

    @Test
    fun differentPasswords(){
        val result = EurekUtil.validateUserInput("Roberto Martinez", "rmroman", "rmroman@tec.mx", "password", "diff_password")
        assertThat(result).isFalse()
    }



}