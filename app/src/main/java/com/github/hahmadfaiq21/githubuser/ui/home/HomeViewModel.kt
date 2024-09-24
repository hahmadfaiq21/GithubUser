package com.github.hahmadfaiq21.githubuser.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application, private val userRepository: UserRepository) :
    AndroidViewModel(application) {

    val randomUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()

    fun addToFavorite(id: Int, username: String, avatarUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addToFavorite(id, username, avatarUrl)
        }
    }

    suspend fun checkUser(id: Int): Int {
        return userRepository.checkUser(id)
    }

    fun getRandomUser() {
        val query = ('a'..'z').random().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteUserIds = userRepository.getAllFavoriteUserIds()
                val response = userRepository.getSearchUser(query)
                if (response.isSuccessful) {
                    val users = response.body()?.items?.filterNot { it.id in favoriteUserIds }
                    if (!users.isNullOrEmpty()) {
                        val randomUser = users.random()
                        getUserDetail(randomUser.login)
                    } else {
                        Log.d(
                            "HomeViewModel",
                            "All users are already in favorites, fetching a new query."
                        )
                        getRandomUser()
                    }
                } else {
                    Log.e("API Error", response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("API Failure", "Error: ${e.message}")
            }
        }
    }

    private suspend fun getUserDetail(username: String) {
        try {
            val response = userRepository.getUserDetail(username)
            if (response.isSuccessful) {
                randomUser.postValue(response.body())
            } else {
                Log.e("API Error", response.errorBody().toString())
            }
        } catch (e: Exception) {
            Log.e("API Failure", "Error: ${e.message}")
        }
    }
}