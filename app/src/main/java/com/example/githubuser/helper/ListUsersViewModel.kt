package com.example.githubuser.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.service.ApiConfig
import com.example.githubuser.service.response.GetSearchListUsersResponse
import com.example.githubuser.service.response.UserGithub
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUsersViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<UserGithub>?>()
    val listUsers: MutableLiveData<List<UserGithub>?> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListUsers() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListUsers()
        client.enqueue(object : Callback<List<UserGithub>> {
            override fun onResponse(
                call: Call<List<UserGithub>>,
                response: Response<List<UserGithub>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        _listUsers.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<UserGithub>>, t: Throwable) {
                _isLoading.value = false

            }
        })
    }

    fun getSearchListUsers(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearchListUsers(username)
        client.enqueue(object : Callback<GetSearchListUsersResponse> {
            override fun onResponse(
                call: Call<GetSearchListUsersResponse>,
                response: Response<GetSearchListUsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = responseBody.items
                    }
                }
            }

            override fun onFailure(call: Call<GetSearchListUsersResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getListFollowers(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListFollowers(username)
        client.enqueue(object : Callback<List<UserGithub>> {
            override fun onResponse(
                call: Call<List<UserGithub>>,
                response: Response<List<UserGithub>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
//
                        _listUsers.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<UserGithub>>, t: Throwable) {
                _isLoading.value = false
//
            }
        })
    }

    fun getListFollowing(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object : Callback<List<UserGithub>> {
            override fun onResponse(
                call: Call<List<UserGithub>>,
                response: Response<List<UserGithub>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
//
                        _listUsers.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<UserGithub>>, t: Throwable) {
                _isLoading.value = false

            }
        })
    }
}