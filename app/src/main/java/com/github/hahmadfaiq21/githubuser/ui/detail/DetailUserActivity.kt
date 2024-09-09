package com.github.hahmadfaiq21.githubuser.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.hahmadfaiq21.githubuser.databinding.ActivityDetailUserBinding
import com.github.hahmadfaiq21.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailUserViewModel.setUserDetail(username)
        }
        detailUserViewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivProfile)
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvBio.text = it.bio
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvCompany.text = it.company
                    tvLocation.text = it.location
                    tvRepository.text = it.publicRepos.toString()
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionPagerAdapter.getPageTitle(position, this)
        }.attach()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

}