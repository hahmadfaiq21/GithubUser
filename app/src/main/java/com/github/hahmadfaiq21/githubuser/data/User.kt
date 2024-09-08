package com.github.hahmadfaiq21.githubuser.data

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,
)
