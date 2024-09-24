package com.github.hahmadfaiq21.githubuser.helper

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.hahmadfaiq21.githubuser.data.Users
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase
import com.github.hahmadfaiq21.githubuser.data.remote.api.Api
import com.github.hahmadfaiq21.githubuser.data.remote.api.RetrofitClient
import com.github.hahmadfaiq21.githubuser.data.remote.response.DetailUserResponse
import com.github.hahmadfaiq21.githubuser.data.remote.response.UserResponse
import retrofit2.Response

class UserRepository(application: Application) {

    private val api: Api = RetrofitClient.apiInstance
    private val favoriteUserDao: FavoriteUserDao

    init {
        val db = UserDatabase.getDatabase(application)!!
        favoriteUserDao = db.favoriteUserDao()
    }

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

    suspend fun addToFavorite(id: Int, username: String, avatarUrl: String) {
        val favoriteUser = FavoriteUser(id, username, avatarUrl)
        return favoriteUserDao.addToFavorite(favoriteUser)
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getFavoriteUser()
    }

    suspend fun checkUser(id: Int): Int {
        return favoriteUserDao.checkUser(id)
    }

    suspend fun removeFromFavorite(id: Int) {
        favoriteUserDao.removeFromFavorite(id)
    }

    suspend fun getAllFavoriteUserIds(): List<Int> {
        return favoriteUserDao.getAllFavoriteUserIds()
    }
}