package com.example.tech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tech.R.*
import com.example.tech.R.id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrearRecursoActivity : AppCompatActivity() {

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var imagenEditText: EditText
    private lateinit var crearButton: Button

    var auth_username = ""
    var auth_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_crear_recurso)

        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            auth_username = datos.getString("auth_username").toString()
            auth_password = datos.getString("auth_password").toString()
        }

        tituloEditText = findViewById(editTextTitulo)
        descripcionEditText = findViewById(editTextDescripcion)
        tipoEditText = findViewById(editTextTipo)
        enlaceEditText = findViewById(editTextEnlace)
        imagenEditText = findViewById(editTextImagen)
        crearButton = findViewById(id.btnGuardar)

        crearButton.setOnClickListener {
            // 1. Capturar todos los nuevos campos
            val titulo = tituloEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()
            val tipo = tipoEditText.text.toString()
            val enlace = enlaceEditText.text.toString()
            val imagen = imagenEditText.text.toString()

            if (titulo.isBlank() || descripcion.isBlank() || tipo.isBlank()) {
                Toast.makeText(this@CrearRecursoActivity, "Título, Descripción y Tipo son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recurso = Recurso(
                id = "0",
                titulo = titulo,
                descripcion = descripcion,
                tipo = tipo,
                enlace = enlace,
                imagen = imagen
            )

            Log.e("API", "auth_username: $auth_username")
            Log.e("API", "auth_password: $auth_password")

            // Crea una instancia de Retrofit (Usando la URL de MockAPI que me proporcionaste)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://69056526ee3d0d14c13285d0.mockapi.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(RecursoApi::class.java)

            api.crearRecurso(recurso).enqueue(object : Callback<Recurso> {
                override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearRecursoActivity, "Recurso creado exitosamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear recurso: $error")
                        Toast.makeText(this@CrearRecursoActivity, "Error al crear el recurso", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Recurso>, t: Throwable) {
                    Log.e("API", "Error de red al crear recurso: ${t.message}")
                    Toast.makeText(this@CrearRecursoActivity, "Error al crear el recurso", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}