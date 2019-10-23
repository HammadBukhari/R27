package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(primaryKeys = ["aid"],
    foreignKeys = [
        ForeignKey(entity = User::class , parentColumns = ["uid"], childColumns = ["owner_id"],onDelete = CASCADE)
    ]
)
class Article (
    val aid : String,
    val owner_id : String,
    val upload_timestamp : Long,
    var update_timestamp : Long,
    var body : String
)