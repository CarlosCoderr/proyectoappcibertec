package com.app.balance

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransaccionGastoActivity : AppCompatActivity() {

    private val cal = Calendar.getInstance()
    private var fechaISO: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

    // categoría (opcional en esta versión)
    private var categoriaNombre: String? = null

    // Recibir resultado desde CrearCategoriaActivity (opcionalmente nombre)
    private val crearCategoriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val nombre = result.data?.getStringExtra("categoria_nombre")
            if (!nombre.isNullOrBlank()) {
                categoriaNombre = nombre
                findViewById<TextView>(R.id.tvCategoriaSeleccionada).text = nombre
            } else {
                Toast.makeText(this, "Categoría creada.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaccion_gasto)

        // Insets (mantén tu contenedor raíz con id @id/main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // === Referencias UI ===
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val tvFechaGasto = findViewById<TextView>(R.id.tvFechaGasto)
        val etComentario = findViewById<EditText>(R.id.etComentario)
        val tvContador = findViewById<TextView>(R.id.tvContadorComentario)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnAnadirTransaccion)
        val tvCategoriaSel = findViewById<TextView>(R.id.tvCategoriaSeleccionada)
        val btnCategoria = findViewById<MaterialButton>(R.id.btnCategoria)

        // Fecha inicial
        tvFechaGasto.text =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)
        tvFechaGasto.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    fechaISO =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    tvFechaGasto.text =
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Contador comentario 0/50
        etComentario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                tvContador.text = "${s.length}/50"
            }
        })

        // Abrir “Elegir / Crear” categoría (pantalla de tu amiga)
        btnCategoria.setOnClickListener {
            val intent = Intent(this, CrearCategoriaActivity::class.java)
            crearCategoriaLauncher.launch(intent)
        }

        // Guardar (categoría OPCIONAL en esta versión)
        btnGuardar.setOnClickListener {
            val monto = etMonto.text?.toString()
                ?.replace(',', '.')?.toDoubleOrNull() ?: 0.0

            if (monto <= 0) {
                etMonto.error = "Monto inválido"
                return@setOnClickListener
            }

            val comentario = etComentario.text?.toString()?.trim().orEmpty()
            val categoria = categoriaNombre // puede ser null

            // TODO: Inserta en tu SQLite/Room:
            //  - monto (Double)
            //  - fecha = fechaISO (yyyy-MM-dd)
            //  - comentario (String)
            //  - categoriaNombre (nullable)
            //  - si luego agregas categoriaId, también guárdalo
            //  - usuarioId desde sesión (si corresponde)

            Toast.makeText(
                this,
                "Gasto guardado: S/ $monto ${if (!categoria.isNullOrBlank()) "($categoria)" else ""} $fechaISO",
                Toast.LENGTH_SHORT
            ).show()

            setResult(RESULT_OK, Intent().putExtra("saved", true))
            finish()
        }

        // Mostrar estado inicial de categoría
        tvCategoriaSel.text = categoriaNombre ?: "(sin seleccionar)"
    }
}
