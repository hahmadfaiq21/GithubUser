package com.github.hahmadfaiq21.githubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class FavoriteUser(
    val login: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val avatarUrl: String
) : Serializable
