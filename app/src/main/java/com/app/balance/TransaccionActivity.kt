package com.app.balance

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.balance.entity.TipoTransaccion
import com.app.balance.repo.CategoriaRepository

class TransaccionActivity : AppCompatActivity() {

    private lateinit var rbGasto: RadioButton
    private lateinit var rbIngreso: RadioButton
    private lateinit var spCategoria: Spinner
    private lateinit var etMonto: EditText
    private lateinit var etComentario: EditText
    private lateinit var btnGuardar: Button

    private var tipoSeleccionado: TipoTransaccion = TipoTransaccion.GASTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) PRIMERO setContentView
        setContentView(R.layout.activity_transaccion)

        // 2) Luego los findViewById
        rbGasto = findViewById(R.id.rbGasto)
        rbIngreso = findViewById(R.id.rbIngreso)
        spCategoria = findViewById(R.id.spCategoria)
        etMonto = findViewById(R.id.etMonto)
        etComentario = findViewById(R.id.etComentario)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Cargar categorías iniciales (GASTO por defecto)
        cargarCategorias()

        // Cambiar categorías cuando cambie el tipo
        rbGasto.setOnClickListener {
            tipoSeleccionado = TipoTransaccion.GASTO
            cargarCategorias()
        }
        rbIngreso.setOnClickListener {
            tipoSeleccionado = TipoTransaccion.INGRESO
            cargarCategorias()
        }

        btnGuardar.setOnClickListener {
            val monto = etMonto.text.toString().trim().toDoubleOrNull()
            if (monto == null || monto <= 0.0) {
                Toast.makeText(this, "Ingresa un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoriaNombre = spCategoria.selectedItem?.toString() ?: "Sin categoría"
            val comentario = etComentario.text.toString().trim()

            // Aquí guardarías en tu TransaccionRepository si lo tienes
            Toast.makeText(
                this,
                "Guardado: $tipoSeleccionado | $categoriaNombre | S/. $monto",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }
    }

    private fun cargarCategorias() {
        val nombres = CategoriaRepository
            .listarPorTipo(tipoSeleccionado)
            .map { it.nombre }

        spCategoria.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            nombres
        )
    }
}
