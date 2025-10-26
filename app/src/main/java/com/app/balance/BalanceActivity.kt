package com.app.balance

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.math.BigDecimal

class BalanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_balance)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val etMonto = findViewById<EditText>(R.id.etMonto)
        val tvDivisaSeleccionada = findViewById<TextView>(R.id.divisaSeleccionada)
        val btnSiguiente = findViewById<MaterialButton>(R.id.btnSiguienteBalance)


        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val codigo = prefs.getString("DIVISA_CODIGO", null)
        val nombre = prefs.getString("DIVISA_NOMBRE", null)

        tvDivisaSeleccionada.text = when {
            !codigo.isNullOrBlank() && !nombre.isNullOrBlank() -> "$codigo - $nombre"
            !codigo.isNullOrBlank() -> codigo
            !nombre.isNullOrBlank() -> nombre
            else -> "Divisa no seleccionada"
        }


        etMonto.addTextChangedListener(object : android.text.TextWatcher {
            private var selfChange = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: android.text.Editable?) {
                if (selfChange || editable.isNullOrEmpty()) return
                val text = editable.toString()


                if (text.length > 1 && text.startsWith("0") && !text.startsWith("0.")) {
                    val cleaned = text.replaceFirst(Regex("^0+"), "").ifEmpty { "0" }
                    selfChange = true
                    editable.replace(0, editable.length, cleaned)
                    selfChange = false
                }
            }
        })


        btnSiguiente.setOnClickListener {
            val input = etMonto.text?.toString()?.trim().orEmpty()

            val normalized = input.replace(',', '.')

            val montoValido = try {
                val valor = BigDecimal(normalized)
                valor > BigDecimal.ZERO
            } catch (_: Exception) {
                false
            }

            if (!montoValido) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Monto requerido")
                    .setMessage("Debes agregar un monto válido para poder empezar el app.")
                    .setPositiveButton("Entendido", null)
                    .show()
                return@setOnClickListener
            }

            getSharedPreferences("AppPreferences", MODE_PRIVATE).edit()
                .putString("BALANCE_INICIAL", normalized)
                .commit()

            startActivity(Intent(this, InicioActivity::class.java))
            finish()
        }
    }
}
