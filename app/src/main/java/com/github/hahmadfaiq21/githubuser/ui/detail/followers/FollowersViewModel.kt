package com.github.hahmadfaiq21.githubuser.ui.detail.followers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowersViewModel(private val userRepository: UserRepository) : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<UserResponse>>()

    fun setListFollowers(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRepository.getFollowers(username)
                if (response.isSuccessful) {
                    listFollowers.postValue(response.body())
                } else {
                    Log.e("API Error", response.errorBody()?.toString() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API Failure", "Error: ${e.message}")
            }
        }
    }
}