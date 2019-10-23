package com.example.resturant27.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAricle (article: Article)
    @Query("SELECT * FROM Article ORDER BY update_timestamp DESC")
    fun getAllArticle() : LiveData<List<Article>>
    @Query("SELECT MAX(upload_timestamp) FROM article LIMIT 1")
    suspend fun getLatestArticleUploadTime() : Long?

}