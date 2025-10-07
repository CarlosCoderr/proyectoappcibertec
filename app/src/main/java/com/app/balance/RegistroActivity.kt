package com.app.balance

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.balance.adapters.CountryCodeAdapter
import com.app.balance.entity.CountryCode
import com.app.balance.entity.Usuario
import com.app.balance.repo.UsuariosRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {

    private lateinit var tilCorreo : TextInputLayout
    private lateinit var tietCorreo : TextInputEditText
    private lateinit var tilNombre: TextInputLayout
    private lateinit var tietNombre: TextInputEditText
    private lateinit var tilApellido: TextInputLayout
    private lateinit var tietApellido: TextInputEditText
    private lateinit var tilAnio: TextInputLayout
    private lateinit var tietAnio: TextInputEditText
    private lateinit var tilMes: TextInputLayout
    private lateinit var tietMes: TextInputEditText
    private lateinit var tilDia: TextInputLayout
    private lateinit var tietDia: TextInputEditText
    private lateinit var chkHombre: CheckBox
    private lateinit var chkMujer: CheckBox
    private lateinit var tilCelular: TextInputLayout
    private lateinit var tietCelular: TextInputEditText
    private lateinit var tilClave: TextInputLayout
    private lateinit var tietClave: TextInputEditText
    private lateinit var chkTerms: CheckBox
    private lateinit var btnRegister: MaterialButton
    private lateinit var spinnerCountry: Spinner

    private var countryCodes = mutableListOf<CountryCode>()
    private var selectedCountryCode = "+51"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupGenderCheckboxes()
        loadCountryCodes()
        setupValidation()
        setupRegisterButton()
    }

    private fun initViews() {
        tilCorreo = findViewById(R.id.tilCorreo)
        tietCorreo = findViewById(R.id.tietCorreo)
        tilNombre = findViewById(R.id.tilNombre)
        tietNombre = findViewById(R.id.tietNombre)
        tilApellido = findViewById(R.id.tilApellido)
        tietApellido = findViewById(R.id.tietApellido)
        tilAnio = findViewById(R.id.tilAnio)
        tietAnio = findViewById(R.id.tietAnio)
        tilMes = findViewById(R.id.tilMes)
        tietMes = findViewById(R.id.tietMes)
        tilDia = findViewById(R.id.tilDia)
        tietDia = findViewById(R.id.tietDia)
        chkHombre = findViewById(R.id.chkHombre)
        chkMujer = findViewById(R.id.chkMujer)
        tilCelular = findViewById(R.id.tilCelular)
        tietCelular = findViewById(R.id.tietCelular)
        tilClave = findViewById(R.id.tilClave)
        tietClave = findViewById(R.id.tietClave)
        chkTerms = findViewById(R.id.chkTerms)
        btnRegister = findViewById(R.id.btnRegister)
        spinnerCountry = findViewById(R.id.spinnerCountry)

        tilCelular.prefixText = selectedCountryCode
    }

    private fun setupGenderCheckboxes() {
        chkHombre.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chkMujer.isChecked = false
        }

        chkMujer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chkHombre.isChecked = false
        }
    }

    private fun loadCountryCodes() {
        Thread {
            try {
                val url = URL("https://restcountries.com/v3.1/all?fields=name,idd,cca2,flags")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)

                val tempList = mutableListOf<CountryCode>()

                for (i in 0 until jsonArray.length()) {
                    val country = jsonArray.getJSONObject(i)
                    val name = country.getJSONObject("name").getString("common")
                    val idd = country.getJSONObject("idd")
                    val root = idd.optString("root", "")
                    val suffixes = idd.optJSONArray("suffixes")

                    if (root.isNotEmpty() && suffixes != null && suffixes.length() > 0) {
                        val code = root + suffixes.getString(0)
                        val flag = country.getJSONObject("flags").getString("png")
                        tempList.add(CountryCode(name, code, flag))
                    }
                }

                tempList.sortBy { it.name }
                countryCodes = tempList

                runOnUiThread {
                    setupCountrySpinner()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    setupFallbackCountryCodes()
                }
            }
        }.start()
    }

    private fun setupCountrySpinner() {
        val adapter = CountryCodeAdapter(this, countryCodes)
        spinnerCountry.adapter = adapter

        val peruIndex = countryCodes.indexOfFirst { it.code == "+51" }
        if (peruIndex != -1) {
            spinnerCountry.setSelection(peruIndex)
        }

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCountryCode = countryCodes[position].code
                tilCelular.prefixText = selectedCountryCode
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFallbackCountryCodes() {
        countryCodes = mutableListOf(
            CountryCode("Perú", "+51", ""),
            CountryCode("Argentina", "+54", ""),
            CountryCode("Chile", "+56", ""),
            CountryCode("Colombia", "+57", ""),
            CountryCode("México", "+52", ""),
            CountryCode("España", "+34", ""),
            CountryCode("Estados Unidos", "+1", "")
        )
        setupCountrySpinner()
    }

    private fun setupValidation() {

        tietCorreo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateCorreo()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tietNombre.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateNombre()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tietApellido.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateApellido()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tietAnio.addTextChangedListener(createDateTextWatcher())
        tietMes.addTextChangedListener(createDateTextWatcher())
        tietDia.addTextChangedListener(createDateTextWatcher())

        tietCelular.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateCelular()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        tietClave.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateClave()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun createDateTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateFecha()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun validateNombre(): Boolean {
        val nombre = tietNombre.text.toString().trim()
        return when {
            nombre.isEmpty() -> {
                tilNombre.error = "El nombre es obligatorio"
                false
            }
            nombre.length < 2 -> {
                tilNombre.error = "El nombre debe tener al menos 2 caracteres"
                false
            }
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> {
                tilNombre.error = "El nombre solo debe contener letras"
                false
            }
            else -> {
                tilNombre.error = null
                true
            }
        }
    }

    private fun validateApellido(): Boolean {
        val apellido = tietApellido.text.toString().trim()
        return when {
            apellido.isEmpty() -> {
                tilApellido.error = "El apellido es obligatorio"
                false
            }
            apellido.length < 2 -> {
                tilApellido.error = "El apellido debe tener al menos 2 caracteres"
                false
            }
            !apellido.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> {
                tilApellido.error = "El apellido solo debe contener letras"
                false
            }
            else -> {
                tilApellido.error = null
                true
            }
        }
    }

    private fun validateFecha(): Boolean {
        val anio = tietAnio.text.toString().trim()
        val mes = tietMes.text.toString().trim()
        val dia = tietDia.text.toString().trim()

        var isValid = true

        if (anio.isEmpty()) {
            tilAnio.error = "Año requerido"
            isValid = false
        } else {
            val anioInt = anio.toIntOrNull()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            when {
                anioInt == null -> {
                    tilAnio.error = "Año inválido"
                    isValid = false
                }
                anioInt < 1900 || anioInt > currentYear -> {
                    tilAnio.error = "Año debe estar entre 1900 y $currentYear"
                    isValid = false
                }
                else -> tilAnio.error = null
            }
        }

        if (mes.isEmpty()) {
            tilMes.error = "Mes requerido"
            isValid = false
        } else {
            val mesInt = mes.toIntOrNull()
            when {
                mesInt == null -> {
                    tilMes.error = "Mes inválido"
                    isValid = false
                }
                mesInt < 1 || mesInt > 12 -> {
                    tilMes.error = "Mes debe estar entre 1 y 12"
                    isValid = false
                }
                else -> tilMes.error = null
            }
        }

        if (dia.isEmpty()) {
            tilDia.error = "Día requerido"
            isValid = false
        } else {
            val diaInt = dia.toIntOrNull()
            when {
                diaInt == null -> {
                    tilDia.error = "Día inválido"
                    isValid = false
                }
                diaInt < 1 || diaInt > 31 -> {
                    tilDia.error = "Día debe estar entre 1 y 31"
                    isValid = false
                }
                else -> tilDia.error = null
            }
        }

        if (isValid && anio.isNotEmpty() && mes.isNotEmpty() && dia.isNotEmpty()) {
            try {
                val calendar = Calendar.getInstance()
                calendar.isLenient = false
                calendar.set(anio.toInt(), mes.toInt() - 1, dia.toInt())
                calendar.time

                val today = Calendar.getInstance()
                val age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
                if (age < 13) {
                    tilAnio.error = "Debes tener al menos 13 años"
                    isValid = false
                }
            } catch (e: Exception) {
                tilDia.error = "Fecha inválida"
                isValid = false
            }
        }

        return isValid
    }

    private fun validateGenero(): Boolean {
        return if (!chkHombre.isChecked && !chkMujer.isChecked) {
            Toast.makeText(this, "Debe seleccionar un género", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun validateCelular(): Boolean {
        val celular = tietCelular.text.toString().trim()
        return when {
            celular.isEmpty() -> {
                tilCelular.error = "El celular es obligatorio"
                false
            }
            !celular.matches(Regex("^[0-9]+$")) -> {
                tilCelular.error = "El celular solo debe contener números"
                false
            }
            celular.length < 7 || celular.length > 15 -> {
                tilCelular.error = "El celular debe tener entre 7 y 15 dígitos"
                false
            }
            else -> {
                tilCelular.error = null
                true
            }
        }
    }

    private fun validateClave(): Boolean {
        val clave = tietClave.text.toString()
        return when {
            clave.isEmpty() -> {
                tilClave.error = "La contraseña es obligatoria"
                false
            }
            clave.length < 6 -> {
                tilClave.error = "La contraseña debe tener al menos 6 caracteres"
                false
            }
            !clave.any { it.isDigit() } -> {
                tilClave.error = "La contraseña debe contener al menos un número"
                false
            }
            !clave.any { it.isUpperCase() } -> {
                tilClave.error = "La contraseña debe contener al menos una mayúscula"
                false
            }
            else -> {
                tilClave.error = null
                true
            }
        }
    }

    private fun validateCorreo(): Boolean {
        val correo = tietCorreo.text.toString().trim()
        return when {
            correo.isEmpty() -> {
                tilCorreo.error = "El correo es obligatorio"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                tilCorreo.error = "El correo no tiene un formato válido"
                false
            }
            else -> {
                tilCorreo.error = null
                true
            }
        }
    }

    private fun validateTerms(): Boolean {
        return if (!chkTerms.isChecked) {
            Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun setupRegisterButton() {

        btnRegister.setOnClickListener {
            val isCorreoValid = validateCorreo()
            val isNombreValid = validateNombre()
            val isApellidoValid = validateApellido()
            val isFechaValid = validateFecha()
            val isGeneroValid = validateGenero()
            val isCelularValid = validateCelular()
            val isClaveValid = validateClave()
            val isTermsValid = validateTerms()

            if (isCorreoValid && isNombreValid && isApellidoValid && isFechaValid &&
                isGeneroValid && isCelularValid && isClaveValid && isTermsValid
            ) {
                val nombre = tietNombre.text.toString().trim()
                val apellido = tietApellido.text.toString().trim()
                val correo = tietCorreo.text.toString().trim()
                val anio = tietAnio.text.toString().trim()
                val mes = tietMes.text.toString().trim().padStart(2, '0')
                val dia = tietDia.text.toString().trim().padStart(2, '0')
                val fechaNacimiento = "$dia/$mes/$anio"
                val genero = if (chkHombre.isChecked) "Hombre" else "Mujer"
                val celular = "$selectedCountryCode${tietCelular.text.toString().trim()}"
                val clave = tietClave.text.toString()

                // Crear el nuevo usuario
                val nuevoId = UsuariosRepository.obtenerSiguienteId()
                val nuevoUsuario = Usuario(
                    codigo = nuevoId,
                    nombre = nombre,
                    apellidos = apellido,
                    correo = correo,
                    fechaNacimiento = fechaNacimiento,
                    genero = genero,
                    celular = celular,
                    clave = clave
                )

                UsuariosRepository.agregarUsuario(nuevoUsuario)

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}