package com.github.hahmadfaiq21.githubuser.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<UserResponse>>()

    fun setSearchUsers(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiInstance.getSearchUsers(query)
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                } else {
                    Log.e("API Error", response.errorBody()?.toString() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API Failure", "Error: ${e.message}")
            }
        }
    }
}
