package com.github.hahmadfaiq21.githubuser.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import com.github.hahmadfaiq21.githubuser.databinding.FragmentSearchBinding
import com.github.hahmadfaiq21.githubuser.ui.adapter.UserAdapter
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.apply {
            btnSearch.setOnClickListener {
                searchUser()
                hideKeyboard()
            }

            btnClear.setOnClickListener {
                etQuery.text?.clear()
                showLoading(false)
            }

            etQuery.addTextChangedListener {
                showLoading(true)
                val query = it?.toString()
                if (!query.isNullOrEmpty()) {
                    btnClear.visibility = View.VISIBLE
                    searchUser()
                } else {
                    btnClear.visibility = View.GONE
                    showLoading(false)
                }
            }

            etQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    hideKeyboard()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.listUsers.observe(viewLifecycleOwner) { users ->
            if (users != null) {
                Log.d("Users", users.toString())
                adapter.setList(users)
                showLoading(false)
            } else {
                Log.d("Users", "No data found")
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                Intent(activity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.rvUser.layoutManager = LinearLayoutManager(activity)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter
    }

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            viewModel.setSearchUsers(query)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        activity?.currentFocus?.let { view ->
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}