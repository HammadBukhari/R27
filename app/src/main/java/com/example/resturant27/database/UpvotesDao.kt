package com.example.resturant27.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UpvotesDao {


    @Query("SELECT COUNT(*) FROM Upvotes WHERE aid = :aid")
    fun getNoOfUpvotesByAid (aid : String) : LiveData<Int>


    @Insert()
    suspend fun addUpvote(upvote: Upvotes)
    @Query("SELECT * FROM Upvotes WHERE aid = :aid and uid = :uid")
    suspend fun getUpvoteOfUserByAid(uid : String, aid : String) : Upvotes?

    @Delete()
    suspend fun deleteUpvote(upvote: Upvotes)

    @Query("SELECT * FROM Upvotes WHERE aid = :aid and uid = :uid")
    fun getUpvoteOfUserByAidRx(aid : String , uid : String) : LiveData<Upvotes>
}