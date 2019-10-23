package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["follwer_uid", "followed_uid"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["uid"] , childColumns = ["follower_uid"]),
        ForeignKey(entity = User::class, parentColumns = ["uid"] , childColumns = ["followed_uid"])
    ]
)
class Friends (
    val follower_uid : String,
    val followed_uid : String
)