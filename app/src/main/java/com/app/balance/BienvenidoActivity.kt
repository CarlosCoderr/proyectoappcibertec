package com.app.balance

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class BienvenidoActivity : AppCompatActivity() {

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
        val alreadyShown = prefs.getBoolean("WELCOME_SHOWN", false)


        if (alreadyShown) {
            routeAccordingToSession(prefs)
            finish()
            return
        }


        prefs.edit().putBoolean("WELCOME_SHOWN", true).apply()

        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenido)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnComenzar = findViewById<MaterialButton>(R.id.btnComenzar)


        btnComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun routeAccordingToSession(prefs: android.content.SharedPreferences) {
        val isLoggedIn = prefs.getBoolean("SESION_ACTIVA", false)

        if (!isLoggedIn) {

            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            return
        }


        val divisaId = prefs.getInt("DIVISA_ID", -1)
        val divisaCodigo = prefs.getString("DIVISA_CODIGO", null)
        val hasDivisa = (divisaId > 0) || !divisaCodigo.isNullOrBlank()
        val hasMonto = !prefs.getString("BALANCE_INICIAL", null).isNullOrBlank()

        val next = when {
            !hasDivisa -> DivisaActivity::class.java
            !hasMonto  -> BalanceActivity::class.java
            else       -> InicioActivity::class.java
        }


        android.util.Log.d(
            "ROUTER_CHECK",
            "SESION_ACTIVA=$isLoggedIn, DIVISA_ID=$divisaId, BALANCE_INICIAL=${prefs.getString("BALANCE_INICIAL", null)}"
        )

        startActivity(Intent(this, next).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
