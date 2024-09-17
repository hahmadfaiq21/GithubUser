package com.github.hahmadfaiq21.githubuser.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hahmadfaiq21.githubuser.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.response.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    val randomUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()

    fun getRandomUser() {
        val query = ('a'..'z').random().toString()

        RetrofitClient.apiInstance.getSearchUsers(query).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    if (!users.isNullOrEmpty()) {
                        val randomUser = users.random()
                        getUserDetail(randomUser.login)
                    } else {
                        randomUser.postValue(null)
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
                        randomUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    randomUser.postValue(null)
                }
            })
    }
}