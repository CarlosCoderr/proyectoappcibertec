package com.app.balance

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BalanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_balance)

        // Referencias a los elementos del layout
        val etMonto: EditText = findViewById(R.id.etMonto)
        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)

        // Acción del botón
        btnSiguiente.setOnClickListener {
            val monto = etMonto.text.toString()

            if (monto.isBlank()) {
                Toast.makeText(this, "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Balance ingresado: $monto PEN", Toast.LENGTH_SHORT).show()
            }
        }
    }
}