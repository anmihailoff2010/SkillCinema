package com.example.skillcinema.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.skillcinema.databinding.FragmentGalleryFullscreenBinding
import com.example.skillcinema.ui.adapters.MyAdapterTypes
import com.example.skillcinema.ui.adapters.MyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentGalleryFullscreen : Fragment() {
    private var _binding: FragmentGalleryFullscreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelGalleryFullScreen by viewModels()
    private lateinit var adapter: MyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFullscreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentGalleryFullscreenArgs by navArgs()
        viewModel.getGallery(args.filmId, args.galleryType)

        setAdapter()                                        // set adapter
        setImagesList(args.position)                        // set images list
    }

    private fun setAdapter() {
        adapter = MyListAdapter(null, {}) {}
        PagerSnapHelper().attachToRecyclerView(binding.galleryImageFullscreenContainer)
        binding.galleryImageFullscreenContainer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.galleryImageFullscreenContainer.adapter = adapter
    }

    private fun setImagesList(position: Int) {
        Log.d("FragmentFullScreen", "setImagesList: $position")
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.images.collect {
                    if (it.isNotEmpty()) {
                        binding.galleryImageFullscreenContainer.scrollToPosition(position)
                        adapter.submitList(it.map { image -> MyAdapterTypes.ItemGalleryFullScreen(image) })
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}