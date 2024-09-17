package com.github.hahmadfaiq21.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.hahmadfaiq21.githubuser.databinding.FragmentHomeBinding
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.randomUser.value == null) {
            showLoading(true)
            viewModel.getRandomUser()
        }

        viewModel.randomUser.observe(viewLifecycleOwner) { user ->
            showLoading(false)
            if (user != null) {
                binding.tvName.text = user.name
                binding.tvUsername.text = user.login
                binding.tvCompany.text = user.company
                binding.tvLocation.text = user.location
                binding.tvBio.text = user.bio

                val ivProfile = binding.ivProfile
                Glide.with(this)
                    .load(user.avatarUrl)
                    .into(ivProfile)
            }

            binding.btnDetails.setOnClickListener {
                Intent(activity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, user?.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, user?.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, user?.avatarUrl)
                    startActivity(it)
                }
            }
        }

        binding.btnClear.setOnClickListener {
            showLoading(true)
            viewModel.getRandomUser()
        }

        binding.btnFavorite.setOnClickListener {
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
