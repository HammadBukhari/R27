package com.example.resturant27.aricle

data class Article(
    val id : Long? = null,
    val userID: String? = null,
    val userName: String? = null,
    val timestamp: Long? = null,
    val userProfilePictureUrl : String? = null,
    val title: String? = null,
    val articleText : String? = null
)