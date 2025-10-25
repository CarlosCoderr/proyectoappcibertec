package com.app.balance

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.CategoriaDAO
import com.app.balance.model.Categoria
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

class CrearCategoriaActivity : AppCompatActivity() {

    private lateinit var tilNombreCategoria: TextInputLayout
    private lateinit var tietNombreCategoria: TextInputEditText
    private lateinit var cbGasto: MaterialCheckBox
    private lateinit var cbIngreso: MaterialCheckBox
    private lateinit var glIconos: GridLayout
    private lateinit var llColores: LinearLayout
    private lateinit var btnAnadirCategoria: MaterialButton
    private lateinit var btnBack: AppCompatImageButton
    private lateinit var cvPreviewIcon: MaterialCardView
    private lateinit var ivPreviewIcon: AppCompatImageView

    private lateinit var dbHelper: AppDatabaseHelper
    private lateinit var database: SQLiteDatabase
    private lateinit var categoriaDAO: CategoriaDAO

    private var selectedTipoId: Int? = null
    private var selectedIconName: String? = null
    private var selectedIconRes: Int? = null
    private var selectedColorHex: String? = null

    private var selectedIconCard: MaterialCardView? = null
    private var selectedIconImageView: AppCompatImageView? = null
    private var selectedColorCard: MaterialCardView? = null
    private var selectedColorInt: Int? = null
    private var defaultIconColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_categoria)

        dbHelper = AppDatabaseHelper(this)
        database = dbHelper.writableDatabase
        categoriaDAO = CategoriaDAO(database, dbHelper)

        initViews()
        setupNombreWatcher()
        setupTipoCategoriaSelector()
        setupIconOptions()
        setupColorOptions()
        setupCreateButton()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::dbHelper.isInitialized) {
            dbHelper.close()
        }
    }

    private fun initViews() {
        tilNombreCategoria = findViewById(R.id.tilNombreCategoria)
        tietNombreCategoria = findViewById(R.id.tietNombreCategoria)
        cbGasto = findViewById(R.id.cbGasto)
        cbIngreso = findViewById(R.id.cbIngreso)
        glIconos = findViewById(R.id.glIconos)
        llColores = findViewById(R.id.llColores)
        btnAnadirCategoria = findViewById(R.id.btnAnadirCategoria)
        btnBack = findViewById(R.id.btnBack)
        cvPreviewIcon = findViewById(R.id.cvPreviewIcon)
        ivPreviewIcon = findViewById(R.id.ivPreviewIcon)

        defaultIconColor = ContextCompat.getColor(this, R.color.gris)
        btnBack.setOnClickListener { finish() }
        updatePreviewIcon()
    }

    private fun setupNombreWatcher() {
        tietNombreCategoria.doAfterTextChanged {
            tilNombreCategoria.error = null
        }
    }

    private fun setupTipoCategoriaSelector() {
        cbGasto.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cbIngreso.isChecked = false
                selectedTipoId = TIPO_GASTO
            } else if (!cbIngreso.isChecked) {
                selectedTipoId = null
            }
        }

        cbIngreso.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cbGasto.isChecked = false
                selectedTipoId = TIPO_INGRESO
            } else if (!cbGasto.isChecked) {
                selectedTipoId = null
            }
        }
    }

    private fun setupIconOptions() {
        val iconOptions = listOf(
            R.drawable.ic_casa,
            R.drawable.ic_billetera,
            R.drawable.ic_categoria,
            R.drawable.ic_configuracion,
            R.drawable.ic_inicio,
            R.drawable.ic_perfil,
            R.drawable.ic_persona,
            R.drawable.ic_logo
        )

        glIconos.removeAllViews()

        val iconSize = resources.getDimensionPixelSize(R.dimen.category_icon_size)
        val iconMargin = resources.getDimensionPixelSize(R.dimen.category_icon_margin)
        val iconPadding = resources.getDimensionPixelSize(R.dimen.category_icon_padding)
        val iconStroke = resources.getDimensionPixelSize(R.dimen.category_icon_stroke)
        val strokeColor = ContextCompat.getColor(this, R.color.balance)
        val backgroundColor = ContextCompat.getColor(this, R.color.light_gray)
        defaultIconColor = ContextCompat.getColor(this, R.color.gris)

        iconOptions.forEach { iconRes ->
            val layoutParams = GridLayout.LayoutParams().apply {
                width = iconSize
                height = iconSize
                setMargins(iconMargin, iconMargin, iconMargin, iconMargin)
            }

            val cardView = MaterialCardView(this).apply {
                this.layoutParams = layoutParams
                radius = iconSize / 2f
                cardElevation = 0f
                setCardBackgroundColor(backgroundColor)
                this.strokeColor = strokeColor
                strokeWidth = 0
                preventCornerOverlap = true
                useCompatPadding = false
                isClickable = true
            }

            val imageView = AppCompatImageView(this).apply {
                setImageResource(iconRes)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(iconPadding, iconPadding, iconPadding, iconPadding)
                contentDescription = getString(R.string.categoria_icono_descripcion)
                imageTintList = ColorStateList.valueOf(defaultIconColor)
            }

            cardView.addView(imageView)
            cardView.tag = iconRes
            cardView.setOnClickListener { selectIcon(cardView, iconStroke) }

            glIconos.addView(cardView)
        }
    }

    private fun selectIcon(cardView: MaterialCardView, strokeSize: Int) {
        if (selectedIconCard === cardView) {
            return
        }

        selectedIconCard?.strokeWidth = 0
        selectedIconImageView?.imageTintList = ColorStateList.valueOf(defaultIconColor)
        selectedIconCard = cardView
        cardView.strokeWidth = strokeSize

        val iconRes = cardView.tag as? Int ?: return
        selectedIconRes = iconRes
        selectedIconName = resources.getResourceEntryName(iconRes)

        val imageView = cardView.getChildAt(0) as? AppCompatImageView
        selectedIconImageView = imageView
        val colorToApply = selectedColorInt ?: defaultIconColor
        imageView?.imageTintList = ColorStateList.valueOf(colorToApply)
        updatePreviewIcon()
    }

    private fun setupColorOptions() {
        val colorOptions = listOf(
            R.color.category_red,
            R.color.category_orange,
            R.color.category_yellow,
            R.color.category_green,
            R.color.category_blue,
            R.color.category_indigo,
            R.color.category_purple,
            R.color.category_gray
        )

        llColores.removeAllViews()

        val colorSize = resources.getDimensionPixelSize(R.dimen.category_color_size)
        val colorMargin = resources.getDimensionPixelSize(R.dimen.category_color_margin)
        val colorStroke = resources.getDimensionPixelSize(R.dimen.category_color_stroke)
        val strokeColor = ContextCompat.getColor(this, R.color.balance)

        colorOptions.forEachIndexed { index, colorRes ->
            val layoutParams = LinearLayout.LayoutParams(colorSize, colorSize).apply {
                if (index < colorOptions.lastIndex) {
                    marginEnd = colorMargin
                }
            }

            val cardView = MaterialCardView(this).apply {
                this.layoutParams = layoutParams
                radius = colorSize / 2f
                cardElevation = 0f
                preventCornerOverlap = true
                useCompatPadding = false
                setCardBackgroundColor(ContextCompat.getColor(this@CrearCategoriaActivity, colorRes))
                this.strokeColor = strokeColor
                strokeWidth = 0
                isClickable = true
                contentDescription = getString(R.string.categoria_color_descripcion)
            }

            cardView.tag = colorRes
            cardView.setOnClickListener { selectColor(cardView, colorStroke) }

            llColores.addView(cardView)
        }
    }

    private fun selectColor(cardView: MaterialCardView, strokeSize: Int) {
        if (selectedColorCard === cardView) {
            return
        }

        selectedColorCard?.strokeWidth = 0
        selectedColorCard = cardView
        cardView.strokeWidth = strokeSize

        val colorRes = cardView.tag as? Int ?: return
        val colorInt = ContextCompat.getColor(this, colorRes)
        selectedColorInt = colorInt
        selectedIconImageView?.imageTintList = ColorStateList.valueOf(colorInt)
        selectedColorHex = String.format(Locale.US, "#%08X", colorInt)
        updatePreviewIcon()
    }

    private fun setupCreateButton() {
        btnAnadirCategoria.setOnClickListener {
            tilNombreCategoria.error = null

            val nombre = tietNombreCategoria.text?.toString()?.trim().orEmpty()
            var isValid = true

            if (nombre.isEmpty()) {
                tilNombreCategoria.error = getString(R.string.categoria_nombre_requerido)
                isValid = false
            }

            val tipo = selectedTipoId
            if (tipo == null) {
                Toast.makeText(this, R.string.categoria_tipo_requerido, Toast.LENGTH_SHORT).show()
                isValid = false
            }

            val icono = selectedIconName
            if (icono.isNullOrEmpty()) {
                Toast.makeText(this, R.string.categoria_icono_requerido, Toast.LENGTH_SHORT).show()
                isValid = false
            }

            val color = selectedColorHex
            if (color.isNullOrEmpty()) {
                Toast.makeText(this, R.string.categoria_color_requerido, Toast.LENGTH_SHORT).show()
                isValid = false
            }

            val userId = getUserIdFromSession()
            if (userId == null || userId <= 0) {
                Toast.makeText(this, R.string.categoria_usuario_no_encontrado, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValid || icono == null || color == null || tipo == null) {
                return@setOnClickListener
            }

            val categoria = Categoria(
                nombre = nombre,
                icono = icono,
                usuarioId = userId,
                tipoCategoriaId = tipo,
                rutaImagen = color
            )

            try {
                val resultado = categoriaDAO.insertarCategoria(categoria)
                if (resultado != -1L) {
                    Toast.makeText(this, R.string.categoria_creada_exito, Toast.LENGTH_SHORT).show()
                    setResult(
                        RESULT_OK,
                        Intent().apply {
                            putExtra("categoria_id", resultado.toInt())
                            putExtra("categoria_nombre", categoria.nombre)
                        }
                    )
                    finish()
                } else {
                    Toast.makeText(this, R.string.categoria_error_general, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, R.string.categoria_error_general, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePreviewIcon() {
        val iconRes = selectedIconRes ?: R.drawable.ic_categoria
        val tintColor = selectedColorInt ?: defaultIconColor
        ivPreviewIcon.setImageResource(iconRes)
        ivPreviewIcon.imageTintList = ColorStateList.valueOf(tintColor)
        cvPreviewIcon.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
    }

    private fun getUserIdFromSession(): Int? {
        val prefs: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        return if (prefs.contains("USER_ID")) {
            prefs.getInt("USER_ID", -1)
        } else {
            null
        }
    }

    companion object {
        private const val TIPO_GASTO = 1
        private const val TIPO_INGRESO = 2
    }
}