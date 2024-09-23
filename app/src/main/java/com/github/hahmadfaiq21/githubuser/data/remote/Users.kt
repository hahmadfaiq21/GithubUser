package com.github.hahmadfaiq21.githubuser.data.remote

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("items")
    val items: ArrayList<UserResponse>
)