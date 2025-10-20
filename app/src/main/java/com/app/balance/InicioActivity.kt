package com.app.balance

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class InicioActivity : AppCompatActivity() {

    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.menu_open, R.string.menu_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            val open = intent.getStringExtra("open")
            if (open == "dashboard") {
                replace(DashboardFragment())
                navView.setCheckedItem(R.id.nav_inicio)
                supportActionBar?.title = "Dashboard"
            } else {
                replace(BienvenidaFragment())
                navView.setCheckedItem(R.id.nav_inicio)
                supportActionBar?.title = "BALANCE+"
            }
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    replace(BienvenidaFragment())
                    supportActionBar?.title = "BALANCE+"
                }
                R.id.nav_config -> {
                    replace(ConfiguracionFragment())
                    supportActionBar?.title = "Configuración"
                }
                R.id.nav_perfil -> {
                    replace(PerfilFragment())
                    supportActionBar?.title = "Mi cuenta"
                }
            }
            drawer.closeDrawers()
            true
        }

        onBackPressedDispatcher.addCallback(this) {
            when {
                drawer.isDrawerOpen(GravityCompat.START) -> drawer.closeDrawer(GravityCompat.START)
                supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
                else -> finish()
            }
        }


    }

    private fun replace(f: Fragment) {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (current?.javaClass == f.javaClass) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, f)
            .commit()
    }
}

