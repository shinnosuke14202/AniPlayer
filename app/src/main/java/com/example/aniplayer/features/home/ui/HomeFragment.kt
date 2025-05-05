package com.example.aniplayer.features.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aniplayer.databinding.FragmentHomeBinding
import com.example.aniplayer.features.home.ui.sources.AnimeSourceFragment
import com.example.aniplayer.features.home.ui.sources.MangaSourceFragment
import com.example.aniplayer.utils.Sources
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = SourceAdapter(this@HomeFragment)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = Sources.allCategories[position].name
        }.attach()
    }

    private inner class SourceAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = Sources.TOTAL

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MangaSourceFragment.newInstance(position)
                1 -> AnimeSourceFragment.newInstance(position)
                else -> throw IllegalArgumentException("Invalid tab position")
            }
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
