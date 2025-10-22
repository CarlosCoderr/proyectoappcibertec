package com.app.balance

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.UsuarioDAO
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tilClave: TextInputLayout
    private lateinit var tietClave: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvClaveOlvidada: TextView
    private lateinit var tvRegistro: TextView

    private lateinit var dbHelper: AppDatabaseHelper
    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializar base de datos
        dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        usuarioDAO = UsuarioDAO(db, dbHelper)

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
        tvClaveOlvidada.setOnClickListener {
            Toast.makeText(this, "Función en desarrollo", Toast.LENGTH_SHORT).show()
        }

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
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.error = "Ingrese un correo válido"
            error = true
        } else {
            tilCorreo.error = null
        }

        if (clave.isEmpty()) {
            tilClave.error = "Ingrese una contraseña"
            error = true
        } else if (clave.length < 6) {
            tilClave.error = "La contraseña debe tener al menos 6 caracteres"
            error = true
        } else {
            tilClave.error = null
        }

        if (!error) iniciarSesion(correo, clave)
    }

    private fun iniciarSesion(correo: String, clave: String) {
        btnLogin.isEnabled = false
        btnLogin.text = "Verificando..."

        try {
            // Obtener usuario por correo
            val usuario = usuarioDAO.obtenerUsuarioPorEmail(correo)

            if (usuario != null) {
                // Verificar contraseña (hashear y comparar)
                val claveHasheada = hashearContrasena(clave)

                if (usuario.contrasena == claveHasheada) {
                    // Guardar sesión en SharedPreferences
                    getSharedPreferences("AppPreferences", MODE_PRIVATE).edit()
                        .putInt("USER_ID", usuario.id)
                        .putString("USER_NOMBRE", "${usuario.nombre} ${usuario.apellido}")
                        .putString("USER_CORREO", usuario.email)
                        .putInt("USER_DIVISA_ID", usuario.divisaId)
                        .putBoolean("SESION_ACTIVA", true)
                        .apply()

                    Toast.makeText(this, "¡Bienvenido ${usuario.nombre}!", Toast.LENGTH_SHORT).show()

                    // Ir a la pantalla principal
                    startActivity(
                        Intent(this, DivisaActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    finish()
                } else {
                    tilClave.error = "Contraseña incorrecta"
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    btnLogin.isEnabled = true
                    btnLogin.text = "Iniciar sesión"
                }
            } else {
                tilCorreo.error = "Usuario no registrado"
                Toast.makeText(this, "Este correo no está registrado", Toast.LENGTH_SHORT).show()
                btnLogin.isEnabled = true
                btnLogin.text = "Iniciar sesión"
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            btnLogin.isEnabled = true
            btnLogin.text = "Iniciar sesión"
        }
    }

    /**
     * Hashea una contraseña usando SHA-256
     */
    private fun hashearContrasena(contrasena: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(contrasena.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun cambioActivity(activityDestino: Class<out AppCompatActivity>) {
        val intent = Intent(this, activityDestino)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

