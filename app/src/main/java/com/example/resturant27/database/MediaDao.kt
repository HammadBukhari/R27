package com.example.resturant27.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMedia (media: Media)

    @Query("SELECT * FROM Media WHERE aid = :articleId")
    suspend fun findMediaByArticleId (articleId: String) : List<Media>

}