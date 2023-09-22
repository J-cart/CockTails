package com.tutorials.drinks.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tutorials.drinks.R
import com.tutorials.drinks.databinding.FragmentDrinkDetailsBinding
import com.tutorials.drinks.domain.util.Resource
import com.tutorials.drinks.presentation.arch.DrinksViewModel
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DrinkDetailsFragment : Fragment() {
    private var _binding: FragmentDrinkDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DrinkDetailsFragmentArgs by navArgs()
    private val viewModel: DrinksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDrinkDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDrinksById(args.drink.drinkId)
        observeDrinkState()
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    private fun observeDrinkState() {
        lifecycleScope.launch {
            viewModel.drinksByIdState.collect { resource ->
                binding.detailView.isVisible = resource is Resource.Successful
                when (resource) {
                    is Resource.Successful -> {
                        if (resource.data?.drinks != null) {
                            showError(state = false)
                            showLoadingState(false)
                            binding.apply {
                                val drinkDetail = resource.data.drinks[0].toDrinkDetail()
                                lifecycleScope.launch {
                                    runFavoriteOp()
                                }
                                drinkName.text = drinkDetail.drinkName
                                drinkCategoryTv.text = drinkDetail.drinkCategory
                                drinkTypeTv.text = drinkDetail.drinkType
                                drinkIngrTV.text = drinkDetail.ingredients
                                drinkInstrsTV.text = drinkDetail.instructions
                                drinkImg.load(drinkDetail.drinkImgUrl) {
                                    crossfade(true)
                                    placeholder(R.drawable.loading_img)
                                    error(R.drawable.ic_baseline_broken_image_24)
                                }
                            }
                            return@collect
                        }
                        showError("Error trying to load item ...",state = true)
                        showLoadingState(false)

                    }
                    is Resource.Failure -> {
                        showLoadingState(false)
                        showError(resource.msg.toString(),true)
                        binding.retryBtn.setOnClickListener {
                            viewModel.getDrinksById(args.drink.drinkId)
                        }

                    }
                    is Resource.Loading -> {
                        showError(state = false)
                        showLoadingState(true)
                    }
                }
            }


        }
    }


    private fun runFavoriteOp() {
        viewModel.checkIfFavorite(args.drink.drinkId)
        lifecycleScope.launch {
            viewModel.favoriteState.collect { value ->
                when (value) {
                    -1 -> Unit
                    0 -> {
                        binding.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.favorite_outlined_icon))
                        binding.favoriteBtn.setOnClickListener {
                            viewModel.addToFavorites(args.drink)
                            Toast.makeText(
                                    requireContext(),
                            "Added to favorites",
                            Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else -> {
                        binding.favoriteBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.favorite_icon))
                        binding.favoriteBtn.setOnClickListener {
                            viewModel.removeFromFavorites(args.drink)
                        }
                    }
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetFavoriteState()
    }

    private fun showLoadingState(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    private fun showError(errorText: String = "", state: Boolean) {
        binding.errorImg.isVisible = state
        binding.errorText.isVisible = state
        binding.retryBtn.isVisible = state
        binding.errorText.text = errorText
    }
}