package com.tutorials.drinks.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tutorials.drinks.databinding.FragmentCategoryBinding
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.adapter.DrinksCategoryAdapter
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.launch

class DrinkCategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel : DrinksViewModel by activityViewModels()
    private val categoryAdapter: DrinksCategoryAdapter by lazy { DrinksCategoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.drinkCategoryRV.adapter= categoryAdapter
        lifecycleScope.launch {
            viewModel.categoriesFirstLaunch.collect{
                if (it){
                    viewModel.getAllCategory()
                    viewModel.toggleCategoriesFirstLaunch( false)
                }
            }
        }
        observeDrinksCategoryState()
        categoryAdapter.adapterClickListener {category->
            val navigate =
                DrinkCategoryFragmentDirections.actionCategoryFragmentToDrinksByCategoryFragment(
                    category.strCategory
                )
            findNavController().navigate(navigate)
        }

    }

    private fun observeDrinksCategoryState(){

        lifecycleScope.launch {
            viewModel.drinkCategoryState.collect { response ->
                binding.drinkCategoryRV.isVisible = response is Resource.Successful
                binding.retryBtn.isVisible = response is Resource.Failure
                when (response) {
                    is Resource.Successful -> {
                        if (response.data?.categories != null){
                            showError(state = false)
                            showLoadingState(false)
                            categoryAdapter.submitList(response.data.categories)
                            return@collect
                        }
                        showError("Error trying to load item ...",state = true)
                        showLoadingState(false)
                    }
                    is Resource.Failure -> {
                        binding.retryBtn.setOnClickListener {
                            viewModel.getAllCategory()
                        }
                        showLoadingState(false)
                        showError(response.msg.toString(),true)
                    }
                    is Resource.Loading -> {
                        showError(state = false)
                        showLoadingState(true)
                    }
                }
            }
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
}