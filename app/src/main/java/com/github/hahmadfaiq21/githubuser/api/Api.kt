package com.github.hahmadfaiq21.githubuser.api

import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

}