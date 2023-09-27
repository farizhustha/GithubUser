package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUsers: FavoriteUsers)

    @Delete
    fun delete(favoriteUsers: FavoriteUsers)

    @Query("SELECT * FROM FavoriteUsers ORDER BY username ASC ")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUsers>>

    @Query("SELECT * FROM FavoriteUsers WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsers>

}