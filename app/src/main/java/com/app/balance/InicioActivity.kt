package com.app.balance

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class InicioActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

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
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        // Insets para el DrawerLayout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar + Toggle del Drawer (manteniendo tu estructura)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.app_name, R.string.app_name
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Mostrar el saldo centrado (si falta config, redirige)
        refreshCenteredBalanceText()
    }

    override fun onResume() {
        super.onResume()
        normalizePrefs()
        refreshCenteredBalanceText()
    }

    /**
     * Muestra el saldo actual centrado en el TextView tvSaldoActual.
     * Si falta la configuración básica (divisa o monto), redirige al paso correspondiente.
     */
    private fun refreshCenteredBalanceText() {
        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Reglas de "config básica"
        val sesionActiva = prefs.getBoolean("SESION_ACTIVA", false)
        if (!sesionActiva) {
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
            return
        }

        val divisaId = prefs.getInt("DIVISA_ID", -1)
        val divisaCodigo = prefs.getString("DIVISA_CODIGO", null)
        val hasDivisa = (divisaId > 0) || !divisaCodigo.isNullOrBlank()
        val rawMonto = prefs.getString("BALANCE_INICIAL", null)
        val hasMonto = !rawMonto.isNullOrBlank()

        if (!hasDivisa) {
            startActivity(
                Intent(this, DivisaActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
            return
        }
        if (!hasMonto) {
            startActivity(
                Intent(this, BalanceActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finish()
            return
        }

        val codigo  = prefs.getString("DIVISA_CODIGO", "")   // ej. "PEN" o "USD"
        val simbolo = prefs.getString("DIVISA_SIMBOLO", null) // si no existe, queda null (fallback al código)

        val tv = findViewById<TextView>(R.id.tvSaldoActual)

        // Normaliza posible coma decimal y da formato local
        val texto = try {
            val normalized = rawMonto!!.replace(',', '.') // soporta "150,75"
            val number = BigDecimal(normalized)
            val nf = NumberFormat.getNumberInstance(Locale.getDefault())
            val formatted = nf.format(number)

            // Si hay símbolo, lo anteponemos; si no, dejamos el código al final.
            if (!simbolo.isNullOrBlank()) {
                "$simbolo $formatted"
            } else {
                "$formatted ${if (!codigo.isNullOrBlank()) codigo else ""}".trim()
            }
        } catch (_: Exception) {
            // En caso de error, muestra crudo con fallback
            if (!simbolo.isNullOrBlank()) {
                "$simbolo ${rawMonto ?: ""}"
            } else {
                "${rawMonto ?: ""} ${if (!codigo.isNullOrBlank()) codigo else ""}".trim()
            }
        }

        tv.text = texto
    }
}
