package com.app.balance.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.balance.LoginActivity
import com.app.balance.R
import com.app.balance.utils.avatarPorGenero


class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("AppPreferences", 0)

        // ==== Referencias del layout (sin cambiar tu XML) ====
        val imgAvatar      = view.findViewById<ImageView>(R.id.imgAvatar)
        val tvIniciales    = view.findViewById<TextView>(R.id.tvIniciales)
        val tvNombre       = view.findViewById<TextView>(R.id.tvNombreCompleto)
        val tvCorreo       = view.findViewById<TextView>(R.id.tvCorreo)
        val tvCelular      = view.findViewById<TextView>(R.id.tvCelular)
        val tvFechaNac     = view.findViewById<TextView>(R.id.tvFechaNac)
        val tvGenero       = view.findViewById<TextView>(R.id.tvGenero)
        val btnEditar      = view.findViewById<View>(R.id.btnEditar)
        val btnCerrarSesion= view.findViewById<Button>(R.id.btnCerrarSesion)

        // ==== Datos desde SharedPreferences (ajusta claves si tus pantallas guardan otras) ====
        val nombre   = prefs.getString("USER_NAME", "") ?: ""
        val apellido = prefs.getString("USER_LAST", "") ?: ""
        val correo   = prefs.getString("USER_MAIL", "") ?: ""
        val celular  = prefs.getString("USER_PHONE", "") ?: ""
        val fechaNac = prefs.getString("USER_BIRTH", "") ?: ""
        val genero   = prefs.getString("USER_GENDER", "") ?: ""
        val fotoUri  = prefs.getString("USER_PHOTO_URI", null) // por si guardas foto real
        // Si guardaste el resId directo:
        val avatarResPref = prefs.getInt("USER_AVATAR", 0)

        // ==== Nombre + correo ====
        val nombreCompleto = listOf(nombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
        tvNombre.text = if (nombreCompleto.isBlank()) "Usuario" else nombreCompleto
        tvCorreo.text = correo

        // ==== Iniciales (fallback cuando no hay foto ni avatar res) ====
        val iniciales = when {
            nombre.isNotBlank() || apellido.isNotBlank() ->
                (nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "") +
                        (apellido.firstOrNull()?.uppercaseChar()?.toString() ?: "")
            correo.contains("@") -> correo.first().uppercaseChar().toString()
            else -> "U"
        }
        tvIniciales.text = iniciales

        // ==== Avatar: prioridad -> foto URI > avatar guardado > avatar por género > iniciales ====
        when {
            !fotoUri.isNullOrBlank() -> {
                imgAvatar.setImageURI(Uri.parse(fotoUri))
                imgAvatar.visibility = View.VISIBLE
                tvIniciales.visibility = View.GONE
            }
            avatarResPref != 0 -> {
                imgAvatar.setImageResource(avatarResPref)
                imgAvatar.visibility = View.VISIBLE
                tvIniciales.visibility = View.GONE
            }
            genero.isNotBlank() -> {
                imgAvatar.setImageResource(avatarPorGenero(genero))
                imgAvatar.visibility = View.VISIBLE
                tvIniciales.visibility = View.GONE
            }
            else -> {
                imgAvatar.visibility = View.GONE
                tvIniciales.visibility = View.VISIBLE
            }
        }

        tvCelular.text  = "Celular: ${celular.ifBlank { "—" }}"
        tvFechaNac.text = "Fecha de Nacimiento: ${fechaNac.ifBlank { "—" }}"
        tvGenero.text   = "Género: ${genero.ifBlank { "—" }}"

        // ==== Editar
        btnEditar.setOnClickListener {
        }

        btnCerrarSesion.setOnClickListener {
            // si quieres conservar divisa y balance al cerrar sesión, guarda antes y restáuralos (como te mostré)
            prefs.edit().clear().apply()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
