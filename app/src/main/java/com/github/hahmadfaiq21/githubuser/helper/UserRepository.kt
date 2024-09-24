package com.github.hahmadfaiq21.githubuser.helper

import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.remote.api.Api
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import retrofit2.Response

class UserRepository(private val api: Api) {
    suspend fun getSearchUser(username: String): Response<Users> {
        return api.getSearchUsers(username)
    }

    suspend fun getUserDetail(username: String): Response<DetailUserResponse> {
        return api.getUserDetail(username)
    }

    suspend fun getFollowers(username: String): Response<ArrayList<UserResponse>> {
        return api.getFollowers(username)
    }

    suspend fun getFollowing(username: String): Response<ArrayList<UserResponse>> {
        return api.getFollowing(username)
    }
}