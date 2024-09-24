package com.github.hahmadfaiq21.githubuser.ui.detail.followers

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import com.github.hahmadfaiq21.githubuser.databinding.FragmentFollowBinding
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import com.github.hahmadfaiq21.githubuser.helper.ViewModelFactory
import com.github.hahmadfaiq21.githubuser.ui.adapter.UserAdapter
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFollowBinding.bind(view)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).orEmpty()

        setupRecyclerView()
        setupObservers()
        showLoading(true)
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                Intent(activity, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    putExtra(DetailUserActivity.EXTRA_URL, data.avatarUrl)
                    startActivity(this)
                }
            }
        })

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = this@FollowersFragment.adapter
        }
    }

    private fun setupObservers() {
        val favoriteDao = UserDatabase.getDatabase(requireContext())!!.favoriteUserDao()
        val repository = UserRepository(RetrofitClient.apiInstance, favoriteDao)
        val factory = ViewModelFactory.getInstance(requireActivity().application, repository)
        followersViewModel = ViewModelProvider(this, factory)[FollowersViewModel::class.java]
        followersViewModel.setListFollowers(username)
        followersViewModel.listFollowers.observe(viewLifecycleOwner) { followers ->
            followers?.let {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
