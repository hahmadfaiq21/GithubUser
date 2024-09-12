package com.github.hahmadfaiq21.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUserDao
import com.github.hahmadfaiq21.githubuser.data.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
}