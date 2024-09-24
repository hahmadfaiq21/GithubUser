package com.github.hahmadfaiq21.githubuser.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val randomUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun getRandomUser() {
        val query = ('a'..'z').random().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favoriteUserIds = userDao?.getAllFavoriteUserIds() ?: emptyList()
                val response = RetrofitClient.apiInstance.getSearchUsers(query)

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
                        getRandomUser() // Try again if all users are favorite
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
            val response = RetrofitClient.apiInstance.getUserDetail(username)
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