package com.app.balance

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.adapters.DivisaAdapter
import com.app.balance.entity.Divisa
import com.app.balance.viewmodels.DivisaViewModel
import com.google.android.material.button.MaterialButton

class DivisaActivity : AppCompatActivity() {

    private lateinit var viewModel: DivisaViewModel
    private lateinit var adapter: DivisaAdapter
    private lateinit var etBuscar: EditText
    private lateinit var rvDivisas: RecyclerView
    private lateinit var btnSiguiente: MaterialButton

    private var divisaSeleccionada: Divisa? = null

    //cambio de activity

    private lateinit var btnLogin : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_divisa)

        inicializarVistas()
        configurarRecyclerView()
        configurarViewModel()
        configurarBuscador()
        configurarBotonSiguiente()

        // Cargar divisas
        viewModel.cargarDivisas()
    }

    private fun inicializarVistas() {
        etBuscar = findViewById(R.id.etBuscar)
        rvDivisas = findViewById(R.id.rvDivisas)
        btnSiguiente = findViewById(R.id.btnLogin)

    }

    private fun configurarRecyclerView() {
        adapter = DivisaAdapter(emptyList()) { divisa ->
            divisaSeleccionada = divisa
            Toast.makeText(this, "Seleccionaste: ${divisa.nombre}", Toast.LENGTH_SHORT).show()
        }

        rvDivisas.layoutManager = LinearLayoutManager(this)
        rvDivisas.adapter = adapter
    }

    private fun configurarViewModel() {
        viewModel = ViewModelProvider(this)[DivisaViewModel::class.java]

        viewModel.divisas.observe(this) { divisas ->
            adapter.actualizarDatos(divisas)
        }

        viewModel.cargando.observe(this) { cargando ->
            // Aquí puedes mostrar/ocultar un ProgressBar si lo deseas
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configurarBuscador() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun configurarBotonSiguiente() {
        btnSiguiente.setOnClickListener {
            if (divisaSeleccionada != null) {
                val resultIntent = Intent(this, BalanceActivity::class.java)
                resultIntent.putExtra("codigoDivisa", divisaSeleccionada!!.codigo)
                startActivity(resultIntent)

                // Guardar en SharedPreferences
                val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("DIVISA_CODIGO", divisaSeleccionada!!.codigo)
                    putString("DIVISA_NOMBRE", divisaSeleccionada!!.nombre)
                    apply()
                }
                Toast.makeText(this, "Divisa guardada: ${divisaSeleccionada!!.nombre}", Toast.LENGTH_SHORT).show()

                 // Cierra esta Activity
//                startActivity(resultIntent)
            } else {
                Toast.makeText(this, "Por favor selecciona una divisa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}