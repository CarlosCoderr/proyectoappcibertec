package com.app.balance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.R
import com.app.balance.model.TransaccionConDetalles
import java.text.SimpleDateFormat
import java.util.Locale

class TransaccionSimpleAdapter :
    ListAdapter<TransaccionConDetalles, TransaccionSimpleAdapter.TransaccionViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaccion_simple, parent, false)
        return TransaccionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransaccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoriaNombre)
        private val tvMonto: TextView = itemView.findViewById(R.id.tvMonto)
        private val tvDetalle: TextView = itemView.findViewById(R.id.tvDetalle)

        private val isoFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val displayFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(transaccion: TransaccionConDetalles) {
            tvCategoria.text = transaccion.categoria.nombre
            tvMonto.text = String.format(Locale.getDefault(), "S/ %.2f", transaccion.transaccion.monto)

            val fechaLegible = transaccion.transaccion.fecha?.let {
                runCatching { isoFormatter.parse(it) }
                    .getOrNull()?.let(displayFormatter::format) ?: it
            } ?: ""

            val comentario = transaccion.transaccion.comentario
            tvDetalle.text = if (!comentario.isNullOrBlank()) {
                "$fechaLegible • $comentario"
            } else {
                fechaLegible
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<TransaccionConDetalles>() {
        override fun areItemsTheSame(
            oldItem: TransaccionConDetalles,
            newItem: TransaccionConDetalles
        ): Boolean = oldItem.transaccion.id == newItem.transaccion.id

        override fun areContentsTheSame(
            oldItem: TransaccionConDetalles,
            newItem: TransaccionConDetalles
        ): Boolean = oldItem == newItem
    }
}
