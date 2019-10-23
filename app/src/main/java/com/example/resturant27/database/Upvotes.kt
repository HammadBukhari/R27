package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["uid","aid"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["uid"], onDelete = CASCADE),
        ForeignKey(entity = Article::class, parentColumns = ["aid"], childColumns = ["aid"], onDelete = CASCADE)]
)
class Upvotes (
    val uid : String,
    val aid : String
)