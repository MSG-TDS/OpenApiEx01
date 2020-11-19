package com.example.OpenApiEx01

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.Holder>() {
    var dataList = mutableListOf<StationDetail>()

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: StationDetail) {
            itemView.textName.text = item.stationName
            itemView.textX.text = item.dmX
            itemView.textY.text = item.dmY
            itemView.textAddr.text = item.addr
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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            intent.putExtra("StationName", item.stationName)
            intent.putExtra("Addr", item.addr)
            intent.putExtra("Item", item.item)
            intent.putExtra("Photo", item.photo)
            intent.putExtra("dmX", item.dmX)
            intent.putExtra("dmY", item.dmY)

            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}