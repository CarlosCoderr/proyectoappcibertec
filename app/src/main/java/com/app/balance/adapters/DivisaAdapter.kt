package com.app.balance.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.R
import com.app.balance.model.CountryCode
import com.bumptech.glide.Glide

class DivisaAdapter(
    private var paises: List<CountryCode>,
    private val onPaisClick: (CountryCode) -> Unit
) : RecyclerView.Adapter<DivisaAdapter.DivisaViewHolder>() {

    private var paisesFiltrados = paises.toList()
    private var seleccionadoPosition = -1

    class DivisaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreDivisa)
        val tvCodigo: TextView = view.findViewById(R.id.tvCodigoDivisa)
        val ivBandera: ImageView = view.findViewById(R.id.ivBanderaDivisa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_divisa, parent, false)
        return DivisaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DivisaViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val pais = paisesFiltrados[position]
        holder.tvNombre.text = pais.nombre
        holder.tvCodigo.text = pais.codigo

        // Cargar bandera con Glide
        if (!pais.bandera.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(pais.bandera)
                .centerCrop()
                .placeholder(android.R.drawable.ic_dialog_map)
                .error(android.R.drawable.ic_dialog_map)
                .into(holder.ivBandera)
        }

        // Cambiar color si está seleccionado
        if (position == seleccionadoPosition) {
            holder.itemView.setBackgroundColor(
                holder.itemView.context.getColor(android.R.color.darker_gray)
            )
        } else {
            holder.itemView.setBackgroundColor(
                holder.itemView.context.getColor(android.R.color.white)
            )
        }

        holder.itemView.setOnClickListener {
            val posicionAnterior = seleccionadoPosition
            seleccionadoPosition = position

            if (posicionAnterior != -1 && posicionAnterior < paisesFiltrados.size) {
                notifyItemChanged(posicionAnterior)
            }
            notifyItemChanged(position)

            onPaisClick(pais)
        }
    }

    override fun getItemCount() = paisesFiltrados.size

    fun filtrar(query: String) {
        paisesFiltrados = if (query.isEmpty()) {
            paises
        } else {
            paises.filter { pais ->
                pais.nombre.contains(query, ignoreCase = true) ||
                        pais.codigo.contains(query, ignoreCase = true)
            }
        }
        seleccionadoPosition = -1
        notifyDataSetChanged()
    }

    fun actualizarDatos(nuevosPaises: List<CountryCode>) {
        paises = nuevosPaises
        paisesFiltrados = nuevosPaises
        seleccionadoPosition = -1
        notifyDataSetChanged()
    }

    fun getPaisSeleccionado(): CountryCode? {
        return if (seleccionadoPosition >= 0 && seleccionadoPosition < paisesFiltrados.size) {
            paisesFiltrados[seleccionadoPosition]
        } else {
            null
        }
    }
}