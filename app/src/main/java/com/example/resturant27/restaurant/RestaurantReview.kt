package com.example.resturant27.restaurant

data class RestaurantReview (
    val userID : String? = null,
    val noisy: Boolean = false,
    val kidFriendly: Boolean = false,
    val review : String? = null,
    val userName: String? = null,
    val userProfilePictureUrl : String? = null,
    val rating: Float = 0f
)
