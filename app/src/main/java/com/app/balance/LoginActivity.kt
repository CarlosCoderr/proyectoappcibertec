package com.app.balance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.balance.enity.Usuario
import com.app.balance.repo.UsuariosRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var tilCorreo : TextInputLayout
    private lateinit var tietCorreo : TextInputEditText
    private lateinit var tilClave : TextInputLayout
    private lateinit var tietClave : TextInputEditText
    private lateinit var btnLogin : MaterialButton
    private lateinit var tvClaveOlvidada : TextView
    private lateinit var tvRegistro : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        incializarVistas()
        configurandoListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun incializarVistas(){
        tilCorreo = findViewById(R.id.tilCorreo)
        tietCorreo = findViewById(R.id.tietCorreo)
        tilClave = findViewById(R.id.tilClave)
        tietClave = findViewById(R.id.tietClave)
        btnLogin = findViewById(R.id.btnLogin)
        tvClaveOlvidada = findViewById(R.id.tvClaveOlvidada)
        tvRegistro = findViewById(R.id.tvRegistro)
    }

    private fun configurandoListeners(){
        btnLogin.setOnClickListener {
            validarCampos()
        }
        tvRegistro.setOnClickListener {
            cambioActivity(RegistroActivity::class.java)
        }
        tietCorreo.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tilCorreo.error = null
            }
            tietClave.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) tilClave.error = null
                }
            }


       private fun validarCampos() {
           val correo = tietCorreo.text.toString()
           val clave = tietClave.text.toString()
           var error = false

           if (correo.isEmpty()) {
               tilCorreo.error = "Ingrese un correo"
               error = true
           } else {
               tilCorreo.error = null
           }

           if (clave.isEmpty()) {
               tilClave.error = "Ingrese una clave"
               error = true
           } else {
               tilClave.error = null
           }

           if (error) {
               return
           } else {
               iniciarSesion(correo, clave)
           }
       }

    private fun iniciarSesion(correo: String, clave: String){
        Toast.makeText(this, "Validando datos...", Toast.LENGTH_SHORT).show()
        val usuarioEncontrado = UsuariosRepository.buscarUsuario(correo, clave)

        if (usuarioEncontrado != null){
            Toast.makeText(
                this, "Bienvenido ${usuarioEncontrado.nombre}", Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, DivisaActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Correo o clave incorrectos", Toast.LENGTH_SHORT).show()

        }
    }

    private fun cambioActivity(activityDestino : Class<out Activity>){
        val intent = Intent(this, activityDestino)
        startActivity(intent)
    }
}