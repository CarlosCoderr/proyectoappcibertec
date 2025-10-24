package com.app.balance

import android.content.Context
import android.content.Intent
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
import com.app.balance.model.Divisa
import com.app.balance.network.apiClient.PaisesApiClientDivisa
import com.app.balance.network.apiClient.PaisesApiClientRegistro
import com.app.balance.respondApi.repository.DivisaRepository
import com.app.balance.respondApi.repository.PaisRepositoryRegistro
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DivisaActivity : AppCompatActivity() {

    private lateinit var etBuscar: EditText
    private lateinit var rvDivisas: RecyclerView
    private lateinit var btnSiguienteDivisa: MaterialButton
    private lateinit var progressBar: ProgressBar

    private lateinit var adapter: DivisaAdapter
    private lateinit var repository: DivisaRepository

    private var todasLasDivisas = mutableListOf<Divisa>()

    // —— Normaliza claves legacy por si algún compañero aún escribe con nombres antiguos
    private fun normalizePrefs() {
        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = prefs.edit()
        var changed = false

        val legacyUserDivisaId = prefs.getInt("USER_DIVISA_ID", -1)
        if (prefs.getInt("DIVISA_ID", -1) <= 0 && legacyUserDivisaId > 0) {
            editor.putInt("DIVISA_ID", legacyUserDivisaId); changed = true
        }

        val legacySaldo = prefs.getString("SALDO_INICIAL", null)
        if (!prefs.contains("BALANCE_INICIAL") && !legacySaldo.isNullOrBlank()) {
            editor.putString("BALANCE_INICIAL", legacySaldo); changed = true
        }

        if (prefs.getBoolean("welcome_shown", false) && !prefs.getBoolean("WELCOME_SHOWN", false)) {
            editor.putBoolean("WELCOME_SHOWN", true); changed = true
        }

        if (changed) editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        normalizePrefs()

        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        val userId = prefs.getInt("USER_ID", -1)
        val userCorreo = prefs.getString("USER_CORREO", null)
        val sesionActiva = prefs.getBoolean("SESION_ACTIVA", false)

        // —— Defensa de sesión
        if (!sesionActiva || userId == -1 || userCorreo.isNullOrBlank()) {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
            return
        }

        // —— Guard de ruteo: si ya hay divisa y falta monto → ir a Balance; si todo está, ir a Inicio
        val divisaId = prefs.getInt("DIVISA_ID", -1)
        val divisaCodigo = prefs.getString("DIVISA_CODIGO", null)
        val hasDivisa = (divisaId > 0) || !divisaCodigo.isNullOrBlank()
        val hasMonto  = !prefs.getString("BALANCE_INICIAL", null).isNullOrBlank()
        if (hasDivisa && !hasMonto) { /* ir a Balance */ }
        if (hasDivisa && hasMonto)  { /* ir a Inicio   */ }

        // —— UI
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
        cargarDivisas()
    }

    private fun initViews() {
        etBuscar = findViewById(R.id.etBuscar)
        rvDivisas = findViewById(R.id.rvDivisas)
        btnSiguienteDivisa = findViewById(R.id.btnSiguienteDivisa)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupAdapter() {
        adapter = DivisaAdapter(emptyList()) { divisa ->
            Toast.makeText(
                this,
                "Seleccionaste: ${divisa.nombre} (${divisa.codigo})",
                Toast.LENGTH_SHORT
            ).show()
        }
        rvDivisas.layoutManager = LinearLayoutManager(this)
        rvDivisas.adapter = adapter
    }

    private fun setupRepository() {
        val divisaService = PaisesApiClientDivisa.crearServicio()
        repository = DivisaRepository(divisaService)
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
            val divisaSeleccionada = adapter.getDivisaSeleccionada()
            if (divisaSeleccionada == null) {
                Toast.makeText(
                    this,
                    "Por favor selecciona una divisa",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // —— Guardar divisa en SharedPreferences y forzar reingreso de monto
            val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            prefs.edit()
                .putInt("DIVISA_ID", divisaSeleccionada.id)           // puede ser 0; no pasa nada con la regla nueva
                .putString("DIVISA_CODIGO", divisaSeleccionada.codigo) // ← CLAVE para fallback
                .putString("DIVISA_NOMBRE", divisaSeleccionada.nombre)
                .putString("DIVISA_BANDERA", divisaSeleccionada.bandera)
                .remove("BALANCE_INICIAL")
                .apply()

            startActivity(Intent(this, BalanceActivity::class.java))
            finish()
        }
    }

    private fun cargarDivisas() {
        progressBar.visibility = ProgressBar.VISIBLE
        lifecycleScope.launch {
            val resultado = repository.cargarDivisas()

            resultado.onSuccess { divisas ->
                todasLasDivisas.clear()
                todasLasDivisas.addAll(divisas)
                adapter.actualizarDatos(divisas)
                progressBar.visibility = ProgressBar.GONE

                Toast.makeText(
                    this@DivisaActivity,
                    "Se cargaron ${divisas.size} divisas",
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

    private fun navigateToBalance() {
        val intent = Intent(this, BalanceActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
