package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["participant_1_uid","participant_2_uid","message_id"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["participant_1_uid"], onDelete = CASCADE ),
        ForeignKey(entity = User::class, parentColumns = ["uid"], childColumns = ["participant_2_uid"], onDelete = CASCADE )
    ]
)
class Message(
    val participant_1_uid : String,
    val participant_2_uid : String,
    val message_id : String,
    val upload_timestamp : String,
    val status : Int,
    val body : String,
    val type : String
)