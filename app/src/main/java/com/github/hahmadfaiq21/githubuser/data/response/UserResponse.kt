package com.github.hahmadfaiq21.githubuser.data.response

import com.github.hahmadfaiq21.githubuser.data.User
import com.google.gson.annotations.SerializedName

data class UserResponse  (
    @SerializedName("items")
    val items: ArrayList<User>
)