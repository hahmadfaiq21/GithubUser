package com.github.hahmadfaiq21.githubuser.data.remote.api

import com.github.hahmadfaiq21.githubuser.BuildConfig
import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getSearchUsers(
        @Query("q") query: String
    ): Response<Users>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): Response<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowers(
        @Path("username") username: String
    ): Response<ArrayList<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    suspend fun getFollowing(
        @Path("username") username: String
    ): Response<ArrayList<UserResponse>>
}