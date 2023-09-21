package com.tutorials.drinks.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.FragmentSearchDrinksBinding
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.adapter.DrinksAdapter
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchDrinksFragment : Fragment() {

    private var _binding: FragmentSearchDrinksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DrinksViewModel by activityViewModels()
    private var job: Job? = null
    private val searchAdapter: DrinksAdapter by lazy { DrinksAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchDrinksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeSearchViewPlate()
        observeDrinksSearchState()
        performSearch()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        searchAdapter.adapterClickListener {
            val navigate =
                SearchDrinksFragmentDirections.actionSearchDrinksFragmentToDrinkDetailsFragment(it)
            findNavController().navigate(navigate)
        }
    }

    private fun observeDrinksSearchState() {
        binding.allDrinksRV.adapter = searchAdapter
        lifecycleScope.launch {
            viewModel.searchDrinksState.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        if (resource.data?.drinks != null) {
                            showError(state = false)
                            showLoadingState(false)
                            binding.allDrinksRV.isVisible = true
                            searchAdapter.submitList(resource.data.drinks)
                            return@collect
                        }
                        showError("Error trying to load item ...", state = true)
                        showLoadingState(false)
                    }
                    is Resource.Failure -> {
                        binding.allDrinksRV.isVisible = false
                        showError(resource.msg.toString(), true)
                        showLoadingState(false)
                    }
                    is Resource.Loading -> {
                        binding.allDrinksRV.isVisible = false
                        showError(state = false)
                        showLoadingState(true)
                    }

                }
            }
        }
    }

    private fun performSearch() {
        binding.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.trim().isEmpty()) {
                        viewModel.toggleSearchDrinksState(Resource.Failure("Search Cocktail"))
                        return@let
                    }
                    viewModel.searchDrinks(query.trim())

                } ?: viewModel.toggleSearchDrinksState(Resource.Failure("Search Cocktail"))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(1000L)
                    newText?.let {
                        if (newText.trim().isEmpty()) {
                            viewModel.toggleSearchDrinksState(Resource.Failure("Search Cocktail"))
                            return@let
                        }
                        viewModel.searchDrinks(newText.trim())

                    } ?: viewModel.toggleSearchDrinksState(Resource.Failure("Search Cocktail"))
                }

                return false
            }
        })
    }

    private fun showLoadingState(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun showError(errorText: String = "", state: Boolean) {
        binding.errorImg.isVisible = state
        binding.errorText.isVisible = state
        binding.errorText.text = errorText
    }

    private fun changeSearchViewPlate() {
        val searchPlate = binding.searchBar.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.transparent_background)
    }

}