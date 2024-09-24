package com.github.hahmadfaiq21.githubuser.ui.detail.followers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowersViewModel : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<UserResponse>>()

    fun setListFollowers(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiInstance.getFollowers(username)
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