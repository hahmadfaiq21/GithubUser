package com.github.hahmadfaiq21.githubuser.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.hahmadfaiq21.githubuser.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<UserResponse>>()

    fun setSearchUsers(query: String) {
        RetrofitClient.apiInstance.getSearchUsers(query)
            .enqueue(object : Callback<Users> {
                override fun onResponse(
                    call: Call<Users>,
                    response: Response<Users>
                ) {
                    if (response.isSuccessful) {
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
}
