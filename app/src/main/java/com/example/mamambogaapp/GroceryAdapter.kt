package com.example.mamambogaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class GroceryAdapter(
    private val items: List<GroceryItem>,
    private val onItemClick: (GroceryItem) -> Unit
) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

    class GroceryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view as MaterialCardView
        val tvEmoji: TextView = view.findViewById(R.id.tvEmoji)
        val tvItemName: TextView = view.findViewById(R.id.tvItemName)
        val tvItemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val cbSelect: CheckBox = view.findViewById(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grocery, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = items[position]
        holder.tvEmoji.text = item.emoji
        holder.tvItemName.text = item.name
        holder.tvItemPrice.text = "KES ${item.price.toInt()} / ${item.unit}"
        holder.cbSelect.isChecked = item.isSelected
        holder.cardView.isChecked = item.isSelected

        holder.cardView.setOnClickListener {
            item.isSelected = !item.isSelected
            holder.cbSelect.isChecked = item.isSelected
            holder.cardView.isChecked = item.isSelected
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size
}
