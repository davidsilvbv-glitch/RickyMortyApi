package com.example.rickandmorty.ui.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.RickRepository
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.PageInfo
import com.example.rickandmorty.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationListUiState(
    val cargando: Boolean = true,
    val ubicaciones: List<Location> = emptyList(),
    val info: PageInfo = PageInfo(),
    val error: String? = null,
    val pagina: Int = 1,
    val nombreFiltro: String = "",
    val tipoFiltro: String = "",
    val dimensionFiltro: String = ""
)

data class LocationDetailUiState(
    val cargando: Boolean = true,
    val ubicacion: Location? = null,
    val residentes: List<Character> = emptyList(),
    val residentesCargando: Boolean = false,
    val error: String? = null,
    val residentesError: String? = null
)

class LocationViewModel(
    private val repository: RickRepository = RickRepository()
) : ViewModel() {

    private val _listState = MutableStateFlow(LocationListUiState())
    val listState: StateFlow<LocationListUiState> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow(LocationDetailUiState())
    val detailState: StateFlow<LocationDetailUiState> = _detailState.asStateFlow()

    init {
        cargarUbicaciones()
    }

    fun cargarUbicaciones(
        pagina: Int = _listState.value.pagina,
        nombre: String = _listState.value.nombreFiltro,
        tipo: String = _listState.value.tipoFiltro,
        dimension: String = _listState.value.dimensionFiltro
    ) {
        viewModelScope.launch {
            _listState.update {
                it.copy(
                    cargando = true,
                    error = null,
                    pagina = pagina,
                    nombreFiltro = nombre,
                    tipoFiltro = tipo,
                    dimensionFiltro = dimension
                )
            }
            when (val res = repository.getLocations(
                page = pagina,
                name = nombre.ifBlank { null },
                type = tipo.ifBlank { null },
                dimension = dimension.ifBlank { null }
            )) {
                is Resultado.Exito -> {
                    val (pageInfo, lista) = res.datos
                    _listState.update {
                        it.copy(
                            cargando = false,
                            ubicaciones = lista,
                            info = pageInfo,
                            error = null
                        )
                    }
                }
                is Resultado.Error -> {
                    _listState.update { it.copy(cargando = false, error = res.mensaje) }
                }
            }
        }
    }

    fun cargarDetalleUbicacion(locationId: Int) {
        viewModelScope.launch {
            _detailState.update {
                LocationDetailUiState(cargando = true, error = null)
            }
            when (val resLocation = repository.getLocationDetail(locationId)) {
                is Resultado.Exito -> {
                    val location = resLocation.datos
                    _detailState.update {
                        it.copy(cargando = false, ubicacion = location, residentesCargando = true)
                    }
                    val ids = location.residents
                        .mapNotNull { it.substringAfterLast("/").toIntOrNull() }
                        .distinct()
                    if (ids.isNotEmpty()) {
                        when (val resResidentes = repository.getCharactersByIds(ids)) {
                            is Resultado.Exito -> {
                                _detailState.update {
                                    it.copy(
                                        residentesCargando = false,
                                        residentes = resResidentes.datos,
                                        residentesError = null
                                    )
                                }
                            }
                            is Resultado.Error -> {
                                _detailState.update {
                                    it.copy(
                                        residentesCargando = false,
                                        residentesError = resResidentes.mensaje
                                    )
                                }
                            }
                        }
                    } else {
                        _detailState.update { it.copy(residentesCargando = false) }
                    }
                }
                is Resultado.Error -> {
                    _detailState.update { it.copy(cargando = false, error = resLocation.mensaje) }
                }
            }
        }
    }
}
