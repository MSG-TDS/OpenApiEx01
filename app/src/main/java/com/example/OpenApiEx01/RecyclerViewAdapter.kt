package com.example.OpenApiEx01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {
    var dataList = mutableListOf<Parm>()

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: Parm) {
            itemView.textName.text = item.stationName
            itemView.textX.text = item.dmX
            itemView.textY.text = item.dmY
        }

        init{

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = dataList.get(position)
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}