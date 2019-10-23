package com.example.resturant27.database

import androidx.room.*

@Dao
interface UserDao {
    @Query ("Select * FROM user WHERE uid = :uid")
    suspend fun getUserByUid (uid : String) : User?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser (user: User)
    @Delete
    suspend fun deleteUser (user: User)
    @Query ("select * FROM user WHERE update_timestamp = :updateTimestamp AND uid = :uid")
    suspend fun getUserByUpdateTimeStampAndUid ( updateTimestamp: Long , uid: String) : User?


}