package com.github.hahmadfaiq21.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.helper.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserViewModel(application: Application, private val userRepository: UserRepository) :
    AndroidViewModel(application) {

    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRepository.getUserDetail(username)
                if (response.isSuccessful) {
                    user.postValue(response.body())
                } else {
                    Log.e("API Error", response.errorBody()?.toString() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API Failure", "Error: ${e.message}")
            }
        }
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = FavoriteUser(username, id, avatarUrl)
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao?.removeFromFavorite(id)
        }
    }
}