package com.github.hahmadfaiq21.githubuser.data

import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("items")
    val items: ArrayList<UserResponse>
)