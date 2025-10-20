package com.app.balance

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.adapters.DivisaAdapter
import com.app.balance.entity.Divisa
import com.app.balance.viewmodels.DivisaViewModel
import com.google.android.material.button.MaterialButton

class DivisaFragment : Fragment(R.layout.fragment_divisa) {

    private lateinit var viewModel: DivisaViewModel
    private lateinit var adapter: DivisaAdapter
    private lateinit var etBuscar: EditText
    private lateinit var rvDivisas: RecyclerView
    private lateinit var btnSiguienteDivisa: MaterialButton

    private var divisaSeleccionada: Divisa? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etBuscar = view.findViewById(R.id.etBuscar)
        rvDivisas = view.findViewById(R.id.rvDivisas)
        btnSiguienteDivisa = view.findViewById(R.id.btnSiguienteDivisa)

        adapter = DivisaAdapter(emptyList()) { divisa ->
            divisaSeleccionada = divisa
            Toast.makeText(requireContext(), "Seleccionaste: ${divisa.codigo}", Toast.LENGTH_SHORT).show()
        }
        rvDivisas.layoutManager = LinearLayoutManager(requireContext())
        rvDivisas.adapter = adapter

        viewModel = ViewModelProvider(this)[DivisaViewModel::class.java]
        viewModel.divisas.observe(viewLifecycleOwner) { adapter.actualizarDatos(it) }
        viewModel.cargando.observe(viewLifecycleOwner) { }
        viewModel.error.observe(viewLifecycleOwner) { it?.let { e ->
            Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_LONG).show()
        } }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.cargarDivisas()

        btnSiguienteDivisa.setOnClickListener {
            val d = divisaSeleccionada
            if (d == null) {
                Toast.makeText(requireContext(), "Por favor selecciona una divisa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("DIVISA_CODIGO", d.codigo)
                .putString("DIVISA_NOMBRE", d.nombre)
                .apply()

            val frag = BalanceFragment().apply {
                arguments = Bundle().apply {
                    putString("codigoDivisa", d.codigo)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack("balance")
                .commit()
        }
    }
}
