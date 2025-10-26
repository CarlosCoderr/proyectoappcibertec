package com.app.balance

<<<<<<< HEAD
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.DivisaDAO
import com.app.balance.data.dao.TransaccionDAO
import com.app.balance.data.dao.UsuarioDAO
import com.app.balance.ui.BalanceUpdateListener
import com.app.balance.ui.ConfiguracionFragment
import com.app.balance.ui.DashboardFragment
import com.app.balance.ui.PerfilFragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class InicioActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var tvBalance: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnEditBalance: ImageButton
    private lateinit var btnMenu: ImageButton
    private lateinit var layoutCentro: LinearLayout

    private lateinit var ivHeaderAvatar: ImageView
    private lateinit var tvHeaderName: TextView
    private lateinit var ivHeaderFlag: ImageView
    private lateinit var tvHeaderEmail: TextView

    private var codigoDivisa = ""
    private var balanceActual = 0.0

    companion object {
        private const val REQUEST_AGREGAR_GASTO = 1001
=======
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


        val legacyUserDivisaId = prefs.getInt("USER_DIVISA_ID", -1)
        if (prefs.getInt("DIVISA_ID", -1) <= 0 && legacyUserDivisaId > 0) {
            editor.putInt("DIVISA_ID", legacyUserDivisaId); changed = true
        }


        val legacySaldo = prefs.getString("SALDO_INICIAL", null)
        if (!prefs.contains("BALANCE_INICIAL") && !legacySaldo.isNullOrBlank()) {
            editor.putString("BALANCE_INICIAL", legacySaldo); changed = true
        }

        // 3) Migrar flag de bienvenida antiguo → nuevo
        if (prefs.getBoolean("welcome_shown", false) && !prefs.getBoolean("WELCOME_SHOWN", false)) {
            editor.putBoolean("WELCOME_SHOWN", true); changed = true
        }

        if (changed) editor.apply()
>>>>>>> origin/main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        initViews()
        initHeaderViews()
        setupDrawer()
        setupNavigation()
        loadBalance()
        loadHeaderData()
        setupEditBalanceButton()

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        tvBalance = findViewById(R.id.tvBalance)
        tvTotal = findViewById(R.id.tvTotal)
        btnEditBalance = findViewById(R.id.btnEditBalance)
        btnMenu = findViewById(R.id.btnMenu)
        layoutCentro = findViewById(R.id.layoutCentro)
    }

    private fun initHeaderViews() {
        val headerView = navigationView.getHeaderView(0)
        ivHeaderAvatar = headerView.findViewById(R.id.ivHeaderAvatar)
        tvHeaderName = headerView.findViewById(R.id.tvHeaderName)
        ivHeaderFlag = headerView.findViewById(R.id.ivHeaderFlag)
        tvHeaderEmail = headerView.findViewById(R.id.tvHeaderEmail)
    }

    private fun loadHeaderData() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val nombre = prefs.getString("USER_NOMBRE", "") ?: ""
        val apellido = prefs.getString("USER_APELLIDO", "") ?: ""
        tvHeaderName.text = "$nombre $apellido"

        val email = prefs.getString("USER_EMAIL", "correo@ejemplo.com") ?: "correo@ejemplo.com"
        tvHeaderEmail.text = email

        val banderaUrl = prefs.getString("DIVISA_BANDERA", "") ?: ""
        if (banderaUrl.isNotEmpty()) {
            Glide.with(this)
                .load(banderaUrl)
                .placeholder(android.R.drawable.ic_menu_mapmode)
                .error(android.R.drawable.ic_menu_mapmode)
                .into(ivHeaderFlag)
        } else {
            ivHeaderFlag.setImageResource(android.R.drawable.ic_menu_mapmode)
        }

        val fotoPerfilPath = prefs.getString("FOTO_PERFIL_PATH", null)
        if (fotoPerfilPath != null) {
            val file = File(fotoPerfilPath)
            if (file.exists()) {
                Glide.with(this)
                    .load(file)
                    .circleCrop()
                    .placeholder(R.drawable.ic_persona)
                    .error(R.drawable.ic_persona)
                    .into(ivHeaderAvatar)
            } else {
                ivHeaderAvatar.setImageResource(R.drawable.ic_persona)
            }
        } else {
            ivHeaderAvatar.setImageResource(R.drawable.ic_persona)
        }
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }
    }

    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inicio -> {
                    loadFragment(DashboardFragment())
                    mostrarHeaderBalance(true)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_perfil -> {
                    loadFragment(PerfilFragment())
                    mostrarHeaderBalance(false)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_config -> {
                    loadFragment(ConfiguracionFragment())
                    mostrarHeaderBalance(false)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_log_out -> {
                    mostrarDialogoCerrarSesion()
                    true
                }
                else -> false
            }
        }
    }

    private fun mostrarHeaderBalance(mostrar: Boolean) {
        if (mostrar) {
            layoutCentro.visibility = View.VISIBLE
            btnEditBalance.visibility = View.VISIBLE
        } else {
            layoutCentro.visibility = View.GONE
            btnEditBalance.visibility = View.GONE
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun loadBalance() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        codigoDivisa = prefs.getString("DIVISA_CODIGO", "PEN") ?: "PEN"
        val balanceString = prefs.getString("BALANCE_MONTO", "0.00") ?: "0.00"

        try {
            balanceActual = balanceString.toDouble()
        } catch (e: Exception) {
            balanceActual = 0.0
        }

        actualizarVistasBalance()
    }

    private fun actualizarVistasBalance() {
        tvTotal.text = "Ingresos"
        tvBalance.text = "$codigoDivisa ${String.format("%.2f", balanceActual)}"
    }

    private fun setupEditBalanceButton() {
        btnEditBalance.setOnClickListener {
            mostrarDialogoActualizarBalance()
        }
    }

    private fun mostrarDialogoActualizarBalance() {
        val dialogView = layoutInflater.inflate(R.layout.dialogo_actualizar_balance, null)

        val tvBalanceActual = dialogView.findViewById<TextView>(R.id.tvBalanceActual)
        val tietEditarBalance = dialogView.findViewById<TextInputEditText>(R.id.tietEditarBalance)
        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btnCancelar)
        val btnActualizar = dialogView.findViewById<MaterialButton>(R.id.btnActualizarBalance)

        tvBalanceActual.text = "$codigoDivisa ${String.format("%.2f", balanceActual)}"
        tietEditarBalance.hint = "Ingresa nuevo balance"

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(false)
            .show()

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnActualizar.setOnClickListener {
            val nuevoBalanceText = tietEditarBalance.text.toString().trim()

            if (nuevoBalanceText.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un valor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoBalanceIngresado = nuevoBalanceText.toDoubleOrNull()
            if (nuevoBalanceIngresado == null || nuevoBalanceIngresado <= 0) {
                Toast.makeText(this, "Ingresa un valor válido mayor a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val userId = prefs.getInt("USER_ID", 0)

            val dbHelper = AppDatabaseHelper(this)
            val db = dbHelper.readableDatabase
            val transaccionDAO = TransaccionDAO(db, dbHelper)

            val transacciones = transaccionDAO.obtenerTransaccionesPorUsuario(userId)
            var totalGastado = 0.0

            transacciones.forEach { transaccion ->
                totalGastado += transaccion.transaccion.monto
            }

            if (nuevoBalanceIngresado < totalGastado) {
                Toast.makeText(
                    this,
                    "El nuevo balance debe ser mayor o igual a tus gastos totales ($codigoDivisa ${String.format("%.2f", totalGastado)})",
                    Toast.LENGTH_LONG
                ).show()
                db.close()
                return@setOnClickListener
            }

            val balanceReal = nuevoBalanceIngresado - totalGastado

            prefs.edit()
                .putString("BALANCE_MONTO", balanceReal.toString())
                .putString("BALANCE_ORIGINAL", nuevoBalanceIngresado.toString())
                .apply()

            balanceActual = balanceReal
            actualizarVistasBalance()

            val usuarioDAO = UsuarioDAO(db, dbHelper)
            usuarioDAO.actualizarMontoTotal(userId, balanceReal)
            db.close()

            notificarCambioBalance(balanceReal, codigoDivisa)

            if (totalGastado > 0) {
                Toast.makeText(
                    this,
                    "Balance actualizado: $codigoDivisa ${String.format("%.2f", nuevoBalanceIngresado)}\n" +
                            "Gastos: $codigoDivisa ${String.format("%.2f", totalGastado)}\n" +
                            "Disponible: $codigoDivisa ${String.format("%.2f", balanceReal)}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Balance actualizado: $codigoDivisa ${String.format("%.2f", balanceReal)}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            dialog.dismiss()
        }
    }

    private fun notificarCambioBalance(nuevoBalance: Double, codigoDivisa: String) {
        val fragmentActual = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (fragmentActual is BalanceUpdateListener) {
            fragmentActual.onBalanceUpdated(nuevoBalance, codigoDivisa)
        }

        if (fragmentActual is DashboardFragment) {
            fragmentActual.childFragmentManager.fragments.forEach { childFragment ->
                if (childFragment is BalanceUpdateListener) {
                    childFragment.onBalanceUpdated(nuevoBalance, codigoDivisa)
                }
            }
        }
    }

    fun recargarBalance() {
        loadBalance()
        notificarCambioBalance(balanceActual, codigoDivisa)
    }

    fun recargarHeader() {
        loadHeaderData()
    }

    private fun verificarYCargarDivisaDesdeDB() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val codigoDivisaActual = prefs.getString("DIVISA_CODIGO", "")

        if (codigoDivisaActual.isNullOrEmpty() || codigoDivisaActual == "PEN") {
            val userId = prefs.getInt("USER_ID", 0)

            if (userId > 0) {
                val dbHelper = AppDatabaseHelper(this)
                val db = dbHelper.readableDatabase
                val usuarioDAO = UsuarioDAO(db, dbHelper)
                val divisaDAO = DivisaDAO(db, dbHelper)

                val usuario = usuarioDAO.obtenerUsuarioPorId(userId)

                if (usuario != null) {
                    val divisa = divisaDAO.obtenerDivisaPorId(usuario.divisaId)

                    if (divisa != null) {
                        prefs.edit()
                            .putInt("DIVISA_ID", divisa.id)
                            .putString("DIVISA_CODIGO", divisa.codigo)
                            .putString("DIVISA_NOMBRE", divisa.nombre)
                            .putString("DIVISA_BANDERA", divisa.bandera)
                            .apply()

                        codigoDivisa = divisa.codigo

                        Log.d("InicioActivity", "Divisa restaurada desde BD: ${divisa.codigo}")
                    }
                }

                db.close()
            }
        } else {
            codigoDivisa = codigoDivisaActual
        }
=======


        val isDark = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            .getBoolean("CONF_TEMA_OSCURO", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        normalizePrefs()
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)


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


        val gestureDetector = android.view.GestureDetector(
            this,
            object : android.view.GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: android.view.MotionEvent): Boolean = true
            }
        )

        rvTransacciones.addOnItemTouchListener(object : androidx.recyclerview.widget.RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: android.view.MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y) ?: return false
                if (!gestureDetector.onTouchEvent(e)) return false

                val pos = rv.getChildAdapterPosition(child)
                if (pos == androidx.recyclerview.widget.RecyclerView.NO_POSITION) return false

                val item = transaccionAdapter.currentList[pos]

                val ivDelete = child.findViewById<View>(R.id.ivDeleteTransaccion)
                if (isTouchInsideView(ivDelete, e)) {
                    com.google.android.material.dialog.MaterialAlertDialogBuilder(this@InicioActivity)
                        .setTitle("Eliminar transacción")
                        .setMessage("¿Seguro que deseas eliminar esta transacción?\nEsta acción no se puede deshacer.")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar") { _, _ ->
                            val filas = transaccionDAO.eliminarTransaccion(item.transaccion.id)
                            if (filas > 0) {
                                android.widget.Toast.makeText(
                                    this@InicioActivity,
                                    "Transacción eliminada",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                                refreshTransaccionesList()
                            } else {
                                android.widget.Toast.makeText(
                                    this@InicioActivity,
                                    "No se pudo eliminar",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .show()
                    return true
                }


                val montoFmt = java.lang.String.format(
                    java.util.Locale.getDefault(),
                    "S/ %.2f",
                    item.transaccion.monto
                )

                val iso = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val out = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                val fechaFmt = runCatching { out.format(iso.parse(item.transaccion.fecha)!!) }
                    .getOrElse { item.transaccion.fecha }

                val dialog = com.app.balance.ui.TransaccionDetalleDialogFragment.newInstance(
                    id = item.transaccion.id,
                    categoria = item.categoria.nombre,
                    montoFormateado = montoFmt,
                    fechaFormateada = fechaFmt,
                    comentario = item.transaccion.comentario
                )
                dialog.show(supportFragmentManager, "detalleTransaccion")

                return true
            }
        })



        supportFragmentManager.setFragmentResultListener(
            com.app.balance.ui.TransaccionDetalleDialogFragment.REQ_EDIT,
            this
        ) { _, bundle ->
            val id = bundle.getInt(com.app.balance.ui.TransaccionDetalleDialogFragment.KEY_ID, -1)
            if (id != -1) {

                startActivity(
                    Intent(this, TransaccionGastoActivity::class.java)
                        .putExtra("MODO_EDICION", true)
                        .putExtra("TRANSACCION_ID", id)
                )
            }
        }


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


        val header = navigationView.getHeaderView(0) ?: navigationView.inflateHeaderView(R.layout.menu_header)

        refreshHeader()


        val fab = findViewById<FloatingActionButton>(R.id.btnAnadirTransGasto)
        fab?.setOnClickListener {
            com.app.balance.ui.AccionesInicioBottomSheet()
                .show(supportFragmentManager, "acciones-inicio")
        }


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

                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
                        supportFragmentManager.beginTransaction().remove(it).commit()
                    }


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


        refreshCenteredBalanceText()
        refreshTransaccionesList()
>>>>>>> origin/main
    }

    override fun onResume() {
        super.onResume()
<<<<<<< HEAD
        verificarYCargarDivisaDesdeDB()
        recargarBalance()
        loadHeaderData()
    }

    private fun mostrarDialogoCerrarSesion() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Cerrar sesión") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val esPrimeraVez = prefs.getBoolean("ES_PRIMERA_VEZ", true)

        prefs.edit().clear().apply()

        prefs.edit()
            .putBoolean("ES_PRIMERA_VEZ", esPrimeraVez)
            .putBoolean("SESION_ACTIVA", false)
            .apply()

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
=======
        normalizePrefs()
        refreshCenteredBalanceText()
        refreshHeader()
        refreshTransaccionesList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::dbHelper.isInitialized) {
            dbHelper.close()
        }
    }

    private fun refreshCenteredBalanceText() {
        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)


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


        val texto = try {
            val normalized = rawMonto!!.replace(',', '.') // soporta "150,75"
            val number = BigDecimal(normalized)
            val nf = NumberFormat.getNumberInstance(Locale.getDefault())
            val formatted = nf.format(number)


            if (!simbolo.isNullOrBlank()) {
                "$simbolo $formatted"
            } else {
                "$formatted ${if (!codigo.isNullOrBlank()) codigo else ""}".trim()
            }
        } catch (_: Exception) {

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


    private fun refreshHeader() {
        val navigationView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigationView)
        val header = navigationView.getHeaderView(0) ?: navigationView.inflateHeaderView(R.layout.menu_header)
        val tvName  = header.findViewById<TextView>(R.id.tvHeaderNombre)
        val tvEmail = header.findViewById<TextView>(R.id.tvHeaderEmail)

        val prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        var nombre = prefs.getString("USER_NAME", "") ?: ""
        var apellido = prefs.getString("USER_LAST", "") ?: ""
        var correo = prefs.getString("USER_MAIL", "") ?: ""


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


    private fun isTouchInsideView(view: View?, e: android.view.MotionEvent): Boolean {
        if (view == null) return false
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = e.rawX
        val y = e.rawY
        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height
        return x >= left && x <= right && y >= top && y <= bottom
    }
}
>>>>>>> origin/main
