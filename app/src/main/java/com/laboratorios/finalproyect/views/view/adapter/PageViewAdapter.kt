package com.laboratorios.finalproyect.views.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laboratorios.finalproyect.R
import org.w3c.dom.Text
import java.util.concurrent.RecursiveAction

class PageViewAdapter(private var title : List<String>, private var description: List<String>, private var images: List<Int>) :
    RecyclerView.Adapter<PageViewAdapter.PageViewHolder>() {

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val image : ImageView = itemView.findViewById(R.id.helpPageImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.page_view, parent, false))
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.title.text = title[position]
        holder.description.text = description[position]
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return title.size
    }


}