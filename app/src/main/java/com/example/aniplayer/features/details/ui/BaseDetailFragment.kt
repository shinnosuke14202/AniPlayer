package com.example.aniplayer.features.details.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.aniplayer.R
import com.example.aniplayer.databinding.FragmentDetailBinding
import com.example.aniplayer.features.details.viewmodel.MangaDetailViewModel
import com.example.aniplayer.utils.CLASS_ITEM
import com.example.aniplayer.utils.CLASS_SITE
import com.example.aniplayer.utils.fragment.goBackFragment

abstract class BaseDetailFragment<T> : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    val binding get() = _binding

    abstract val viewModel: MangaDetailViewModel

    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBasicSetup()
        binding.toolbar.setNavigationOnClickListener {
            goBackFragment()
        }
    }

    private fun createBasicSetup() {
        binding.toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorCard
            )
        )
        binding.tvToggleDescription.setOnClickListener {
            binding.tvToggleDescription.text = "See Less"
            if (isExpanded) {
                binding.tvDescription.maxLines = 3 // collapse
                binding.tvToggleDescription.text = "See More"
            } else {
                binding.tvDescription.maxLines = Integer.MAX_VALUE // expand
            }
            isExpanded = !isExpanded
        }
    }

    companion object {
        fun <T : BaseDetailFragment<*>, I : Parcelable> createDetailFragment(
            fragment: T,
            item: I,
            site: I,
        ): T {
            return fragment.apply {
                arguments = Bundle().apply {
                    putParcelable(CLASS_ITEM, item)
                    putParcelable(CLASS_SITE, site)
                }
            }
        }
    }
}
