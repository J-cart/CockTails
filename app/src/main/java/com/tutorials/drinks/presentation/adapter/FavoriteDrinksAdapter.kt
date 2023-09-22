package com.tutorials.drinks.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.FavoritesViewholderBinding
import com.tutorials.drinks.domain.model.Drink

class FavoriteDrinksAdapter:ListAdapter<Drink, FavoriteDrinksAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val binding = FavoritesViewholderBinding.bind(view)
        fun bind(drink: Drink){
            binding.apply {
                root.clipToOutline = true
                imageHolder.clipToOutline = true
                drinkImage.clipToOutline = true
                drinkTitleText.text = drink.drinkName
                drinkImage.load(drink.drinkImgUrl){
                    crossfade(true)
                    placeholder(R.drawable.drink_icon)
                    error(R.drawable.drink_icon)
                }
                holder.setOnClickListener {
                    listener?.let { it(drink) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.favorites_viewholder,parent,false)
        return ViewHolder(view)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Drink>() {

        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.drinkId == newItem.drinkId
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.drinkId == newItem.drinkId && oldItem.drinkName == newItem.drinkName
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        holder.bind(pos)
    }
    private var listener:((Drink)->Unit)? = null

    fun adapterClickListener(listener:(Drink)->Unit){
        this.listener = listener
    }
}