package com.github.hahmadfaiq21.githubuser.ui.detail.following

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<UserResponse>>()

    fun setListFollowing(username: String) {
        RetrofitClient.apiInstance.getFollowing(username)
            .enqueue(object : Callback<ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserResponse>>,
                    response: Response<ArrayList<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    } else {
                        Log.e("API Error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
    }
}