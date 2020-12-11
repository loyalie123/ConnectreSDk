package com.loyalie.connectre.ui.rewards

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.loyalie.connectre.R
import java.util.*

class RewardsPagerAdapter(
    context: Context,
    fm: FragmentManager,
    val mFragments: ArrayList<Fragment>
) :
    FragmentStatePagerAdapter(fm) {

    private val mFragmentTitles: Array<String> =
        context.resources.getStringArray(R.array.rewards_tab)

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }
}