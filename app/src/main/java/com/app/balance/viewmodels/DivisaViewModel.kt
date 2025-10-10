package com.app.balance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.balance.entity.Divisa
import com.app.balance.repo.DivisaRepository
import kotlinx.coroutines.launch

class DivisaViewModel : ViewModel() {

    private val repository = DivisaRepository()

    private val _divisas = MutableLiveData<List<Divisa>>()
    val divisas: LiveData<List<Divisa>> = _divisas

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarDivisas() {
        _cargando.value = true
        viewModelScope.launch {
            val resultado = repository.obtenerDivisas()
            _cargando.value = false

            resultado.onSuccess { listaDivisas ->
                _divisas.value = listaDivisas
                _error.value = null
            }.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }
}