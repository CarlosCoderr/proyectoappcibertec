package com.app.balance

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.adapters.CategoriaCarouselAdapter
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.CategoriaDAO
import com.app.balance.data.dao.TransaccionDAO
import com.app.balance.model.Categoria
import com.app.balance.model.Transaccion
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransaccionGastoActivity : AppCompatActivity() {


    companion object {
        const val EXTRA_MODO_EDICION = "MODO_EDICION"
        const val EXTRA_TRANSACCION_ID = "TRANSACCION_ID"
    }
    private var isEditMode = false
    private var editingId: Int = -1

    private val cal = Calendar.getInstance()
    private var fechaISO: String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

    private lateinit var dbHelper: AppDatabaseHelper
    private lateinit var categoriaDAO: CategoriaDAO
    private lateinit var transaccionDAO: TransaccionDAO
    private var userId: Int = -1

    private var categoriaSeleccionada: Categoria? = null
    private lateinit var categoriaAdapter: CategoriaCarouselAdapter

    private val crearCategoriaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val nuevaCategoriaId = result.data?.getIntExtra("categoria_id", -1)
            loadCategorias(nuevaCategoriaId?.takeIf { it > 0 })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaccion_gasto)

        dbHelper = AppDatabaseHelper(this)
        val database = dbHelper.writableDatabase
        categoriaDAO = CategoriaDAO(database, dbHelper)
        transaccionDAO = TransaccionDAO(database, dbHelper)

        userId = getUserIdFromSession() ?: run {
            Toast.makeText(this, R.string.categoria_usuario_no_encontrado, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Insets (mantén tu contenedor raíz con id @id/main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // === Referencias UI ===
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val tvFechaGasto = findViewById<TextView>(R.id.tvFechaGasto)
        val etComentario = findViewById<EditText>(R.id.etComentario)
        val tvContador = findViewById<TextView>(R.id.tvContadorComentario)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnAnadirTransaccion)
        val tvCategoriaSel = findViewById<TextView>(R.id.tvCategoriaSeleccionada)
        val rvCategorias = findViewById<RecyclerView>(R.id.rvCategorias)

        categoriaAdapter = CategoriaCarouselAdapter(
            onCategoriaSelected = { categoria ->
                categoriaSeleccionada = categoria
                tvCategoriaSel.text = categoria.nombre
                tvCategoriaSel.setTextColor(ContextCompat.getColor(this, R.color.black))
            },
            onAddCategoria = {
                val intent = Intent(this, CrearCategoriaActivity::class.java)
                crearCategoriaLauncher.launch(intent)
            }
        )

        rvCategorias.apply {
            layoutManager = LinearLayoutManager(this@TransaccionGastoActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriaAdapter
        }

        loadCategorias()


        isEditMode = intent?.getBooleanExtra(EXTRA_MODO_EDICION, false) == true
        editingId = intent?.getIntExtra(EXTRA_TRANSACCION_ID, -1) ?: -1


        if (!isEditMode) {
            tvFechaGasto.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)
        }


        tvFechaGasto.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    fechaISO = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    tvFechaGasto.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.time)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        etComentario.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                tvContador.text = "${s.length}/50"
            }
        })


        if (isEditMode && editingId > 0) {

            (btnGuardar as? MaterialButton)?.text = "Guardar cambios"

            val detalle = transaccionDAO.obtenerTransaccionPorId(editingId)
            if (detalle != null) {
                // Monto
                etMonto.setText(
                    String.format(Locale.getDefault(), "%.2f", detalle.transaccion.monto)
                )

                detalle.transaccion.fecha?.let { iso ->
                    fechaISO = iso

                    val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outFmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val show = runCatching { outFmt.format(inFmt.parse(iso)!!) }.getOrElse { iso }
                    tvFechaGasto.text = show


                    runCatching {
                        cal.time = inFmt.parse(iso)!!
                    }
                }

                etComentario.setText(detalle.transaccion.comentario ?: "")


                loadCategorias(detalle.transaccion.categoriaId)
            } else {
                Toast.makeText(this, "No se pudo cargar la transacción", Toast.LENGTH_SHORT).show()
            }
        }

        // Guardar transacción (crear o actualizar)
        btnGuardar.setOnClickListener {
            val monto = etMonto.text?.toString()
                ?.replace(',', '.')?.toDoubleOrNull() ?: 0.0

            if (monto <= 0) {
                etMonto.error = "Monto inválido"
                return@setOnClickListener
            }

            val comentario = etComentario.text?.toString()?.trim().orEmpty()
            val categoria = categoriaSeleccionada

            if (categoria == null) {
                Toast.makeText(this, R.string.transaccion_categoria_requerida, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode && editingId > 0) {
                // ==== UPDATE ====
                val filas = transaccionDAO.actualizarTransaccion(
                    Transaccion(
                        id = editingId,
                        categoriaId = categoria.id,
                        monto = monto,
                        fecha = fechaISO, // en ISO para DB
                        comentario = comentario.takeIf { it.isNotEmpty() },
                        usuarioId = userId
                    )
                )
                if (filas > 0) {
                    Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, Intent().putExtra("updated", true))
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
                }
            } else {
                // ==== INSERT ====
                val resultado = transaccionDAO.insertarTransaccion(
                    Transaccion(
                        categoriaId = categoria.id,
                        monto = monto,
                        fecha = fechaISO,
                        comentario = comentario.takeIf { it.isNotEmpty() },
                        usuarioId = userId
                    )
                )

                if (resultado != -1L) {
                    Toast.makeText(this, R.string.transaccion_guardada_exito, Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, Intent().putExtra("saved", true))
                    finish()
                } else {
                    Toast.makeText(this, R.string.transaccion_guardada_error, Toast.LENGTH_SHORT).show()
                }
            }
        }


        if (!isEditMode) {
            tvCategoriaSel.text = getString(R.string.transaccion_categoria_placeholder)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::dbHelper.isInitialized) {
            dbHelper.close()
        }
    }

    private fun loadCategorias(preselectId: Int? = null) {
        val categorias = categoriaDAO.obtenerCategoriasPorUsuario(userId)
        categoriaAdapter.submitList(categorias)

        val targetId = preselectId ?: categoriaSeleccionada?.id
        if (targetId != null) {
            val categoria = categorias.firstOrNull { it.id == targetId }
            if (categoria != null) {
                categoriaSeleccionada = categoria
                categoriaAdapter.setSelectedCategoria(categoria.id)
                val tvCategoriaSel = findViewById<TextView>(R.id.tvCategoriaSeleccionada)
                tvCategoriaSel.text = categoria.nombre
                tvCategoriaSel.setTextColor(ContextCompat.getColor(this, R.color.black))
                return
            }
        }

        categoriaSeleccionada = null
        categoriaAdapter.setSelectedCategoria(null)
        val tvCategoriaSel = findViewById<TextView>(R.id.tvCategoriaSeleccionada)
        tvCategoriaSel.text = getString(R.string.transaccion_categoria_placeholder)
        tvCategoriaSel.setTextColor(ContextCompat.getColor(this, R.color.gris))
    }

    private fun getUserIdFromSession(): Int? {
        val prefs: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return if (prefs.contains("USER_ID")) {
            prefs.getInt("USER_ID", -1).takeIf { it > 0 }
        } else {
            null
        }
    }
}
