package com.example.resturant27.database


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity( primaryKeys = ["sender_uid","receiver_uid","action_id"],
        foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["sender_uid"], onDelete = CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["receiver_uid"], onDelete = CASCADE)
        ]
)
class Actions (
    val sender_uid : String,
    val receiver_uid : String,
    val action_id : String,
    val type : String,
    val timestamp: Long,
    val result : String
)