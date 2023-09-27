package com.example.githubuser.service

import com.example.githubuser.service.response.GetDetailUserResponse
import com.example.githubuser.service.response.GetSearchListUsersResponse
import com.example.githubuser.service.response.UserGithub
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchListUsers(@Query("q") q: String): Call<GetSearchListUsersResponse>

    @GET("users")
    fun getListUsers(): Call<List<UserGithub>>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<GetDetailUserResponse>

    @GET("users/{username}/followers")
    fun getListFollowers(@Path("username") username: String) : Call<List<UserGithub>>

    @GET("users/{username}/following")
    fun getListFollowing(@Path("username") username: String) : Call<List<UserGithub>>
}