package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["uid","aid","comment_id"],
    foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["uid"], onDelete = CASCADE),
    ForeignKey(entity = Article::class, parentColumns = ["aid"], childColumns = ["aid"], onDelete = CASCADE)]

)
class Comments(
    val uid : String,
    val aid : String,
    val comment_id : String,
    val upload_timestamp : Long,
    var body : String
)