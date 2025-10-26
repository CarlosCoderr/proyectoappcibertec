package com.app.balance.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.balance.CrearCategoriaActivity
import com.app.balance.R
import com.app.balance.TransaccionGastoActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccionesInicioBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_acciones_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Agregar transacción
        view.findViewById<View>(R.id.optionAgregarTransaccion).setOnClickListener {
            startActivity(Intent(requireContext(), TransaccionGastoActivity::class.java))
            dismiss()
        }

        // Crear categoría
        view.findViewById<View>(R.id.optionCrearCategoria).setOnClickListener {
            startActivity(Intent(requireContext(), CrearCategoriaActivity::class.java))
            dismiss()
        }
    }
}
