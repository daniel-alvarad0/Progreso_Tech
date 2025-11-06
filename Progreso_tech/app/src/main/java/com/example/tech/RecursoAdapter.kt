package com.example.tech

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tech.Recurso

class RecursoAdapter(private val recursos: List<Recurso>) : RecyclerView.Adapter<RecursoAdapter.ViewHolder>() {

    private var onItemClick: OnItemClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.tvTitulo)
        val descripcionTextView: TextView = view.findViewById(R.id.tvDescripcion)
        val tipoTextView: TextView = view.findViewById(R.id.tvTipo)

        val enlaceTextView: TextView = view.findViewById(R.id.tvEnlace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recurso_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recurso = recursos[position]

        holder.tituloTextView.text = recurso.titulo
        holder.descripcionTextView.text = recurso.descripcion
        holder.tipoTextView.text = "Tipo: ${recurso.tipo}"

        holder.enlaceTextView.text = recurso.enlace

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(recurso)
        }
    }

    override fun getItemCount(): Int {
        return recursos.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClick = listener
    }

    interface OnItemClickListener {
        fun onItemClick(recurso: Recurso)
    }
}