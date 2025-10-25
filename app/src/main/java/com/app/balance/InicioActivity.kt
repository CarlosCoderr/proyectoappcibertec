package com.app.balance

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.adapters.TransaccionSimpleAdapter
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.TransaccionDAO
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class InicioActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var homeScroll: NestedScrollView
    private lateinit var tvListadoTransacciones: TextView
    private lateinit var rvTransacciones: RecyclerView
    private lateinit var transaccionAdapter: TransaccionSimpleAdapter
    private lateinit var dbHelper: AppDatabaseHelper
    private lateinit var transaccionDAO: TransaccionDAO
    private var userId: Int = -1

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

        // aplicar tema noche en configuracion
        val isDark = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            .getBoolean("CONF_TEMA_OSCURO", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        normalizePrefs()
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        // Toolbar + Toggle del Drawer (manteniendo tu estructura)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigationView)

        homeScroll = findViewById(R.id.homeScroll)
        tvListadoTransacciones = findViewById(R.id.tvListadoTransaccionesInicio)
        rvTransacciones = findViewById(R.id.rvTransaccionesInicio)

        dbHelper = AppDatabaseHelper(this)
        val database = dbHelper.writableDatabase
        transaccionDAO = TransaccionDAO(database, dbHelper)
        userId = getUserIdFromSession() ?: -1

        transaccionAdapter = TransaccionSimpleAdapter()
        rvTransacciones.apply {
            layoutManager = LinearLayoutManager(this@InicioActivity)
            adapter = transaccionAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }


    // Aplica insets SOLO al contenedor de contenido
        val fragmentContainer: View? = findViewById(R.id.fragmentContainer)
        fragmentContainer?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bars.bottom)
                insets
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(homeScroll) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bars.bottom)
            insets
        }


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.app_name, R.string.app_name
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.navigationIcon?.setTint(getColor(R.color.black))

        // Header seguro
        val header = navigationView.getHeaderView(0) ?: navigationView.inflateHeaderView(R.layout.menu_header)
        // Inicializa header una vez
        refreshHeader()

        // ingresa a transaccion activity
        val fab = findViewById<FloatingActionButton>(R.id.btnAnadirTransGasto)
        fab?.setOnClickListener {
            startActivity(Intent(this, TransaccionGastoActivity::class.java))
        }

        // ===== INICIO COMO ACTIVITY (sin fragmentos encima) =====
        val currentFrag = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFrag != null) {
            supportFragmentManager.beginTransaction()
                .remove(currentFrag)
                .commit()
        }
        fragmentContainer?.visibility = View.GONE
        homeScroll.visibility = View.VISIBLE
        // Inicio SIN título
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = ""
        toolbar.subtitle = null

        // ===== Navegación del Drawer =====
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_inicio -> {
                    // Limpia el back stack y quita fragment del contenedor
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
                        supportFragmentManager.beginTransaction().remove(it).commit()
                    }

                    // Inicio SIN título (y muestra el FAB)
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    toolbar.title = ""
                    toolbar.subtitle = null
                    fab?.show()
                    homeScroll.visibility = View.VISIBLE
                    fragmentContainer?.visibility = View.GONE

                    item.isChecked = true
                }

                R.id.nav_config -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, com.app.balance.ui.ConfiguracionFragment())
                        .addToBackStack(null)
                        .commit()

                    // Mostrar título solo aquí
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    toolbar.title = "Configuración"
                    toolbar.subtitle = null
                    fab?.hide()
                    homeScroll.visibility = View.GONE
                    fragmentContainer?.visibility = View.VISIBLE

                    item.isChecked = true
                }

                R.id.nav_perfil -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, com.app.balance.ui.PerfilFragment())
                        .addToBackStack(null)
                        .commit()

                    // Mostrar título solo aquí
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    toolbar.title = "Perfil"
                    toolbar.subtitle = null
                    fab?.hide()
                    homeScroll.visibility = View.GONE
                    fragmentContainer?.visibility = View.VISIBLE

                    item.isChecked = true
                }
            }

            drawerLayout.closeDrawers()
            true
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val hasFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) != null
            if (hasFragment) {
                homeScroll.visibility = View.GONE
                fragmentContainer?.visibility = View.VISIBLE
                fab?.hide()
            } else {
                homeScroll.visibility = View.VISIBLE
                fragmentContainer?.visibility = View.GONE
                fab?.show()
            }
        }

        // Mostrar el saldo centrado
        refreshCenteredBalanceText()
        refreshTransaccionesList()
    }

    override fun onResume() {
        super.onResume()
        normalizePrefs()
        refreshCenteredBalanceText()
        refreshHeader() // refresca el header al volver de Perfil/Registro/Divisa
        refreshTransaccionesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::dbHelper.isInitialized) {
            dbHelper.close()
        }
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

    private fun refreshTransaccionesList() {
        if (!this::transaccionAdapter.isInitialized) return

        if (userId <= 0) {
            userId = getUserIdFromSession() ?: -1
        }

        val transacciones = if (userId > 0) {
            transaccionDAO.obtenerTransaccionesPorUsuario(userId)
        } else {
            emptyList()
        }

        transaccionAdapter.submitList(transacciones)
        val hasItems = transacciones.isNotEmpty()
        tvListadoTransacciones.visibility = if (hasItems) View.VISIBLE else View.GONE
        rvTransacciones.visibility = if (hasItems) View.VISIBLE else View.GONE
    }

    private fun getUserIdFromSession(): Int? {
        val prefs: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return if (prefs.contains("USER_ID")) {
            prefs.getInt("USER_ID", -1).takeIf { it > 0 }
        } else {
            null
        }
    }

    // ===== Header: Hola, nombre + correo =====
    private fun refreshHeader() {
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigationView)
        val header = navigationView.getHeaderView(0) ?: navigationView.inflateHeaderView(R.layout.menu_header)
        val tvName  = header.findViewById<TextView>(R.id.tvHeaderNombre)
        val tvEmail = header.findViewById<TextView>(R.id.tvHeaderEmail)

        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        var nombre = prefs.getString("USER_NAME", "") ?: ""
        var apellido = prefs.getString("USER_LAST", "") ?: ""
        var correo = prefs.getString("USER_MAIL", "") ?: ""

        // fallbacks por compatibilidad con claves viejas
        if (nombre.isBlank()) {
            val full = prefs.getString("USER_NOMBRE", "") ?: ""
            if (full.isNotBlank()) {
                nombre = full.substringBefore(" ")
                apellido = full.substringAfter(" ", "")
            }
        }
        if (correo.isBlank()) correo = prefs.getString("USER_CORREO", "") ?: ""

        val full = listOf(nombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
        tvName.text  = if (full.isNotBlank()) "Hola, $full" else "Hola usuari@"
        tvEmail.text = correo
    }
}
