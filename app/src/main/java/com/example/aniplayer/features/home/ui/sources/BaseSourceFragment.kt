package com.example.aniplayer.features.home.ui.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.aniplayer.features.home.adapter.SiteAdapter
import com.example.aniplayer.site.Site
import com.example.aniplayer.utils.CATEGORY_INDEX
import com.example.aniplayer.utils.Inflate
import com.example.aniplayer.utils.Sources

abstract class BaseSourceFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private lateinit var _sites: List<Site>

    private var index: Int = 0

    abstract val listView: ListView
    abstract fun onSourceClick(categoryIndex: Int, siteIndex: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        index = requireArguments().getInt(CATEGORY_INDEX)
        _sites = Sources.allCategories[index].sources
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView.adapter = SiteAdapter(requireContext(), _sites)

        listView.setOnItemClickListener { _, _, position, _ -> onSourceClick(index, position) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun <T : BaseSourceFragment<*>> createSourceFragment(
            fragment: T,
            categoryIndex: Int
        ): T {
            return fragment.apply {
                arguments = Bundle().apply {
                    putInt(CATEGORY_INDEX, categoryIndex)
                }
            }
        }
    }
}
