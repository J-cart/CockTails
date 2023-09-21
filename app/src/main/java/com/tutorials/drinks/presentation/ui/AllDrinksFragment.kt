package com.tutorials.drinks.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.FragmentAllDrinksBinding
import com.tutorials.drinks.domain.util.ALCOHOLIC
import com.tutorials.drinks.domain.util.NON_ALCOHOLIC
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.adapter.DrinksAdapter
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.launch


class AllDrinksFragment : Fragment() {
//    private var _binding: FragmentAllDrinksBinding? = null
    private lateinit var binding: FragmentAllDrinksBinding
//    private val binding get() = _binding!!
    private val viewModel: DrinksViewModel by activityViewModels()
    private val drinksAdapter: DrinksAdapter by lazy { DrinksAdapter() }
    private var mMenu: Menu? = null
    private var checkedMenuPos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllDrinksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allDrinksRV.adapter = drinksAdapter
        lifecycleScope.launch {
            viewModel.homeFirstLaunch.collect{
                if (it){
                    viewModel.getAllDrinks(ALCOHOLIC)
                    viewModel.toggleHomeFirstLaunch( false)
                }
            }
        }

        binding.moreBtn.setOnClickListener {
            setUpPopUpMenu(it)
        }

        observeAllDrinksState()

        binding.searchPlate.setOnClickListener {
            val navigate =
                AllDrinksFragmentDirections.actionAllDrinksFragmentToSearchDrinksFragment()
            findNavController().navigate(navigate)
        }

        drinksAdapter.adapterClickListener { drink->
            val navigate =
                AllDrinksFragmentDirections.actionAllDrinksFragmentToDrinkDetailsFragment(drink)
            findNavController().navigate(navigate)
        }
    }

    private fun observeAllDrinksState() {
        lifecycleScope.launch {
            viewModel.allDrinksState.collect{ resource ->
                binding.retryBtn.isVisible = resource is Resource.Failure
                binding.allDrinksRV.isVisible = resource is Resource.Successful
                when (resource) {
                    is Resource.Successful -> {
                        if (resource.data?.drinks != null){
                            showError(state = false)
                            showLoadingState(false)
                            drinksAdapter.submitList(resource.data.drinks)
                            return@collect
                        }
                        showError("Error trying to load item ...",state = true)
                        showLoadingState(false)
                    }
                    is Resource.Failure -> {
                        binding.retryBtn.setOnClickListener {
                            mMenu?.let {menu->
                                if (menu.getItem(0).isChecked){
                                    viewModel.getAllDrinks(ALCOHOLIC)
                                    return@setOnClickListener
                                }
                                viewModel.getAllDrinks(NON_ALCOHOLIC)
                            } ?: viewModel.getAllDrinks(ALCOHOLIC)

                        }
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

    private fun showLoadingState(state:Boolean){
        binding.progressBar.isVisible = state
    }

    private fun showError(errorText:String="",state: Boolean){
        binding.errorImg.isVisible = state
        binding.errorText.isVisible = state
        binding.errorText.text = errorText
    }

    private fun setUpPopUpMenu(view: View){
        val popupMenu = PopupMenu(requireContext(), view).apply {
            mMenu = menu
        }
        popupMenu.menuInflater.inflate(R.menu.pop_type_menu,popupMenu.menu)
        popupMenu.menu.getItem(checkedMenuPos).isChecked = true
        popupMenu.setOnMenuItemClickListener {item->
            when (item.itemId) {
                R.id.alcoholic_option -> {
                    checkedMenuPos = 0
                    viewModel.getAllDrinks(ALCOHOLIC)
                    true
                }
                R.id.non_alcoholic_option -> {
                    checkedMenuPos = 1
                    viewModel.getAllDrinks(NON_ALCOHOLIC)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
        popupMenu.show()
    }


}