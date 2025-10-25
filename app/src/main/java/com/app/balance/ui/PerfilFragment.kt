package com.app.balance.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.balance.LoginActivity
import com.app.balance.R
import com.app.balance.utils.avatarPorGenero
import java.util.Locale

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(v.paddingLeft, v.paddingTop + top, v.paddingRight, v.paddingBottom)
            insets
        }

        renderPerfil(view)

        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            val prefs = requireContext().getSharedPreferences("AppPreferences", 0)
            prefs.edit().clear().apply()
            startActivity(
                Intent(requireContext(), LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        view.findViewById<View>(R.id.btnEditar).setOnClickListener {
        }
    }

    override fun onResume() {
        super.onResume()
        view?.let { renderPerfil(it) } // refresca al volver de edición
    }

    private fun renderPerfil(root: View) {
        val prefs = requireContext().getSharedPreferences("AppPreferences", 0)

        val imgAvatar   = root.findViewById<ImageView>(R.id.imgAvatar)
        val tvIniciales = root.findViewById<TextView>(R.id.tvIniciales)
        val tvNombre    = root.findViewById<TextView>(R.id.tvNombreCompleto)
        val tvCorreo    = root.findViewById<TextView>(R.id.tvCorreo)
        val tvCelular   = root.findViewById<TextView>(R.id.tvCelular)
        val tvFechaNac  = root.findViewById<TextView>(R.id.tvFechaNac)
        val tvGenero    = root.findViewById<TextView>(R.id.tvGenero)

        // Claves nuevas
        var nombre   = prefs.getString("USER_NAME", "") ?: ""
        var apellido = prefs.getString("USER_LAST", "") ?: ""
        var correo   = prefs.getString("USER_MAIL", "") ?: ""
        val celular  = prefs.getString("USER_PHONE", "") ?: ""
        val fechaNac = prefs.getString("USER_BIRTH", "") ?: ""
        val genero   = (prefs.getString("USER_GENDER", "") ?: "").trim()
        val fotoUri  = prefs.getString("USER_PHOTO_URI", null)
        val avatarResPref = prefs.getInt("USER_AVATAR", 0)

        // Fallback por compatibilidad (claves antiguas)
        if (nombre.isBlank()) {
            val full = prefs.getString("USER_NOMBRE", "") ?: ""
            if (full.isNotBlank()) {
                nombre = full.substringBefore(" ")
                apellido = full.substringAfter(" ", "")
            }
        }
        if (correo.isBlank()) {
            correo = prefs.getString("USER_CORREO", "") ?: ""
        }

        // Nombre y correo
        val nombreCompleto = listOf(nombre, apellido).filter { it.isNotBlank() }.joinToString(" ")
        tvNombre.text = if (nombreCompleto.isBlank()) "Usuario" else nombreCompleto
        tvCorreo.text = correo

        // Iniciales (fallback si no hay avatar/foto)
        val iniciales = when {
            nombreCompleto.isNotBlank() -> {
                val n = nombre.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                val a = apellido.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                (n + a).ifBlank { "U" }
            }
            correo.contains("@") -> correo.first().uppercaseChar().toString()
            else -> "U"
        }
        tvIniciales.text = iniciales

        // Avatar: foto > avatar guardado > por género > iniciales
        when {
            !fotoUri.isNullOrBlank() -> {
                try {
                    imgAvatar.setImageURI(Uri.parse(fotoUri))
                    imgAvatar.imageTintList = null
                    imgAvatar.visibility = View.VISIBLE
                    tvIniciales.visibility = View.GONE
                } catch (_: Exception) {
                    // si falla la uri, cae al siguiente caso
                    usarAvatarPorPreferencias(imgAvatar, tvIniciales, avatarResPref, genero)
                }
            }
            else -> {
                usarAvatarPorPreferencias(imgAvatar, tvIniciales, avatarResPref, genero)
            }
        }

        tvCelular.text  = "Celular: ${celular.ifBlank { "—" }}"
        tvFechaNac.text = "Fecha de Nacimiento: ${fechaNac.ifBlank { "—" }}"
        tvGenero.text   = "Género: ${genero.ifBlank { "—" }}"
    }

    private fun usarAvatarPorPreferencias(
        imgAvatar: ImageView,
        tvIniciales: TextView,
        avatarResPref: Int,
        genero: String
    ) {
        when {
            avatarResPref != 0 -> {
                imgAvatar.setImageResource(avatarResPref)
                imgAvatar.imageTintList = null
                imgAvatar.visibility = View.VISIBLE
                tvIniciales.visibility = View.GONE
            }
            genero.isNotEmpty() -> {
                val res = avatarPorGenero(genero.lowercase(Locale.getDefault()))
                if (res != 0) {
                    imgAvatar.setImageResource(res)
                    imgAvatar.imageTintList = null
                    imgAvatar.visibility = View.VISIBLE
                    tvIniciales.visibility = View.GONE
                } else {
                    imgAvatar.visibility = View.GONE
                    tvIniciales.visibility = View.VISIBLE
                }
            }
            else -> {
                imgAvatar.visibility = View.GONE
                tvIniciales.visibility = View.VISIBLE
            }
        }
    }
}
