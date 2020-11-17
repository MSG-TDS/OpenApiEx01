package com.example.OpenApiEx01

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {
    var dataList = mutableListOf<StationDetail>()

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(detail: StationDetail) {
            itemView.textName.text = detail.stationName
            itemView.textX.text = detail.dmX
            itemView.textY.text = detail.dmY
            itemView.textAddr.text = detail.addr
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

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            intent.putExtra("stationName", item.stationName)
            intent.putExtra("dmX", item.dmX.toDoubleOrNull())
            intent.putExtra("dmY", item.dmY.toDoubleOrNull())
            intent.putExtra("addr", item.addr)
            intent.putExtra("item", item.item)
            intent.putExtra("photo", item.photo)

            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}