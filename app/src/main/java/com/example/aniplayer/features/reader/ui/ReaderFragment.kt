package com.example.aniplayer.features.reader.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.aniplayer.databinding.FragmentReaderBinding
import com.example.aniplayer.features.list.paging.PagingLoadStateAdapter
import com.example.aniplayer.features.reader.adapter.ReaderAdapter
import com.example.aniplayer.features.reader.viewmodel.ReaderViewModel
import com.example.aniplayer.features.reader.viewmodel.ReaderViewModelFactory
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.CHAPTER_NUMBER
import com.example.aniplayer.utils.CLASS_SITE
import com.example.aniplayer.utils.DEFAULT_ERROR_MESSAGE
import com.example.aniplayer.utils.LIST_CHAPTERS
import com.example.aniplayer.utils.fragment.goBackFragment
import com.example.aniplayer.utils.parcelable.parcelable
import com.example.aniplayer.utils.parcelable.parcelableArrayList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReaderFragment : Fragment() {

    private lateinit var binding: FragmentReaderBinding

    private lateinit var chapters: List<MangaChapter>
    private lateinit var site: MangaSite
    private var position: Int = 0

    private lateinit var adapter: ReaderAdapter
    private lateinit var footerAdapter: LoadStateAdapter<*>

    private lateinit var viewModel: ReaderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chapters = requireArguments().parcelableArrayList<MangaChapter>(LIST_CHAPTERS).toList()
        site = requireArguments().parcelable(CLASS_SITE)
        position = requireArguments().getInt(CHAPTER_NUMBER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ReaderViewModelFactory(site, chapters, position)
        )[ReaderViewModel::class.java]

        setupRecyclerView()
        setupPagingFlow()

        binding.toolbar.setNavigationOnClickListener {
            goBackFragment()
        }
    }

    private fun setupRecyclerView() {
        adapter = ReaderAdapter()
        footerAdapter = PagingLoadStateAdapter()
        val concatAdapter = adapter.withLoadStateFooter(
            footer = footerAdapter
        )
        binding.rvImages.adapter = concatAdapter
    }

    private fun setupPagingFlow() {
        collectPagingData()
        observeLoadStates()
    }

    private fun collectPagingData() {
        lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun observeLoadStates() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (val state = loadStates.refresh) {
                    is LoadState.Loading -> {

                    }

                    is LoadState.NotLoading -> {

                    }

                    is LoadState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.error.message ?: DEFAULT_ERROR_MESSAGE,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(
            site: MangaSite, chapters: List<MangaChapter>, position: Int
        ): ReaderFragment {
            return ReaderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CLASS_SITE, site as Parcelable)
                    putParcelableArrayList(LIST_CHAPTERS, ArrayList(chapters))
                    putInt(CHAPTER_NUMBER, position)
                }
            }
        }
    }
}
