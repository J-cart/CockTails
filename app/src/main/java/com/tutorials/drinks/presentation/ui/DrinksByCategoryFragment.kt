package com.tutorials.drinks.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tutorials.drinks.databinding.FragmentDrinksByCategoryBinding
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.adapter.DrinksAdapter
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.launch

class DrinksByCategoryFragment : Fragment() {

    private var _binding:FragmentDrinksByCategoryBinding? = null
    private val binding get() = _binding!!
    private val args: DrinksByCategoryFragmentArgs by navArgs()
    private val drinksByCategoryAdapter: DrinksAdapter by lazy { DrinksAdapter() }
    private val viewModel: DrinksViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDrinksByCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDrinksByCategory(args.categoryId)
        observeDrinksByCategoryState()
        binding.categoryName.text = args.categoryId
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        drinksByCategoryAdapter.adapterClickListener { drink->
            val navigate =
                DrinksByCategoryFragmentDirections.actionDrinksByCategoryFragmentToDrinkDetailsFragment(
                    drink
                )
            findNavController().navigate(navigate)
        }
    }

    private fun observeDrinksByCategoryState(){
        binding.drinksByCategoryRV.adapter= drinksByCategoryAdapter
        lifecycleScope.launch {
            viewModel.drinksByCategoryState.collect { response ->
                when (response) {
                    is Resource.Successful -> {
                        if (response.data?.drinks != null){
                            showError(state = false)
                            showLoadingState(false)
                            binding.drinksByCategoryRV.isVisible = true
                            drinksByCategoryAdapter.submitList(response.data.drinks)
                            return@collect
                        }
                        binding.drinksByCategoryRV.isVisible = false
                        showError("Error trying to load item ...",state = true)
                        showLoadingState(false)
                    }
                    is Resource.Failure -> {
                        binding.drinksByCategoryRV.isVisible = false
                        showLoadingState(false)
                        showError(response.msg.toString(),true)
                    }
                    is Resource.Loading -> {
                        showError(state = false)
                        binding.drinksByCategoryRV.isVisible = false
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