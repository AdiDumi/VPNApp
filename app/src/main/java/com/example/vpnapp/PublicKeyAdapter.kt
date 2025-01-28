package com.example.vpnapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PublicKeyAdapter(
    private val keys: List<PublicKeyItem>,
    private val onKeyClick: (PublicKeyItem) -> Unit
) : RecyclerView.Adapter<PublicKeyAdapter.KeyViewHolder>() {

    class KeyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val keyText: TextView = view.findViewById(R.id.keyTextView)
        val container: View = view.findViewById(R.id.key_text_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_public_key, parent, false)
        return KeyViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        val keyItem = keys[position]
        holder.keyText.text = keyItem.key

        // Highlight if the key is in use
        holder.container.setBackgroundColor(
            if (keyItem.isInUse) Color.GREEN else Color.TRANSPARENT
        )

        holder.itemView.setOnClickListener {
            onKeyClick(keyItem)
        }
    }

    override fun getItemCount() = keys.size
}