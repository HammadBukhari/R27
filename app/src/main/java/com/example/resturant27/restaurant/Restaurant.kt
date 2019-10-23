package com.example.resturant27.restaurant

data class Restaurant (
    val tag : String,
    val name : String? = null,
    val kidFriendly : Boolean = false,
    val location : HashMap<String,Any>,
    val price : Int? = 1,
    val rating : Int? = 0,
    val phone: Double? = null,
    val website : String? = null,
    val noiseLevel : Int? =  0,
    val userRatings : MutableList<RestaurantReview> = mutableListOf<RestaurantReview>()

)