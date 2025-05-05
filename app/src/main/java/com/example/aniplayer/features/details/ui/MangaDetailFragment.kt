package com.example.aniplayer.features.details.ui

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.aniplayer.R
import com.example.aniplayer.features.details.adapter.BaseEntryDetailAdapter
import com.example.aniplayer.features.details.adapter.MangaEntryDetailAdapter
import com.example.aniplayer.features.details.factory.MangaDetailViewModelFactory
import com.example.aniplayer.features.details.viewmodel.MangaDetailViewModel
import com.example.aniplayer.features.reader.ui.ReaderFragment
import com.example.aniplayer.model.manga.Manga
import com.example.aniplayer.model.manga.MangaChapter
import com.example.aniplayer.site.manga.MangaSite
import com.example.aniplayer.utils.CLASS_ITEM
import com.example.aniplayer.utils.CLASS_SITE
import com.example.aniplayer.utils.fragment.addFragment
import com.example.aniplayer.utils.fragment.replaceFragment
import com.example.aniplayer.utils.parcelable.parcelable
import com.example.aniplayer.utils.state.UiState

class MangaDetailFragment : BaseDetailFragment<MangaChapter>() {

    private lateinit var entryAdapter: MangaEntryDetailAdapter

    override val viewModel: MangaDetailViewModel
        get() = ViewModelProvider(
            this, MangaDetailViewModelFactory(site)
        )[MangaDetailViewModel::class.java]

    private lateinit var manga: Manga
    private lateinit var site: MangaSite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manga = requireArguments().parcelable<Manga>(CLASS_ITEM)
        site = requireArguments().parcelable<MangaSite>(CLASS_SITE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(manga)
        loadMangaDetail()
    }

    private fun loadMangaDetail() {
        viewModel.loadDetail(manga)
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.icProgressBar.flProgressCircular.visibility = View.VISIBLE
                    binding.tvToggleDescription.visibility = View.GONE
                }

                is UiState.Error -> {
                    binding.icProgressBar.flProgressCircular.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

                is UiState.Success<Manga> -> {
                    binding.icProgressBar.flProgressCircular.visibility = View.GONE
                    initView(it.data)
                    binding.llDetail.visibility = View.VISIBLE
                    binding.tvToggleDescription.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun createEntryAdapter(chapters: List<MangaChapter>?) {
        val list = chapters ?: listOf()
        entryAdapter = MangaEntryDetailAdapter(list) {
            replaceFragment(R.id.mainFl, ReaderFragment.newInstance(site, list, list.indexOf(it)), true)
        }
        binding.rvChapters.adapter = entryAdapter
    }

    private fun initView(manga: Manga) {
        binding.tvTitle.text = manga.title
        binding.toolbar.title = manga.title
        binding.tvAuthor.text = manga.authors.joinToString()
        if (manga.description != null) binding.tvDescription.text = manga.description
        if (manga.state != null) binding.tvStatus.text = manga.state.name
        binding.ivDetailImage.load(manga.coverUrl) {
            crossfade(true)
            placeholder(R.drawable.item_loading)
            error(R.drawable.item_error)
        }
        createEntryAdapter(manga.chapters)
    }

    companion object {
        fun newInstance(manga: Manga, site: MangaSite) =
            createDetailFragment(MangaDetailFragment(), manga, site as Parcelable)
    }
}
