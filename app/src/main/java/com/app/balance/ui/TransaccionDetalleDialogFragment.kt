package com.app.balance.ui

import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.app.balance.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TransaccionDetalleDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_transaccion_detalle, null, false)

        val args = requireArguments()

        view.findViewById<TextView>(R.id.txtCategoria).text = args.getString(ARG_CATEGORIA) ?: ""
        view.findViewById<TextView>(R.id.txtMonto).text      = args.getString(ARG_MONTO_FORMAT) ?: ""
        view.findViewById<TextView>(R.id.txtFecha).text      = args.getString(ARG_FECHA_FORMAT) ?: ""
        view.findViewById<TextView>(R.id.txtComentario).text = args.getString(ARG_COMENTARIO) ?: ""

        val dialog = MaterialAlertDialogBuilder(
            ContextThemeWrapper(requireContext(), R.style.ThemeOverlay_Balance_Alert_Square)
        )
            .setTitle("Detalle de transacción")
            .setView(view)
            .setNegativeButton("Cerrar", null) // solo cierra
            .setPositiveButton("Editar") { _, _ ->
                val id = if (args.containsKey(ARG_ID)) args.getInt(ARG_ID) else null
                parentFragmentManager.setFragmentResult(REQ_EDIT, bundleOf(KEY_ID to id))
            }
            .create()


        dialog.setOnShowListener {
            val btnEditar = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            val btnCerrar = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)


            btnEditar.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            btnCerrar.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))


            btnEditar.paint.isFakeBoldText = true
            btnCerrar.paint.isFakeBoldText = true
        }

        return dialog
    }

    companion object {
        const val REQ_EDIT = "req_edit_transaccion"
        const val KEY_ID = "transaccion_id"

        private const val ARG_ID = "id"
        private const val ARG_CATEGORIA = "categoria"
        private const val ARG_MONTO_FORMAT = "monto_format"
        private const val ARG_FECHA_FORMAT = "fecha_format"
        private const val ARG_COMENTARIO = "comentario"

        fun newInstance(
            id: Int?,
            categoria: String?,
            montoFormateado: String?,
            fechaFormateada: String?,
            comentario: String?
        ): TransaccionDetalleDialogFragment {
            return TransaccionDetalleDialogFragment().apply {
                arguments = Bundle().apply {
                    if (id != null) putInt(ARG_ID, id)
                    putString(ARG_CATEGORIA, categoria)
                    putString(ARG_MONTO_FORMAT, montoFormateado)
                    putString(ARG_FECHA_FORMAT, fechaFormateada)
                    putString(ARG_COMENTARIO, comentario)
                }
            }
        }
    }
}
