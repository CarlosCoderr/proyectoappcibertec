package com.app.balance.repo

import com.app.balance.enity.Usuario

object UsuariosRepository {
    private val listaUsuarios = mutableListOf(
        Usuario(1, "Juan Ramon", "Deroi Tryan", "juan@gmail.com", "12-05-2000", "Hombre", "987654321", "C123456"),
        Usuario(2, "Flor Maria", "Davila Guiterrez", "flor@gmail.com", "11-07-1990", "Mujer", "957652321", "C223456"),
    )

    fun obtenerUsuarios(): List<Usuario> = listaUsuarios

    fun agregarUsuario(usuario: Usuario) {
        listaUsuarios.add(usuario)
    }

    fun obtenerSiguienteId(): Int {
        return if (UsuariosRepository.listaUsuarios.isEmpty()) 1 else UsuariosRepository.listaUsuarios.maxOf { it.codigo } + 1
    }

    fun buscarUsuario(correo: String, clave: String): Usuario? {
        return UsuariosRepository.listaUsuarios.find { it.correo == correo && it.clave == clave }
    }
}