package com.example.aniplayer.features.list.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.aniplayer.MainActivity
import com.example.aniplayer.R
import com.example.aniplayer.databinding.FragmentGridItemsBinding
import com.example.aniplayer.features.list.adapter.BaseGridAdapter
import com.example.aniplayer.features.list.paging.PagingLoadStateAdapter
import com.example.aniplayer.features.list.viewmodel.BaseItemViewModel
import com.example.aniplayer.utils.CATEGORY_INDEX
import com.example.aniplayer.utils.DEFAULT_ERROR_MESSAGE
import com.example.aniplayer.utils.SITE_INDEX
import com.example.aniplayer.utils.fragment.goBackFragment
import com.example.aniplayer.utils.view.GridSpacingItemDecoration
import com.example.aniplayer.utils.view.ViewUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseGridFragment<T : Any, F> : Fragment() {
    private lateinit var binding: FragmentGridItemsBinding

    private lateinit var adapter: BaseGridAdapter<T>
    private lateinit var footerAdapter: LoadStateAdapter<*>
    protected abstract fun createAdapter(): BaseGridAdapter<T>

    protected abstract val viewModel: BaseItemViewModel<T, F>

    protected var categoryIndex: Int = 0
    protected var siteIndex: Int = 0

    open var spanCount: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryIndex = requireArguments().getInt(CATEGORY_INDEX)
        siteIndex = requireArguments().getInt(SITE_INDEX)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridItemsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToggleNavigationBar()
        setupPagingFlow()
        setupSearch()
    }

    private fun setupRecyclerView() {
        spanCount = ViewUtils.calculateSpanCount()
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)

        adapter = createAdapter()
        footerAdapter = PagingLoadStateAdapter()
        val concatAdapter = adapter.withLoadStateFooter(
            footer = footerAdapter
        )

        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == concatAdapter.itemCount - 1) {
                    if (footerAdapter.itemCount > 0) spanCount else 1
                } else {
                    1
                }
            }
        }

        binding.rvSiteData.layoutManager = layoutManager

        binding.rvSiteData.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
            )
        )

        binding.rvSiteData.adapter = concatAdapter
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
                        binding.progressBar.flProgressCircular.visibility = View.VISIBLE
                        binding.rvSiteData.visibility = View.GONE
                    }

                    is LoadState.NotLoading -> {
                        binding.progressBar.flProgressCircular.visibility = View.GONE
                        binding.rvSiteData.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        binding.progressBar.flProgressCircular.visibility = View.GONE
                        showError(state.error.message ?: DEFAULT_ERROR_MESSAGE)
                    }
                }
            }
        }
    }

    private fun setupSearch() {
        val searchItem = binding.toolBar.menu.findItem(R.id.miSearch)
        val searchView = searchItem.actionView as SearchView
        val searchPlate = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchPlate.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchView.query.toString()
                viewModel.updateFilter(getFilterQuery(query))

                val imm =
                    searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.windowToken, 0)
            }
            true
        }
    }

    abstract fun getFilterQuery(query: String): F

    private fun setupToggleNavigationBar() {
        binding.toolBar.setNavigationOnClickListener {
            goBack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            },
        )
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    fun goBack() {
        (activity as? MainActivity)?.toggleBottomNavigationView()
        goBackFragment()
    }

    companion object {
        fun <T : BaseGridFragment<*, *>> createSourceFragment(
            fragment: T,
            categoryIndex: Int,
            siteIndex: Int,
        ): T {
            return fragment.apply {
                arguments = Bundle().apply {
                    putInt(CATEGORY_INDEX, categoryIndex)
                    putInt(SITE_INDEX, siteIndex)
                }
            }
        }
    }

}
