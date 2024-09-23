package com.github.hahmadfaiq21.githubuser.data.remote

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("followers_url")
    val followersUrl: String,

    @SerializedName("following_url")
    val followingUrl: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("bio")
    val bio: String,

    @SerializedName("public_repos")
    val publicRepos: Int,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int,

    @SerializedName("location")
    val location: String,

    @SerializedName("company")
    val company: String,
)