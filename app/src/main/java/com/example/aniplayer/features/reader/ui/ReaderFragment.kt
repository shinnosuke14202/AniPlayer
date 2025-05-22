package com.example.aniplayer.features.reader.ui

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aniplayer.databinding.FragmentReaderBinding
import com.example.aniplayer.features.list.paging.PagingLoadStateAdapter
import com.example.aniplayer.features.reader.adapter.ReaderAdapter
import com.example.aniplayer.features.reader.viewmodel.ReaderViewModel
import com.example.aniplayer.features.reader.viewmodel.ReaderViewModelFactory
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.CHAPTER_NUMBER
import com.example.aniplayer.utils.CLASS_ITEM
import com.example.aniplayer.utils.CLASS_SITE
import com.example.aniplayer.utils.DEFAULT_ERROR_MESSAGE
import com.example.aniplayer.utils.fragment.goBackFragment
import com.example.aniplayer.utils.parcelable.parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class ReaderFragment : Fragment() {

    private lateinit var binding: FragmentReaderBinding

    private lateinit var chapters: List<MangaChapter>
    private lateinit var site: MangaSite
    private lateinit var manga: Manga
    private var position: Int = 0

    private lateinit var adapter: ReaderAdapter
    private lateinit var footerAdapter: LoadStateAdapter<*>
    private lateinit var headerAdapter: LoadStateAdapter<*>

    private lateinit var viewModel: ReaderViewModel

    private var showAppBar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        site = requireArguments().parcelable<MangaSite>(CLASS_SITE)
        manga = requireArguments().parcelable<Manga>(CLASS_ITEM)
        chapters = manga.chapters ?: emptyList()
        position = requireArguments().getInt(CHAPTER_NUMBER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentReaderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this, ReaderViewModelFactory(site, chapters, position)
        )[ReaderViewModel::class.java]

        setupAppBar()
        setupRecyclerView()
        setupPagingFlow()
        setupScrollListener()
    }

    private fun setupScrollListener() {

        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // Update chapter when scroll stops for better performance
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateCurrentChapterInfo()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Optional: Update in real-time while scrolling
                if (abs(dy) > 50) { // Only update on significant scroll
                    updateCurrentChapterInfo()
                }
            }
        })
    }

    private fun updateCurrentChapterInfo() {
        val layoutManager = binding.rvImages.layoutManager as LinearLayoutManager
        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

        val snapshot = adapter.snapshot()
        if (firstVisiblePosition >= 0 && firstVisiblePosition < snapshot.size) {
            snapshot[firstVisiblePosition]?.let { page ->
                if (binding.toolbar.subtitle.toString() != page.chapterTitle) {
                    binding.toolbar.subtitle = page.chapterTitle
                }
            }
        }
    }

    private fun setupAppBar() {
        binding.toolbar.title = manga.title
        binding.toolbar.subtitle = chapters[position].title
        binding.toolbar.setNavigationOnClickListener {
            goBackFragment()
        }
    }

    private fun setupRecyclerView() {
        adapter = ReaderAdapter {
            toggleAppBar()
        }
        footerAdapter = PagingLoadStateAdapter()
        headerAdapter = PagingLoadStateAdapter()
        val concatAdapter = adapter.withLoadStateHeaderAndFooter(
            footer = footerAdapter, header = headerAdapter
        )
        binding.rvImages.adapter = concatAdapter
    }

    private fun toggleAppBar() {
        if (showAppBar) {
            binding.appBarLayout.visibility = View.GONE
        } else {
            binding.appBarLayout.visibility = View.VISIBLE
        }
        showAppBar = !showAppBar
    }

    private fun setupPagingFlow() {
        collectPagingData()
        observeLoadStates()
    }

    private fun collectPagingData() {
        lifecycleScope.launch(Dispatchers.IO) {
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
                        binding.icProgressBar.flProgressCircular.visibility = View.VISIBLE
                        binding.rvImages.visibility = View.GONE
                    }

                    is LoadState.NotLoading -> {
                        binding.icProgressBar.flProgressCircular.visibility = View.GONE
                        binding.rvImages.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.error.message ?: DEFAULT_ERROR_MESSAGE,
                            Toast.LENGTH_LONG
                        ).show()

                        binding.icProgressBar.flProgressCircular.visibility = View.GONE
                        binding.rvImages.visibility = View.GONE
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(
            site: MangaSite, manga: Manga, position: Int
        ): ReaderFragment {
            return ReaderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CLASS_SITE, site as Parcelable)
                    putParcelable(CLASS_ITEM, manga as Parcelable)
                    putInt(CHAPTER_NUMBER, position)
                }
            }
        }
    }
}
