package com.example.aniplayer

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.example.aniplayer.databinding.ActivityMainBinding
import com.example.aniplayer.features.home.ui.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.selectedItemId = R.id.homeItem

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> replaceFragment(HomeFragment.newInstance())
            }
            true
        }

    }

    fun toggleBottomNavigationView() {
        if (binding.bottomNavigationView.isGone) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainFl, fragment).commit()
    }
}
