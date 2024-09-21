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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()
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
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearch() {
        binding.apply {
            btnSearch.setOnClickListener {
                searchUser()
                hideKeyboard()
            }

            btnClear.setOnClickListener {
                etQuery.text?.clear()
                showLoading(false)
            }

            etQuery.addTextChangedListener { text ->
                showLoading(true)
                btnClear.visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.GONE
                if (!text.isNullOrEmpty()) searchUser() else showLoading(false)
            }

            etQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    hideKeyboard()
                    true
                } else false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.listUsers.observe(viewLifecycleOwner) { users ->
            users?.let {
                adapter.setList(it)
                showLoading(false)
            } ?: Log.d("Users", "No data found")
        }
    }

    private fun searchUser() {
        val query = binding.etQuery.text.toString()
        if (query.isNotEmpty()) viewModel.setSearchUsers(query)
    }

    private fun hideKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
