package com.example.tech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarRecursoActivity : AppCompatActivity() {

    private lateinit var api: RecursoApi
    private var recurso: Recurso? = null

    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var enlaceEditText: EditText
    private lateinit var imagenEditText: EditText
    private lateinit var actualizarButton: Button

    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_recurso)

        tituloEditText = findViewById(R.id.editTextTitulo)
        descripcionEditText = findViewById(R.id.editTextDescripcion)
        tipoEditText = findViewById(R.id.editTextTipo)
        enlaceEditText = findViewById(R.id.editTextEnlace)
        imagenEditText = findViewById(R.id.editTextImagen)
        actualizarButton = findViewById(R.id.actualizarButton)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://69056526ee3d0d14c13285d0.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RecursoApi::class.java)

        val recursoId = intent.getStringExtra("recurso_id")
        Log.e("API", "recursoId : $recursoId")

        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val tipo = intent.getStringExtra("tipo")
        val enlace = intent.getStringExtra("enlace")
        val imagen = intent.getStringExtra("imagen")

        tituloEditText.setText(titulo)
        descripcionEditText.setText(descripcion)
        tipoEditText.setText(tipo)
        enlaceEditText.setText(enlace)
        imagenEditText.setText(imagen)

        actualizarButton.setOnClickListener {
            if (!recursoId.isNullOrEmpty()) {
                val recursoActualizado = Recurso(
                    id = recursoId,
                    titulo = tituloEditText.text.toString(),
                    descripcion = descripcionEditText.text.toString(),
                    tipo = tipoEditText.text.toString(),
                    enlace = enlaceEditText.text.toString(),
                    imagen = imagenEditText.text.toString()
                )

                val jsonRecursoActualizado = Gson().toJson(recursoActualizado)
                Log.d("API", "JSON enviado: $jsonRecursoActualizado")

                api.actualizarRecurso(recursoId, recursoActualizado).enqueue(object : Callback<Recurso> {
                    override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(this@ActualizarRecursoActivity, "Recurso actualizado correctamente", Toast.LENGTH_SHORT).show()
                            val i = Intent(getBaseContext(), MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            try {
                                val errorJson = response.errorBody()?.string()
                                val errorObj = JSONObject(errorJson)
                                val errorMessage = errorObj.getString("message")
                                Toast.makeText(this@ActualizarRecursoActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Log.e("API", "Error al actualizar recurso. Código: ${response.code()}")
                                Toast.makeText(this@ActualizarRecursoActivity, "Error al actualizar el recurso", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<Recurso>, t: Throwable) {
                        Log.e("API", "onFailure : ${t.message}")
                        Toast.makeText(this@ActualizarRecursoActivity, "Error de red al actualizar recurso", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@ActualizarRecursoActivity, "Error: ID de recurso no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}