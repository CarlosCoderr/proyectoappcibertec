package com.app.balance

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import java.math.BigDecimal

class BalanceFragment : Fragment(R.layout.fragment_balance) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etMonto = view.findViewById<EditText>(R.id.etMonto)
        val tvDivisa = view.findViewById<TextView>(R.id.divisaSeleccionada)
        val btnSiguiente = view.findViewById<MaterialButton>(R.id.btnSiguienteBalance)

        val prefs = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val codigoArg = arguments?.getString("codigoDivisa")
        val codigoPrefs = prefs.getString("DIVISA_CODIGO", null)
        val codigoDivisa = codigoArg ?: codigoPrefs

        tvDivisa.text = codigoDivisa ?: "Divisa no seleccionada"

        btnSiguiente.setOnClickListener {
            val codigo = codigoDivisa
            if (codigo.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Primero selecciona una divisa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val montoTxt = etMonto.text?.toString()?.trim().orEmpty()
            val monto = montoTxt.toBigDecimalOrNull()
            if (monto == null || monto < BigDecimal.ZERO) {
                Toast.makeText(requireContext(), "Ingresa un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString("DIVISA_CODIGO", codigo)
                .putString("BALANCE_INICIAL", monto.toPlainString())
                .apply()

            Toast.makeText(
                requireContext(),
                "$codigo $montoTxt",
                Toast.LENGTH_SHORT
            ).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DashboardFragment())
                .addToBackStack("dashboard")
                .commit()
        }
    }
}
