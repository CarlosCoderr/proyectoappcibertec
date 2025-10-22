package com.app.balance

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.adapters.DivisaAdapter
import com.app.balance.model.CountryCode
import com.app.balance.network.apiClient.PaisesApiClient
import com.app.balance.respondApi.repository.PaisRepository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DivisaActivity : AppCompatActivity() {

    private lateinit var etBuscar: EditText
    private lateinit var rvDivisas: RecyclerView
    private lateinit var btnSiguienteDivisa: MaterialButton
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: DivisaAdapter
    private lateinit var repository: PaisRepository

    private var todosLosPaises = mutableListOf<CountryCode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_divisa)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupAdapter()
        setupRepository()
        setupSearchListener()
        setupBoton()

        cargarPaises()
    }

    private fun initViews() {
        etBuscar = findViewById(R.id.etBuscar)
        rvDivisas = findViewById(R.id.rvDivisas)
        btnSiguienteDivisa = findViewById(R.id.btnSiguienteDivisa)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupAdapter() {
        adapter = DivisaAdapter(emptyList()) { pais ->
            Toast.makeText(
                this,
                "Seleccionaste: ${pais.nombre} (${pais.codigo})",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvDivisas.layoutManager = LinearLayoutManager(this)
        rvDivisas.adapter = adapter
    }

    private fun setupRepository() {
        val paisService = PaisesApiClient.crearServicio()
        repository = PaisRepository(paisService)
    }

    private fun setupSearchListener() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupBoton() {
        btnSiguienteDivisa.setOnClickListener {
            val paisSeleccionado = adapter.getPaisSeleccionado()
            if (paisSeleccionado == null) {
                Toast.makeText(
                    this,
                    "Por favor selecciona una divisa",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Guardar divisa en SharedPreferences
            val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            prefs.edit()
                .putInt("DIVISA_ID", paisSeleccionado.id)
                .putString("DIVISA_CODIGO", paisSeleccionado.codigo)
                .putString("DIVISA_NOMBRE", paisSeleccionado.nombre)
                .putString("DIVISA_BANDERA", paisSeleccionado.bandera)
                .apply()

            Toast.makeText(
                this,
                "Divisa guardada: ${paisSeleccionado.nombre}",
                Toast.LENGTH_SHORT
            ).show()


        }
    }

    private fun cargarPaises() {
        progressBar.visibility = ProgressBar.VISIBLE
        lifecycleScope.launch {
            val resultado = repository.cargarPaises()

            resultado.onSuccess { paises ->
                todosLosPaises.clear()
                todosLosPaises.addAll(paises)
                adapter.actualizarDatos(paises)
                progressBar.visibility = ProgressBar.GONE

                Toast.makeText(
                    this@DivisaActivity,
                    "Se cargaron ${paises.size} países",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure { error ->
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(
                    this@DivisaActivity,
                    "Error al cargar: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}