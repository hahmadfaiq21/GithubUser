package com.github.hahmadfaiq21.githubuser.ui.detail.following

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hahmadfaiq21.githubuser.R
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import com.github.hahmadfaiq21.githubuser.databinding.FragmentFollowBinding
import com.github.hahmadfaiq21.githubuser.ui.adapter.UserAdapter
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFollowBinding.bind(view)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).orEmpty()

        setupRecyclerView()
        setupViewModel()
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
            adapter = this@FollowingFragment.adapter
        }
    }

    private fun setupViewModel() {
        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
        followingViewModel.setListFollowing(username)
        followingViewModel.listFollowing.observe(viewLifecycleOwner) { following ->
            following?.let {
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
