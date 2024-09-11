package com.github.hahmadfaiq21.githubuser.ui.detail

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.databinding.ActivityDetailUserBinding
import com.github.hahmadfaiq21.githubuser.ui.adapter.SectionPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        if (username != null) {
            detailUserViewModel.setUserDetail(username)
        }

        detailUserViewModel.user.observe(this) {
            if (it != null) {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
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

        CoroutineScope(Dispatchers.IO).launch {
            val count = detailUserViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.fab.setImageResource(R.drawable.ic_favorite)
                        isFavorite = true
                    } else {
                        binding.fab.setImageResource(R.drawable.ic_unfavorite)
                        isFavorite = false
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                detailUserViewModel.addToFavorite(username.toString(), id)
                showSnackBar("Added to Favorite")
            } else {
                detailUserViewModel.removeFromFavorite(id)
                showSnackBar("Removed from Favorite")
            }
            toggleFavoriteIcon(binding.fab)
        }


        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = sectionPagerAdapter.getPageTitle(position, this)
        }.attach()
    }

    private fun toggleFavoriteIcon(fab: FloatingActionButton) {
        if (isFavorite) {
            fab.setImageResource(R.drawable.ic_favorite)
        } else {
            fab.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
    }

}