package com.github.hahmadfaiq21.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.databinding.FragmentHomeBinding
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import com.github.hahmadfaiq21.githubuser.helper.ViewModelFactory
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private var isFavorite = false
    private var currentUserId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        setupObservers()

        if (viewModel.randomUser.value == null) {
            viewModel.getRandomUser()
        }

        binding.btnClear.setOnClickListener {
            viewModel.getRandomUser()
            showLoading(true)
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfFavorite()
    }

    private fun setupObservers() {
        val repository = UserRepository(requireActivity().application)
        val factory = ViewModelFactory.getInstance(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        viewModel.randomUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                currentUserId = user.id
                updateUI(it)
            }
        }
    }

    private fun updateUI(user: DetailUserResponse) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvCompany.text = user.company
            tvLocation.text = user.location

            Glide.with(this@HomeFragment)
                .load(user.avatarUrl)
                .into(ivProfile)

            btnDetails.setOnClickListener {
                Intent(activity, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
                    putExtra(DetailUserActivity.EXTRA_ID, user.id)
                    putExtra(DetailUserActivity.EXTRA_URL, user.avatarUrl)
                    startActivity(this)
                }
            }

            btnFavorite.setOnClickListener {
                addToFavorite(user)
            }
        }
        showLoading(false)
    }

    private fun addToFavorite(user: DetailUserResponse) {
        isFavorite = !isFavorite
        viewModel.addToFavorite(user.id, user.login, user.avatarUrl)
        Snackbar.make(binding.root, "Added to Favorite", Snackbar.LENGTH_SHORT).show()
        viewModel.getRandomUser()
        showLoading(true)
    }

    private fun checkIfFavorite() {
        currentUserId?.let { id ->
            lifecycleScope.launch(Dispatchers.IO) {
                val count = viewModel.checkUser(id)
                withContext(Dispatchers.Main) {
                    isFavorite = count > 0
                    viewModel.getRandomUser()
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}