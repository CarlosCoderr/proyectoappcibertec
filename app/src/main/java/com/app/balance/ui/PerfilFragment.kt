package com.app.balance.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.app.balance.LoginActivity
import com.app.balance.R

class PerfilFragment : Fragment(R.layout.fragment_perfil) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("AppPreferences", 0)
            prefs.edit().clear().apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}