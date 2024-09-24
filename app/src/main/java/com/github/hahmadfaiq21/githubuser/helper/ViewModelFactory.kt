package com.github.hahmadfaiq21.githubuser.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.hahmadfaiq21.githubuser.ui.detail.DetailUserViewModel
import com.github.hahmadfaiq21.githubuser.ui.detail.followers.FollowersViewModel
import com.github.hahmadfaiq21.githubuser.ui.detail.following.FollowingViewModel
import com.github.hahmadfaiq21.githubuser.ui.home.HomeViewModel
import com.github.hahmadfaiq21.githubuser.ui.search.SearchViewModel

class ViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(application, userRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(application, userRepository) as T
        } else if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            return FollowingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}