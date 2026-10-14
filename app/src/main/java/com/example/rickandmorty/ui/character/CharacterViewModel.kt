package com.example.rickandmorty.ui.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.RickRepository
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharacterUiState(
    val cargando: Boolean = true,
    val personajes: List<Character> = emptyList(),
    val error: String? = null
)

class CharacterViewModel(
    private val repository: RickRepository = RickRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    init {
        cargarPersonajes()
    }

    fun cargarPersonajes() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val res = repository.getCharacters()) {
                is Resultado.Exito -> {
                    val (_, lista) = res.datos
                    _uiState.update { it.copy(cargando = false, personajes = lista, error = null) }
                }
                is Resultado.Error -> {
                    _uiState.update { it.copy(cargando = false, error = res.mensaje) }
                }
            }
        }
    }
}
