package com.github.hahmadfaiq21.githubuser.ui.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.ui.detail.followers.FollowersFragment
import com.github.hahmadfaiq21.githubuser.ui.detail.following.FollowingFragment

class SectionPagerAdapter(fa: FragmentActivity, data: Bundle) : FragmentStateAdapter(fa) {

    private var fragmentBundle: Bundle = data

    @StringRes
    private val tabs = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
        fragment.arguments = this.fragmentBundle
        return fragment
    }

    fun getPageTitle(position: Int, context: Context): CharSequence {
        return context.resources.getString(tabs[position])
    }
}