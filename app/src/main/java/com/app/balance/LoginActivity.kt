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
import com.app.balance.repo.UsuarioRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tilClave: TextInputLayout
    private lateinit var tietClave: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvClaveOlvidada: TextView
    private lateinit var tvRegistro: TextView

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

    private fun incializarVistas() {
        tilCorreo = findViewById(R.id.tilCorreo)
        tietCorreo = findViewById(R.id.tietCorreo)
        tilClave = findViewById(R.id.tilClave)
        tietClave = findViewById(R.id.tietClave)
        btnLogin = findViewById(R.id.btnLogin)
        tvClaveOlvidada = findViewById(R.id.tvClaveOlvidada)
        tvRegistro = findViewById(R.id.tvRegistro)
    }

    private fun configurandoListeners() {
        btnLogin.setOnClickListener { validarCampos() }
        tvRegistro.setOnClickListener { cambioActivity(RegistroActivity::class.java) }

        tietCorreo.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilCorreo.error = null }
        tietClave.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilClave.error = null }
    }

    private fun validarCampos() {
        val correo = tietCorreo.text?.toString()?.trim().orEmpty()
        val clave = tietClave.text?.toString()?.trim().orEmpty()
        var error = false

        if (correo.isEmpty()) {
            tilCorreo.error = "Ingrese un correo"
            error = true
        } else tilCorreo.error = null

        if (clave.isEmpty()) {
            tilClave.error = "Ingrese una clave"
            error = true
        } else tilClave.error = null

        if (!error) iniciarSesion(correo, clave)
    }

    private fun iniciarSesion(correo: String, clave: String) {
        Toast.makeText(this, "Validando datos...", Toast.LENGTH_SHORT).show()

        // Buscar usuario en repositorio
        val usuario = UsuarioRepository.buscarUsuario(correo, clave)

        if (usuario != null) {
            // Guardar sesión localmente
            getSharedPreferences("AppPreferences", MODE_PRIVATE).edit()
                .putInt("USER_ID", usuario.codigo)
                .putString("USER_NOMBRE", "${usuario.nombre} ${usuario.apellidos}")
                .putString("USER_CORREO", usuario.correo)
                .apply()

            Toast.makeText(this, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()

            // Ir a la pantalla principal
            startActivity(
                Intent(this, InicioActivity::class.java)
                    .putExtra("open", "bienvenida")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else {
            Toast.makeText(this, "Correo o clave incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cambioActivity(activityDestino: Class<out Activity>) {
        val intent = Intent(this, activityDestino)
        startActivity(intent)
    }
}
