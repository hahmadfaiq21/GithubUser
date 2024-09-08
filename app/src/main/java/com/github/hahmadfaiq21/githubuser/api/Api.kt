package com.github.hahmadfaiq21.githubuser.api

import com.github.hahmadfaiq21.githubuser.BuildConfig
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>
}