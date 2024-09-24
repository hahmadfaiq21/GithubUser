package com.github.hahmadfaiq21.githubuser.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.databinding.ActivityDetailUserBinding
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import com.github.hahmadfaiq21.githubuser.helper.ViewModelFactory
import com.github.hahmadfaiq21.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val repository = UserRepository(this.application)
        val factory = ViewModelFactory.getInstance(application, repository)
        detailUserViewModel = ViewModelProvider(this, factory)[DetailUserViewModel::class.java]
        username?.let { detailUserViewModel.setUserDetail(it) }

        binding.apply {
            btnBack.setOnClickListener { finish() }
            fab.setOnClickListener { toggleFavorite(id, username, avatarUrl) }
        }

        setupObservers()
        checkFavoriteStatus(id)
        setupViewPager(username)
    }

    private fun setupObservers() {
        detailUserViewModel.user.observe(this) { user ->
            user?.let {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivProfile)
                    tvName.text = it.name
                    tvBio.text = it.bio
                    tvUsername.text = it.login
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvCompany.text = it.company
                    tvLocation.text = it.location
                    tvRepository.text = it.publicRepos.toString()
                }
            }
        }
    }

    private fun checkFavoriteStatus(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val count = detailUserViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                isFavorite = count > 0
                toggleFavoriteIcon(binding.fab)
            }
        }
    }

    private fun toggleFavorite(id: Int, username: String?, avatarUrl: String?) {
        isFavorite = !isFavorite
        if (isFavorite) {
            detailUserViewModel.addToFavorite(id, username ?: "", avatarUrl ?: "")
            showSnackBar("Added to Favorite")
        } else {
            detailUserViewModel.removeFromFavorite(id)
            showSnackBar("Removed from Favorite")
        }
        toggleFavoriteIcon(binding.fab)
    }

    private fun toggleFavoriteIcon(fab: FloatingActionButton) {
        fab.setImageResource(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite)
    }

    private fun setupViewPager(username: String?) {
        val bundle = Bundle().apply { putString(EXTRA_USERNAME, username) }
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionPagerAdapter.getPageTitle(position, this)
        }.attach()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}
