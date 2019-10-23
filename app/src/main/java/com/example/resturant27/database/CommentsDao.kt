package com.example.resturant27.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommentsDao {
    @Insert
    suspend fun insertComment(comment: Comments)

    @Query("SELECT * FROM Comments WHERE aid = :aid ORDER BY upload_timestamp DESC")
    fun getAllCommentsOfArticle (aid : String) : LiveData<List<Comments>>


}