package com.tutorials.drinks.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.FragmentFavoritesBinding
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.adapter.FavoriteDrinksAdapter
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoriteDrinksAdapter by lazy { FavoriteDrinksAdapter() }
    private val viewModel by activityViewModels<DrinksViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesRV.adapter = favoriteDrinksAdapter
        bindItemTouchHelper(view)
        viewModel.getAllFavoriteDrinks()

        observeAllFavoriteDrinksState()
        favoriteDrinksAdapter.adapterClickListener {
            val navigate =
                FavoritesFragmentDirections.actionFavoritesFragmentToDrinkDetailsFragment(it)
            findNavController().navigate(navigate)
        }
    }

    private fun observeAllFavoriteDrinksState() {
        lifecycleScope.launch {
            viewModel.allFavoriteDrinksState.collect{ resource ->
                binding.menuBtn.isVisible = resource is Resource.Successful
                binding.favoritesRV.isVisible = resource is Resource.Successful
                when (resource) {
                    is Resource.Successful -> {
                        showError(state = false)
                        showLoadingState(false)
                        favoriteDrinksAdapter.submitList(resource.data)
                        binding.menuBtn.setOnClickListener{
                            setUpPopUpMenu(it)
                        }
                    }
                    is Resource.Failure -> {
                        showLoadingState(false)
                        showError(resource.msg.toString(), true)
                    }
                    is Resource.Loading -> {
                        showError(state = false)
                        showLoadingState(true)
                    }
                }
            }
        }
    }


    private fun setUpPopUpMenu(view: View){
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.pop_delete_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {item->
            when (item.itemId) {
                R.id.delete_option -> {
                   // viewModel.removeAllFavorites()
                    showDialog()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popupMenu.show()
    }

    private fun bindItemTouchHelper(view: View){
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val drink = favoriteDrinksAdapter.currentList[position]
                viewModel.removeFromFavorites(drink)
                Snackbar.make(view, "${drink.drinkName} removed from favorites", Snackbar.LENGTH_SHORT).apply {
                    setAction("UNDO") {
                        viewModel.addToFavorites(drink)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.favoritesRV)
        }
    }

    private fun showLoadingState(state:Boolean){
        binding.progressBar.isVisible = state
    }

    private fun showError(errorText:String="",state: Boolean){
        binding.errorImg.isVisible = state
        binding.errorText.isVisible = state
        binding.errorText.text = errorText
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage("Are you sure you want to delete all ?")
            setPositiveButton("YES") { d, i ->
                viewModel.removeAllFavorites()
            }
            setNegativeButton("NO") { d, i ->
                d.dismiss()
            }
            show()
        }

    }

}