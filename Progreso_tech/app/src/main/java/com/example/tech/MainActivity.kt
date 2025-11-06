package com.example.tech

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tech.RecursoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private lateinit var api: RecursoApi

    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab_agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.fab_agregar)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://69056526ee3d0d14c13285d0.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RecursoApi::class.java)

        cargarDatos(api)

        fab_agregar.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), CrearRecursoActivity::class.java)
            i.putExtra("auth_username", auth_username)
            i.putExtra("auth_password", auth_password)
            startActivity(i)
        })
    }

    override fun onResume() {
        super.onResume()
        cargarDatos(api)
    }

    private fun cargarDatos(api: RecursoApi) {
        val call = api.obtenerRecursos()
        call.enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    val recursos = response.body()
                    if (recursos != null) {
                        val recursosOrdenados = recursos.sortedByDescending { it.id.toInt() }

                        adapter = RecursoAdapter(recursosOrdenados)
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickListener(object : RecursoAdapter.OnItemClickListener {
                            override fun onItemClick(recurso: Recurso) {
                                val opciones = arrayOf("Modificar Recurso", "Eliminar Recurso")
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(recurso.titulo)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> Modificar(recurso)
                                            1 -> eliminarRecurso(recurso, api)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los recursos: $error")
                    Toast.makeText(this@MainActivity,
                        "Error al obtener los recursos 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Log.e("API", "Error al obtener los recursos: ${t.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Error al obtener los recursos 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun Modificar(recurso: Recurso) {
        val i = Intent(getBaseContext(), ActualizarRecursoActivity::class.java) // ¡CAMBIADO!
        i.putExtra("recurso_id", recurso.id)
        i.putExtra("titulo", recurso.titulo)
        i.putExtra("descripcion", recurso.descripcion)
        i.putExtra("tipo", recurso.tipo)
        i.putExtra("enlace", recurso.enlace)
        i.putExtra("imagen", recurso.imagen)
        startActivity(i)
    }

    private fun eliminarRecurso(recurso: Recurso, api: RecursoApi) {
        Log.e("API", "id : $recurso")
        val llamada = api.eliminarRecurso(recurso.id)
        llamada.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                    cargarDatos(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al eliminar recurso : $error")
                    Toast.makeText(this@MainActivity, "Error al eliminar recurso 1", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error al eliminar recurso : $t")
                Toast.makeText(this@MainActivity, "Error al eliminar recurso 2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}