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

    private fun normalizePrefs() {
        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = prefs.edit()
        var changed = false

        // 1) Migrar divisa antigua → nueva
        val legacyUserDivisaId = prefs.getInt("USER_DIVISA_ID", -1)
        if (prefs.getInt("DIVISA_ID", -1) <= 0 && legacyUserDivisaId > 0) {
            editor.putInt("DIVISA_ID", legacyUserDivisaId); changed = true
        }

        // 2) Migrar saldo antiguo → nuevo
        val legacySaldo = prefs.getString("SALDO_INICIAL", null)
        if (!prefs.contains("BALANCE_INICIAL") && !legacySaldo.isNullOrBlank()) {
            editor.putString("BALANCE_INICIAL", legacySaldo); changed = true
        }

        // 3) Migrar flag de bienvenida antiguo → nuevo
        if (prefs.getBoolean("welcome_shown", false) && !prefs.getBoolean("WELCOME_SHOWN", false)) {
            editor.putBoolean("WELCOME_SHOWN", true); changed = true
        }

        if (changed) editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        normalizePrefs()

        // —— Guard de ruteo: si ya hay sesión activa, saltar al paso que falte (divisa / monto / inicio)
        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        if (prefs.getBoolean("SESION_ACTIVA", false)) {
            val hasDivisa = (prefs.getInt("DIVISA_ID", -1) > 0) || !prefs.getString("DIVISA_CODIGO", null).isNullOrBlank()

            val hasMonto  = !prefs.getString("BALANCE_INICIAL", null).isNullOrBlank()

            val next = when {
                !hasDivisa -> DivisaActivity::class.java
                !hasMonto  -> BalanceActivity::class.java
                else       -> InicioActivity::class.java
            }
            startActivity(Intent(this, next).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
            return
        }

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
                    val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                    val editor = prefs.edit()
                        .putInt("USER_ID", usuario.id)
                        .putString("USER_NOMBRE", "${usuario.nombre} ${usuario.apellido}")
                        .putString("USER_CORREO", usuario.email)
                        .putBoolean("SESION_ACTIVA", true)

                    // Si tu usuario trae divisa asignada, opcionalmente la persistimos como DIVISA_ID
                    // (Si no, DivisaActivity la pedirá y la guardará)
                    if (usuario.divisaId > 0) {
                        editor.putInt("DIVISA_ID", usuario.divisaId)
                        // Si en tu modelo no tienes símbolo/código aquí, no los toques; DivisaActivity los llenará.
                    }

                    editor.apply()

                    Toast.makeText(this, "¡Bienvenido ${usuario.nombre}!", Toast.LENGTH_SHORT).show()

                    // —— Ruteo post-login: si falta algo, pedirlo. Si está completo, ir a Inicio.
                    val hasDivisa = prefs.getInt("DIVISA_ID", -1) > 0
                    val hasMonto  = !prefs.getString("BALANCE_INICIAL", null).isNullOrBlank()

                    val next = when {
                        !hasDivisa -> DivisaActivity::class.java
                        !hasMonto  -> BalanceActivity::class.java
                        else       -> InicioActivity::class.java
                    }

                    startActivity(
                        Intent(this, next)
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
