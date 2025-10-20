package com.app.balance

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class BienvenidaFragment : Fragment(R.layout.fragment_bienvenida) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnComenzar).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DivisaFragment())
                .addToBackStack("divisa")
                .commit()
        }
    }
}
