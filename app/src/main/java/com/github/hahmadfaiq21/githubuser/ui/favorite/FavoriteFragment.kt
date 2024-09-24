package com.github.hahmadfaiq21.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import com.github.hahmadfaiq21.githubuser.databinding.FragmentFavoriteBinding
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import com.github.hahmadfaiq21.githubuser.helper.ViewModelFactory
import com.github.hahmadfaiq21.githubuser.ui.adapter.UserAdapter
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeFavoriteUsers()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
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
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeFavoriteUsers() {
        val repository = UserRepository(requireActivity().application)
        val factory = ViewModelFactory.getInstance(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        viewModel.getFavoriteUser().observe(viewLifecycleOwner) {
            it?.let { users -> adapter.setList(mapList(users)) }
        }
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<UserResponse> {
        return users.map {
            UserResponse(it.id, it.login, it.avatarUrl)
        } as ArrayList<UserResponse>
    }

    override fun onResume() {
        super.onResume()
        observeFavoriteUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}