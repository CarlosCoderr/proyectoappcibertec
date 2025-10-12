package com.app.balance

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.balance.entity.TipoCategoria
import com.app.balance.entity.TipoTransaccion
import com.app.balance.entity.Transaccion
import com.app.balance.repo.CategoriaRepository
import com.app.balance.repo.TransaccionRepository
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class TransaccionActivity : AppCompatActivity() {

    private lateinit var tabTipo: TabLayout
    private lateinit var etMonto: EditText
    private lateinit var grid: GridLayout
    private lateinit var chipHoy: Chip
    private lateinit var chipAyer: Chip
    private lateinit var chipAntes: Chip
    private lateinit var chipOtro: Chip
    private lateinit var etComentario: EditText
    private lateinit var btnAnadir: Button

    private var tipoActual: TipoTransaccion = TipoTransaccion.GASTO
    private var categoriaSeleccionadaId: Int? = null
    private var categoriaSeleccionadaNombre: String = "Sin categoría"
    private var categoriaSeleccionadaColor: Int? = null
    private var fechaSeleccionada: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaccion)

        tabTipo = findViewById(R.id.tabTipo)
        etMonto = findViewById(R.id.etMonto)
        grid = findViewById(R.id.gridCategorias)
        chipHoy = findViewById(R.id.chipHoy)
        chipAyer = findViewById(R.id.chipAyer)
        chipAntes = findViewById(R.id.chipAntes)
        chipOtro = findViewById(R.id.chipOtro)
        etComentario = findViewById(R.id.etComentario)
        btnAnadir = findViewById(R.id.btnAnadir)

        // GASTOS / INGRESOS
        if (tabTipo.tabCount == 0) {
            tabTipo.addTab(tabTipo.newTab().setText("GASTOS"))
            tabTipo.addTab(tabTipo.newTab().setText("INGRESOS"))
        }
        tabTipo.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tipoActual = if (tab.position == 0) TipoTransaccion.GASTO else TipoTransaccion.INGRESO
                pintarCategorias()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        fun setChipTexts(base: LocalDate = LocalDate.now()) {
            chipHoy.text = "${base.dayOfMonth}/${base.monthValue}\nHoy"
            chipAyer.text = "${base.minusDays(1).dayOfMonth}/${base.minusDays(1).monthValue}\nAyer"
            chipAntes.text = "${base.minusDays(2).dayOfMonth}/${base.minusDays(2).monthValue}\nHace dos días"
        }
        setChipTexts(LocalDate.now())
        chipHoy.setOnClickListener { fechaSeleccionada = LocalDate.now() }
        chipAyer.setOnClickListener { fechaSeleccionada = LocalDate.now().minusDays(1) }
        chipAntes.setOnClickListener { fechaSeleccionada = LocalDate.now().minusDays(2) }
        chipOtro.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Elige una fecha").build()
            picker.addOnPositiveButtonClickListener { millis ->
                val d = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                fechaSeleccionada = d
                chipOtro.text = "${d.dayOfMonth}/${d.monthValue}"
                chipOtro.isChecked = true
            }
            picker.show(supportFragmentManager, "fecha")
        }

        // Pintar categorías
        pintarCategorias()

        // Crear categoría (último círculo)
        // Nota: en tu layout el último ítem tiene id imgCrearCat
        findViewById<ImageView>(R.id.imgCrearCat)?.setOnClickListener {
            CategoriaDialogFragment { nuevaId ->
                // tras crear, refrescar y preseleccionar
                pintarCategorias(preselectId = nuevaId)
            }.show(supportFragmentManager, "catDialog")
        }

        // Guardar
        btnAnadir.setOnClickListener {
            val monto = etMonto.text?.toString()?.replace(",", ".")?.toDoubleOrNull()
            if (monto == null || monto <= 0.0) {
                Toast.makeText(this, "Ingresa un monto válido", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            val t = Transaccion(
                id = 0, // autoincrementa en repositorio
                monto = monto,
                tipo = tipoActual,
                categoriaId = categoriaSeleccionadaId,
                categoriaNombre = categoriaSeleccionadaNombre,
                categoriaColor = categoriaSeleccionadaColor,
                fecha = fechaSeleccionada,
                comentario = etComentario.text?.toString().orEmpty()
            )
            TransaccionRepository.agregar(t)
            Toast.makeText(this, "Transacción añadida", Toast.LENGTH_SHORT).show()
            finish() // vuelve al dashboard
        }
    }

    private fun pintarCategorias(preselectId: Int? = null) {
        grid.removeAllViews()
        val tipoCat = if (tipoActual == TipoTransaccion.GASTO) TipoCategoria.GASTO else TipoCategoria.INGRESO
        val cats = CategoriaRepository.listar(tipoCat).take(5) // 5 + 1 crear = 6
        val inflater = layoutInflater

        fun addItem(nombre: String, id: Int?, color: Int?) {
            val item = inflater.inflate(R.layout.item_categoria, grid, false)
            val img = item.findViewById<ImageView>(R.id.imgCat)
            val txt = item.findViewById<TextView>(R.id.txtCat)
            txt.text = nombre
            if (color != null) img.setColorFilter(color)

            item.setOnClickListener {
                categoriaSeleccionadaId = id
                categoriaSeleccionadaNombre = nombre
                categoriaSeleccionadaColor = color
                Toast.makeText(this, "Categoría: $nombre", Toast.LENGTH_SHORT).show()
            }
            grid.addView(item)
        }

        cats.forEach { addItem(it.nombre, it.id, it.color) }
        if (preselectId != null) {
            val nueva = CategoriaRepository.listar(tipoCat).firstOrNull { it.id == preselectId }
            if (nueva != null) {
                categoriaSeleccionadaId = nueva.id
                categoriaSeleccionadaNombre = nueva.nombre
                categoriaSeleccionadaColor = nueva.color
            }
        }
    }
}
