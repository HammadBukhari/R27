package com.example.resturant27.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class, Article::class, Media::class, Upvotes::class, Comments::class), version = 2)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun ArticleDao(): ArticleDao
    abstract fun MediaDao(): MediaDao
    abstract fun upvotesDao(): UpvotesDao
    abstract fun commentsDao() : CommentsDao
    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).apply { INSTANCE = this }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "Res27Local.db")
                .fallbackToDestructiveMigration()
                .build()
    }


}