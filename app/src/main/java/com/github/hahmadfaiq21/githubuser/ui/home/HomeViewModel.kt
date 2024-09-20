package com.github.hahmadfaiq21.githubuser.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.hahmadfaiq21.githubuser.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.response.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val randomUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
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
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteUserIds = userDao?.getAllFavoriteUserIds() ?: emptyList()
            RetrofitClient.apiInstance.getSearchUsers(query).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    if (response.isSuccessful) {
                        val users =
                            response.body()?.items?.filterNot { it.id in favoriteUserIds } // Filter users not in favorite
                        if (users.isNullOrEmpty()) {
                            randomUser.postValue(null)
                            Log.d(
                                "HomeViewModel",
                                "All users are already in favorites, fetching a new query."
                            )
                            getRandomUser()
                        } else {
                            val randomUser = users.random()
                            getUserDetail(randomUser.login)
                        }
                    } else {
                        Log.e("API Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
        }
    }

    private fun getUserDetail(username: String) {
        RetrofitClient.apiInstance.getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        randomUser.postValue(response.body())
                    } else {
                        Log.e("API Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
    }
}