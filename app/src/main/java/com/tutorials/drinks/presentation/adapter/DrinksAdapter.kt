package com.tutorials.drinks.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.AllDrinksViewholderBinding
import com.tutorials.drinks.domain.model.Drink

class DrinksAdapter:ListAdapter<Drink, DrinksAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val binding = AllDrinksViewholderBinding.bind(view)
        fun bind(drink: Drink){
            binding.apply {
                drinkName.text = drink.drinkName
                drinkImg.load(drink.drinkImgUrl){
                    crossfade(true)
                    placeholder(R.drawable.drink_icon)
                    error(R.drawable.drink_icon)
                }
            }
            binding.root.setOnClickListener {
                listener?.let { it(drink) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_drinks_viewholder,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = getItem(position)
        holder.bind(pos)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Drink>() {

        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.drinkId == newItem.drinkId
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.drinkId == newItem.drinkId && oldItem.drinkName == newItem.drinkName
        }

    }

    private var listener:((Drink)->Unit)? = null

    fun adapterClickListener(listener:(Drink)->Unit){
        this.listener = listener
    }


}