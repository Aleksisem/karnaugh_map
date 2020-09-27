package com.example.karnaughmap.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karnaughmap.R
import com.example.karnaughmap.database.Expression


class MenuAdapter : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    var data = listOf<Expression>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, holder.expressionShort.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MenuViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expressionLong: TextView = itemView.findViewById(R.id.expression_long_text)
        val expressionShort: TextView = itemView.findViewById(R.id.expression_short_text)

        fun bind(item: Expression, context: Context) {
            expressionLong.text = "ДНФ: ${item.expressionLong}"
            expressionShort.text = "СДНФ: ${item.expressionShort}"
            itemView.setOnClickListener {
                Log.i("MenuAdapter", "Url: ${item.uri}")

                val intent = Intent()
                intent.setDataAndType(Uri.parse("file://${item.uri}"), "image/*")
                context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MenuViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.menu_item, parent, false)
                return MenuViewHolder(view)
            }
        }
    }

}