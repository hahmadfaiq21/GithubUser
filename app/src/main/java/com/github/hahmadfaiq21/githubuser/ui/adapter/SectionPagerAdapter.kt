package com.github.hahmadfaiq21.githubuser.ui.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.ui.detail.FollowersFragment
import com.github.hahmadfaiq21.githubuser.ui.detail.FollowingFragment

class SectionPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    @StringRes
    private val tabs = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    fun getPageTitle(position: Int, context: Context): CharSequence {
        return context.resources.getString(tabs[position])
    }
}