package com.app.balance.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.balance.R
import com.app.balance.model.Categoria
import com.google.android.material.card.MaterialCardView

class CategoriaCarouselAdapter(
    private val onCategoriaSelected: (Categoria) -> Unit,
    private val onAddCategoria: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categorias = mutableListOf<Categoria>()
    private var selectedCategoriaId: Int? = null

    override fun getItemCount(): Int = categorias.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position < categorias.size) VIEW_TYPE_CATEGORIA else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CATEGORIA) {
            val view = inflater.inflate(R.layout.item_categoria_chip, parent, false)
            CategoriaViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_categoria_chip, parent, false)
            AddViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoriaViewHolder && position < categorias.size) {
            val categoria = categorias[position]
            holder.bind(categoria, categoria.id == selectedCategoriaId)
        } else if (holder is AddViewHolder) {
            holder.bind()
        }
    }

    fun submitList(newCategorias: List<Categoria>) {
        categorias.clear()
        categorias.addAll(newCategorias)
        notifyDataSetChanged()
    }

    fun setSelectedCategoria(categoriaId: Int?) {
        val previousId = selectedCategoriaId
        if (previousId == categoriaId) {
            return
        }

        val previousIndex = categorias.indexOfFirst { it.id == previousId }
        selectedCategoriaId = categoriaId
        val newIndex = categorias.indexOfFirst { it.id == categoriaId }

        if (previousIndex != -1) {
            notifyItemChanged(previousIndex)
        }

        if (newIndex != -1) {
            notifyItemChanged(newIndex)
        }

        if (previousIndex == -1 && newIndex == -1) {
            notifyDataSetChanged()
        }
    }

    private inner class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val card: MaterialCardView = view.findViewById(R.id.card)
        private val icon: AppCompatImageView = view.findViewById(R.id.img)
        private val name: TextView = view.findViewById(R.id.tv)
        private val context = view.context
        private val selectedStroke = context.resources.getDimensionPixelSize(R.dimen.category_icon_stroke)

        fun bind(categoria: Categoria, isSelected: Boolean) {
            val iconResId = context.resources.getIdentifier(
                categoria.icono,
                "drawable",
                context.packageName
            ).takeIf { it != 0 } ?: R.drawable.ic_categoria

            icon.setImageResource(iconResId)

            val colorInt = categoria.rutaImagen?.takeIf { it.isNotBlank() }?.let {
                runCatching { Color.parseColor(it) }.getOrNull()
            } ?: ContextCompat.getColor(context, R.color.gold)

            icon.imageTintList = ColorStateList.valueOf(colorInt)
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            card.strokeWidth = if (isSelected) selectedStroke else 0
            card.strokeColor = colorInt
            name.text = categoria.nombre

            itemView.setOnClickListener {
                setSelectedCategoria(categoria.id)
                onCategoriaSelected(categoria)
            }
        }
    }

    private inner class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val card: MaterialCardView = view.findViewById(R.id.card)
        private val icon: AppCompatImageView = view.findViewById(R.id.img)
        private val name: TextView = view.findViewById(R.id.tv)

        fun bind() {
            val context = itemView.context
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_gray))
            card.strokeWidth = 0
            icon.setImageResource(R.drawable.ic_add)
            icon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gold))
            name.text = context.getString(R.string.categoria_agregar)

            itemView.setOnClickListener { onAddCategoria() }
        }
    }

    companion object {
        private const val VIEW_TYPE_CATEGORIA = 0
        private const val VIEW_TYPE_ADD = 1
    }
}
