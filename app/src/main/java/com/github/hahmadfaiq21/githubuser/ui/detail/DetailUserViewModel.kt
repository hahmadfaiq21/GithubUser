package com.github.hahmadfaiq21.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.github.hahmadfaiq21.githubuser.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.response.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance.getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    } else {
                        Log.e("API Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
    }

    fun addToFavorite(username: String, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}