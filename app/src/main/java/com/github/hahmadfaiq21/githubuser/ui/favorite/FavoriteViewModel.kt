package com.github.hahmadfaiq21.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.hahmadfaiq21.githubuser.data.local.FavoriteUser
import com.github.hahmadfaiq21.githubuser.helper.UserRepository

class FavoriteViewModel(application: Application, private val userRepository: UserRepository) :
    AndroidViewModel(application) {

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> {
        return userRepository.getFavoriteUser()
    }
}