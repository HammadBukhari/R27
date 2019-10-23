package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User
    (
    @PrimaryKey val uid : String,
    val name : String,
    val update_timestamp: Long,
    val photo_url: String,
    val desc : String) {
}