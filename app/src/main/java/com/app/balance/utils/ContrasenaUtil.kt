package com.app.balance.utils

import com.app.balance.R
import java.security.MessageDigest

object ContrasenaUtil {
    fun hashear(contrasena: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(contrasena.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verificar(contrasena: String, hash: String): Boolean {
        return hashear(contrasena) == hash
    }
}
fun avatarPorGenero(genero: String?): Int = when (genero?.lowercase()) {
    "hombre", "masculino", "m" -> R.drawable.ic_hombre
    "mujer", "femenino", "f"   -> R.drawable.ic_mujer
    else                       -> R.drawable.ic_otro
}