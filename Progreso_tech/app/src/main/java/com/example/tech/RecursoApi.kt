package com.example.tech

import retrofit2.Call
import retrofit2.http.*

interface RecursoApi {

    // Ruta base de la API: https://69056526ee3d0d14c13285d0.mockapi.io/api/recursos

    @GET("recursos")
    fun obtenerRecursos(): Call<List<Recurso>>

    @GET("recursos/{id}")
    fun obtenerRecursoPorId(@Path("id") id: String): Call<Recurso>

    @POST("recursos")
    fun crearRecurso(@Body recurso: Recurso): Call<Recurso>

    @PUT("recursos/{id}")
    fun actualizarRecurso(@Path("id") id: String, @Body recurso: Recurso): Call<Recurso>

    @DELETE("recursos/{id}")
    fun eliminarRecurso(@Path("id") id: String): Call<Void>
}