package com.github.hahmadfaiq21.githubuser.ui.detail.following

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<UserResponse>>()

    fun setListFollowing(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiInstance.getFollowing(username)
                if (response.isSuccessful) {
                    listFollowing.postValue(response.body())
                } else {
                    Log.e("API Error", response.errorBody()?.toString() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API Failure", "Error: ${e.message}")
            }
        }
    }
}