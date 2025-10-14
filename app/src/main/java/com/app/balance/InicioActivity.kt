package com.app.balance

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class InicioActivity : AppCompatActivity() {

    private lateinit var txtGastos: TextView
    private lateinit var txtIngresos: TextView
    private lateinit var txtAhorros: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        txtGastos=findViewById(R.id.txtGastos)
        txtIngresos=findViewById(R.id.txtIngresos)
        txtAhorros=findViewById(R.id.txtAhorros)

        txtGastos.setOnClickListener {
            cargarFragment(GastosFragment())
        }

        txtIngresos.setOnClickListener {
            cargarFragment(IngresosFragment())
        }

        txtAhorros.setOnClickListener {
            cargarFragment(IngresosFragment())
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        cargarFragment(IngresosFragment())

    }

    private fun cargarFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentContenedor,fragment)
            .commit()
    }


}