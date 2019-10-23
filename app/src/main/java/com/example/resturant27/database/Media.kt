package com.example.resturant27.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["aid"],
    foreignKeys = [
    ForeignKey(entity = Article::class, parentColumns = ["aid"], childColumns = ["aid"])]
)
class Media (
    val aid : String,
    val type : String,
    val url : String
){
}