package com.example.githubuser.helper

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.repository.FavoriteUsersRepository
import com.example.githubuser.service.ApiConfig
import com.example.githubuser.service.response.GetDetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _user = MutableLiveData<GetDetailUserResponse?>()
    val user: LiveData<GetDetailUserResponse?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavoriteUsersRepository: FavoriteUsersRepository =
        FavoriteUsersRepository(application)

    fun insert(favoriteUsers: FavoriteUsers) {
        mFavoriteUsersRepository.insert(favoriteUsers)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsers> =
        mFavoriteUsersRepository.getFavoriteUserByUsername(username)

    fun delete(favoriteUsers: FavoriteUsers) {
        mFavoriteUsersRepository.delete(favoriteUsers)
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<GetDetailUserResponse> {
            override fun onResponse(
                call: Call<GetDetailUserResponse>,
                response: Response<GetDetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<GetDetailUserResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}