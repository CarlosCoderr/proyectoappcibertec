package com.app.balance

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.data.dao.CategoriaDAO
import com.app.balance.model.Categoria
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import java.io.File

class CrearCategoriaActivity : AppCompatActivity() {

    private lateinit var btnRegresar: ImageButton
    private lateinit var ivIconoSeleccionado: ImageView
    private lateinit var etNombreCategoria: TextInputEditText
    private lateinit var radioGroupTipo: RadioGroup
    private lateinit var rbNecesidad: RadioButton
    private lateinit var rbDeseo: RadioButton
    private lateinit var gridIconos: GridLayout
    private lateinit var linearLayoutColores: LinearLayout
    private lateinit var btnAnadirCategoria: MaterialButton

    private var iconoSeleccionado: Int = R.drawable.ic_comida
    private var colorSeleccionado: Int = android.R.color.holo_green_light
    private var imagenPersonalizada: String? = null
    private var usuarioId: Int = 0

    private val iconosPredeterminados = listOf(
        R.drawable.ic_comida,
        R.drawable.ic_transporte,
        R.drawable.ic_casa,
        R.drawable.ic_salud,
        R.drawable.ic_educacion,
        R.drawable.ic_entretenimiento,
        R.drawable.ic_compras,
        R.drawable.ic_otros
    )

    private val coloresDisponibles = listOf(
        android.R.color.holo_green_light,
        android.R.color.holo_blue_light,
        android.R.color.holo_red_light,
        android.R.color.white,
        android.R.color.holo_orange_light,
        android.R.color.holo_purple,
        android.R.color.darker_gray
    )

    companion object {
        private const val REQUEST_GALLERY = 100
        private const val REQUEST_CAMERA = 101
        private const val REQUEST_PERMISSIONS = 102
    }
<<<<<<< HEAD
=======
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
>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_categoria)

<<<<<<< HEAD
<<<<<<< HEAD
=======
        dbHelper = AppDatabaseHelper(this)
        database = dbHelper.writableDatabase
        categoriaDAO = CategoriaDAO(database, dbHelper)

        initViews()
        setupNombreWatcher()
        setupTipoCategoriaSelector()
        setupIconOptions()
        setupColorOptions()
        setupCreateButton()

>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)

        obtenerUsuarioId()
        initViews()
        setupIconos()
        setupColores()
        setupBotonAnadir()
        setupIconoClick()
        setupBotonRegresar()
    }

    private fun obtenerUsuarioId() {
        val prefs = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        usuarioId = prefs.getInt("USER_ID", 0)
    }

    private fun initViews() {
        btnRegresar = findViewById(R.id.btnRegresar)
        ivIconoSeleccionado = findViewById(R.id.ivIconoSeleccionado)
        etNombreCategoria = findViewById(R.id.etNombreCategoria)
        radioGroupTipo = findViewById(R.id.radioGroupTipo)
        rbNecesidad = findViewById(R.id.rbNecesidad)
        rbDeseo = findViewById(R.id.rbDeseo)
        gridIconos = findViewById(R.id.gridIconos)
        linearLayoutColores = findViewById(R.id.linearLayoutColores)
        btnAnadirCategoria = findViewById(R.id.btnAnadirCategoria)
    }

    private fun setupBotonRegresar() {
        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun setupIconos() {
        iconosPredeterminados.forEach { iconoRes ->
            val imageView = ImageView(this).apply {
                val sizeInDp = 70
                val sizeInPx = (sizeInDp * resources.displayMetrics.density).toInt()

                layoutParams = GridLayout.LayoutParams().apply {
                    width = sizeInPx
                    height = sizeInPx
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(16, 16, 16, 16)
                }
                setImageResource(iconoRes)
                setPadding(4, 24, 24, 24)
                setBackgroundResource(R.drawable.fondo_circular_solido)
                elevation = 4f
                setColorFilter(getColor(android.R.color.black), PorterDuff.Mode.SRC_IN)
                scaleType = ImageView.ScaleType.CENTER_INSIDE

                setOnClickListener {
                    iconoSeleccionado = iconoRes
                    imagenPersonalizada = null
                    actualizarIconoSeleccionado()
                }
            }
            gridIconos.addView(imageView)
        }
    }
    private fun setupColores() {
        coloresDisponibles.forEach { colorRes ->
            val colorView = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(70, 70).apply {
                    setMargins(12, 12, 12, 12)
                }
                setBackgroundResource(R.drawable.fondo_circular_solido)
                backgroundTintList = ColorStateList.valueOf(getColor(colorRes))

                setOnClickListener {
                    colorSeleccionado = colorRes
                    actualizarIconoSeleccionado()
                }
            }
            linearLayoutColores.addView(colorView)
        }
    }

    private fun setupIconoClick() {
        ivIconoSeleccionado.setOnClickListener {
            mostrarOpcionesImagen()
        }
    }

    private fun mostrarOpcionesImagen() {
        val opciones = arrayOf("Tomar foto", "Seleccionar de galería", "Cancelar")

        MaterialAlertDialogBuilder(this)
            .setTitle("Seleccionar imagen")
            .setItems(opciones) { dialog, which ->
                when (which) {
                    0 -> verificarPermisosYAbrirCamara()
                    1 -> abrirGaleria()
                    2 -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun verificarPermisosYAbrirCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSIONS
            )
        } else {
            abrirCamara()
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CAMERA)
        } else {
            Toast.makeText(this, "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_GALLERY -> {
                    data?.data?.let { uri ->
                        imagenPersonalizada = guardarImagenEnStorage(uri)

                        ivIconoSeleccionado.background = null
                        ivIconoSeleccionado.clearColorFilter()

                        Glide.with(this)
                            .load(uri)
                            .circleCrop()
                            .into(ivIconoSeleccionado)
                    }
                }
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        imagenPersonalizada = guardarBitmapEnStorage(it)

                        ivIconoSeleccionado.background = null
                        ivIconoSeleccionado.clearColorFilter()

                        Glide.with(this)
                            .load(it)
                            .circleCrop()
                            .into(ivIconoSeleccionado)
                    }
                }
            }
        }
    }
    private fun guardarImagenEnStorage(uri: Uri): String {
        val fileName = "categoria_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            file.outputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }

        } catch (e: Exception) {
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        return file.absolutePath
    }

    private fun guardarBitmapEnStorage(bitmap: Bitmap): String {
        val fileName = "categoria_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)

        file.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        }

        return file.absolutePath
    }

    private fun actualizarIconoSeleccionado() {
        if (imagenPersonalizada == null) {
            ivIconoSeleccionado.setBackgroundResource(R.drawable.fondo_circular_solido)
            ivIconoSeleccionado.setImageResource(iconoSeleccionado)
            ivIconoSeleccionado.scaleType = ImageView.ScaleType.CENTER_INSIDE
            ivIconoSeleccionado.setColorFilter(getColor(colorSeleccionado), PorterDuff.Mode.SRC_IN)
        } else {

            ivIconoSeleccionado.background = null
            ivIconoSeleccionado.clearColorFilter()

            Glide.with(this)
                .load(File(imagenPersonalizada!!))
                .circleCrop()
                .into(ivIconoSeleccionado)
        }
    }
    private fun setupBotonAnadir() {
        btnAnadirCategoria.setOnClickListener {
            val nombreCategoria = etNombreCategoria.text.toString().trim()

            if (nombreCategoria.isEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor ingresa un nombre para la categoría",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val tipoSeleccionado = when (radioGroupTipo.checkedRadioButtonId) {
                R.id.rbNecesidad -> 1
                R.id.rbDeseo -> 2
                else -> 1
            }

            guardarCategoriaSistema(nombreCategoria, tipoSeleccionado)
        }
    }

    private fun guardarCategoriaSistema(nombre: String, tipoId: Int) {
        val dbHelper = AppDatabaseHelper(this)
        val db = dbHelper.writableDatabase
        val categoriaSistemaDAO = CategoriaDAO(db, dbHelper)

        val iconoNombre = if (imagenPersonalizada != null) {
            "custom_${System.currentTimeMillis()}"
        } else {
            obtenerNombreIcono(iconoSeleccionado)
        }

        val categoria = Categoria(
            nombre = nombre,
            icono = iconoNombre,
            usuarioId = usuarioId,
            tipoCategoriaId = tipoId,
            rutaImagen = imagenPersonalizada,
            color = if (imagenPersonalizada == null) colorSeleccionado else null
        )

        val resultado = categoriaSistemaDAO.insertarCategoriaSistema(categoria)

        if (resultado > 0) {
            Toast.makeText(
                this,
                "Categoría '$nombre' creada exitosamente",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            Toast.makeText(
                this,
                "Error al crear la categoría",
                Toast.LENGTH_SHORT
            ).show()
        }

        db.close()
    }

    private fun obtenerNombreIcono(iconoRes: Int): String {
        return when (iconoRes) {
            R.drawable.ic_comida -> "comida"
            R.drawable.ic_transporte -> "transporte"
            R.drawable.ic_casa -> "casa"
            R.drawable.ic_salud -> "salud"
            R.drawable.ic_educacion -> "educacion"
            R.drawable.ic_entretenimiento -> "entretenimiento"
            R.drawable.ic_compras -> "compras"
            R.drawable.ic_otros -> "otros"
            else -> "default"
        }
    }
<<<<<<< HEAD
=======
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
>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
}