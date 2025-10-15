package com.app.balance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class BalanceActivity : AppCompatActivity() {

    private lateinit var divisaSeleccionada: TextView
    private var codigoDivisa: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_balance)



        // Referencias a los elementos del layout
        val etMonto: EditText = findViewById(R.id.etMonto)
        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)
        divisaSeleccionada= findViewById(R.id.divisaSeleccionada)

        codigoDivisa = intent.getStringExtra("codigoDivisa")
        divisaSeleccionada.text = codigoDivisa


        // Acción del botón
        btnSiguiente.setOnClickListener {
            val monto = etMonto.text.toString()

            if (monto.isBlank()) {
                Toast.makeText(this, "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Balance ingresado: $monto${codigoDivisa ?:""}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}