package com.loyalie.connectre.ui.project.location

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class LocationPagerAdapter(
    val context: Context,
    fm: FragmentManager,
    val mFragment: ArrayList<Fragment>,
    private val mFragmentTitles: List<String>
) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return mFragment[position]
    }

    override fun getCount(): Int {
        return mFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]

    }
}