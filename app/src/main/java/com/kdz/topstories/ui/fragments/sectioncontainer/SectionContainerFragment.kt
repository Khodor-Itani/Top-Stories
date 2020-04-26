package com.kdz.topstories.ui.fragments.sectioncontainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kdz.topstories.R
import com.kdz.topstories.databinding.SectionContainerFragmentBinding
import com.kdz.topstories.ui.activities.main.SectionsPagerAdapter

class SectionContainerFragment : Fragment() {

    lateinit var binding: SectionContainerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SectionContainerFragmentBinding.inflate(inflater, container, false)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                activity as FragmentActivity,
                childFragmentManager
            )

        binding.viewPager.adapter = sectionsPagerAdapter

        binding.tabs.setupWithViewPager(binding.viewPager)

        return binding.root
    }
}