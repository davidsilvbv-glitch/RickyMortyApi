package com.example.rickandmorty.util

/**
 * Clase sellada genérica para representar el resultado de una operación (Éxito o Error).
 */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String, val excepcion: Throwable? = null) : Resultado<Nothing>()
}
