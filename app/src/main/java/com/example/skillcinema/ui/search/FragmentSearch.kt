package com.example.skillcinema.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.data.ParamsFilterFilm
import com.example.skillcinema.databinding.FragmentSearchBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes
import com.example.skillcinema.ui.adapters.MyPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentSearch : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelSearch by activityViewModels()

    private val filmAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyPagingAdapter { onFilmClick(it) }
    }
    private val personAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyPagingAdapter { onPersonClick(it) }
    }
    private val myMergeAdapter = ConcatAdapter(personAdapter, filmAdapter)

    private var isEditFocused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setSearchString()
        observeViewModel()

        binding.searchFilterBtn.setOnClickListener { setSearchMenu(it) }
    }

    private fun onFilmClick(filmId: Int) {
        val action = FragmentSearchDirections.actionFragmentSearchToFragmentFilmDetail(filmId)
        findNavController().navigate(action)
    }

    private fun onPersonClick(personId: Int) {
        val action = FragmentSearchDirections.actionFragmentSearchToFragmentPersonDetail(personId)
        findNavController().navigate(action)
    }

    private fun setAdapter() {
        filmAdapter.addLoadStateListener { state ->
            val currentState = state.refresh
            val isLoading = currentState is LoadState.Loading
            binding.searchFilmList.isVisible = !isLoading
            binding.loadingProgress.isVisible = isLoading
            binding.searchProgressText.isVisible = !isLoading
            binding.searchProgressImage.isVisible = isLoading
            if (currentState is LoadState.Error) {
                binding.searchProgressText.text = getString(R.string.search_not_found)
            } else {
                binding.searchProgressText.text = ""
            }
        }
        binding.searchFilmList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchFilmList.adapter = myMergeAdapter
    }

    private fun setSearchString() {
        binding.searchMyField.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            binding.searchGroup.background = if (hasFocus) {
                isEditFocused = true
                ResourcesCompat.getDrawable(
                    resources, R.drawable.search_input_field_select, null
                )
            } else {
                ResourcesCompat.getDrawable(resources, R.drawable.search_input_field, null)
            }
        }

        binding.searchClearBtn.setOnClickListener {
            binding.searchMyField.text?.clear()
            val newFilter = viewModel.getFiltersFull().copy(keyword = "")
            viewModel.updateFiltersFull(newFilter)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchMyField.textChanges()
                .debounce(300)  // Debounce to limit frequent updates
                .collect { state ->
                    val keyword = binding.searchMyField.text.toString()
                    val newFilter = viewModel.getFiltersFull().copy(keyword = keyword)
                    viewModel.updateFiltersFull(newFilter)
                }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.filterFlow.collect {
                        personAdapter.refresh()
                        filmAdapter.refresh()
                        Log.d("FragmentSearch", "Filter applied, adapters refreshed")
                    }
                }
                launch {
                    viewModel.newFilms.collect { pagingData ->
                        Log.d("FragmentSearch", "New films received")
                        filmAdapter.submitData(pagingData.map { film -> MyAdapterTypes.ItemSearchFilms(film) })
                    }
                }
                launch {
                    viewModel.persons.collect { pagingData ->
                        Log.d("FragmentSearch", "New persons received")
                        personAdapter.submitData(pagingData.map { person -> MyAdapterTypes.ItemSearchPersons(person) })
                    }
                }
            }
        }
    }

    private fun setSearchMenu(view: View) {
        val popUpMenu = PopupMenu(requireContext(), view)
        popUpMenu.inflate(R.menu.search_menu)

        popUpMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.search_settings_set -> {
                    findNavController().navigate(R.id.action_fragmentSearch_to_fragmentSearchSettings)
                    true
                }
                R.id.search_filters_clear -> {
                    viewModel.updateFiltersFull(ParamsFilterFilm())
                    binding.searchMyField.text?.clear()
                    Toast.makeText(
                        requireActivity().applicationContext,
                        getString(R.string.search_clear_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                else -> false
            }
        }

        popUpMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun EditText.textChanges(): Flow<Boolean> {
        return callbackFlow {
            val watcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    // No operation
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    trySend(s?.isNotEmpty() == true)
                }

                override fun afterTextChanged(s: Editable?) {
                    // No operation
                }
            }
            addTextChangedListener(watcher)
            awaitClose { removeTextChangedListener(watcher) }
        }.debounce(300)  // Debounce to limit frequent updates
            .onStart { emit(false) }
    }
}
