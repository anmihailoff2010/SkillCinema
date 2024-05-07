package com.example.skillcinema.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.CategoriesFilms
import com.example.skillcinema.databinding.FragmentHomeBinding
import com.example.skillcinema.ui.StateLoading
import com.example.skillcinema.ui.adapters.CategoryAdapter
import com.example.skillcinema.ui.allfilmsbycategory.FragmentAllFilms
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateLoadingListener()              // set loading listener
        getCategories()                     // set film list by categories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homePageList.collect {
                    categoryAdapter =
                        CategoryAdapter(20, it, { onClickShoAllButton(it) }, { onClickFilm(it) })
                    binding.categoryList.adapter = categoryAdapter
                }
            }
        }
    }

    private fun onClickFilm(filmId: Int) {
        Log.d(TAG, "onClickFilm: filmId = $filmId")
        val action = FragmentHomeDirections.actionFragmentHomeToFragmentFilmDetail(filmId)
        findNavController().navigate(action)
    }

    private fun onClickShoAllButton(category: CategoriesFilms) {
        findNavController().navigate(
            R.id.action_fragmentHome_to_fragmentAllFilms,
            bundleOf(FragmentAllFilms.KEY_FILM_CATEGORY to category)
        )
    }

    private fun stateLoadingListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadCategoryState.collect { state ->
                    Log.d(TAG, "State received: $state")
                    when (state) {
                        is StateLoading.Loading -> {
                            binding.progressGroup.isVisible = true
                            binding.loadingProgress.isVisible = true
                            binding.loadingRefreshBtn.isVisible = false
                            binding.categoryList.isVisible = false
                        }
                        is StateLoading.Success -> {
                            binding.progressGroup.isVisible = false
                            binding.categoryList.isVisible = true
                        }
                        else -> {
                            binding.progressGroup.isVisible = true
                            binding.loadingProgress.isVisible = false
                            binding.loadingRefreshBtn.isVisible = true
                            binding.categoryList.isVisible = false
                            binding.loadingRefreshBtn.setOnClickListener { viewModel.getFilmsByCategories() }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "FragmentHome"
    }
}
