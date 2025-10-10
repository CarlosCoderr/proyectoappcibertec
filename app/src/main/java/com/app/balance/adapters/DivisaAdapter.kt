package com.app.balance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.R
import com.app.balance.entity.Divisa

class DivisaAdapter(
    private var divisas: List<Divisa>,
    private val onDivisaClick: (Divisa) -> Unit
) : RecyclerView.Adapter<DivisaAdapter.DivisaViewHolder>() {

    private var divisasFiltradas = divisas.toList()

    class DivisaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreDivisa)
        val tvCodigo: TextView = view.findViewById(R.id.tvCodigoDivisa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_divisa, parent, false)
        return DivisaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DivisaViewHolder, position: Int) {
        val divisa = divisasFiltradas[position]
        holder.tvNombre.text = divisa.nombre
        holder.tvCodigo.text = divisa.codigo

        holder.itemView.setOnClickListener {
            onDivisaClick(divisa)
        }
    }

    override fun getItemCount() = divisasFiltradas.size

    fun filtrar(query: String) {
        divisasFiltradas = if (query.isEmpty()) {
            divisas
        } else {
            divisas.filter { divisa ->
                divisa.nombre.contains(query, ignoreCase = true) ||
                        divisa.codigo.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun actualizarDatos(nuevasDivisas: List<Divisa>) {
        divisas = nuevasDivisas
        divisasFiltradas = nuevasDivisas
        notifyDataSetChanged()
    }
}