package com.example.rickandmorty.ui.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.RickRepository
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.PageInfo
import com.example.rickandmorty.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EpisodeListUiState(
    val cargando: Boolean = true,
    val episodios: List<Episode> = emptyList(),
    val info: PageInfo = PageInfo(),
    val error: String? = null
)

data class EpisodeDetailUiState(
    val episodio: Episode? = null,
    val personajes: List<Character> = emptyList(),
    val cargandoPersonajes: Boolean = false,
    val errorPersonajes: String? = null
)

class EpisodeViewModel(
    private val repository: RickRepository = RickRepository()
) : ViewModel() {

    private val _listState = MutableStateFlow(EpisodeListUiState())
    val listState: StateFlow<EpisodeListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(EpisodeDetailUiState())
    val detailState: StateFlow<EpisodeDetailUiState> = _detailState.asStateFlow()

    init {
        cargarEpisodios()
    }

    fun cargarEpisodios(pagina: Int = 1) {
        viewModelScope.launch {
            _listState.update { it.copy(cargando = true, error = null) }
            when (val res = repository.getEpisodes(page = pagina)) {
                is Resultado.Exito -> {
                    val (info, lista) = res.datos
                    _listState.update {
                        it.copy(cargando = false, episodios = lista, info = info, error = null)
                    }
                }
                is Resultado.Error -> {
                    _listState.update { it.copy(cargando = false, error = res.mensaje) }
                }
            }
        }
    }

    fun seleccionarEpisodio(episodio: Episode?) {
        _detailState.update {
            EpisodeDetailUiState(
                episodio = episodio,
                personajes = emptyList(),
                cargandoPersonajes = episodio != null,
                errorPersonajes = null
            )
        }
        if (episodio != null) {
            cargarPersonajesDeEpisodio(episodio)
        }
    }

    private fun cargarPersonajesDeEpisodio(episodio: Episode) {
        viewModelScope.launch {
            val ids = episodio.characters
                .mapNotNull { it.substringAfterLast("/").toIntOrNull() }
                .distinct()

            if (ids.isEmpty()) {
                _detailState.update { it.copy(cargandoPersonajes = false) }
                return@launch
            }

            when (val res = repository.getCharactersByIds(ids)) {
                is Resultado.Exito -> {
                    _detailState.update {
                        it.copy(cargandoPersonajes = false, personajes = res.datos, errorPersonajes = null)
                    }
                }
                is Resultado.Error -> {
                    _detailState.update {
                        it.copy(cargandoPersonajes = false, errorPersonajes = res.mensaje)
                    }
                }
            }
        }
    }
}
