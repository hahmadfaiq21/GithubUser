package com.github.hahmadfaiq21.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hahmadfaiq21.githubuser.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import com.github.hahmadfaiq21.githubuser.data.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    val listUsers = MutableLiveData<ArrayList<UserResponse>>()

    fun setSearchUsers(query: String) {
        RetrofitClient.apiInstance.getSearchUsers(query)
            .enqueue(object : Callback<Users> {
                override fun onResponse(
                    call: Call<Users>,
                    response: Response<Users>
                ) {
                    if (response.isSuccessful) {
                        Log.d("API Response", response.body()?.items.toString())
                        listUsers.postValue(response.body()?.items)
                    } else {
                        Log.e("API Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
    }

    fun getSearchUsers(): MutableLiveData<ArrayList<UserResponse>> {
        return listUsers
    }

}