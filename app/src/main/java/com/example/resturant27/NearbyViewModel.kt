package com.example.resturant27

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resturant27.restaurant.Restaurant
import com.example.resturant27.restaurant.RestaurantReview
import com.google.firebase.firestore.FirebaseFirestore

class NearbyViewModel : ViewModel() {
    public val  restaurants  : MutableLiveData<MutableList<Restaurant>> by lazy {   MutableLiveData<MutableList<Restaurant>>().also{
        it.postValue(mutableListOf())
        loadRestaurants()
    }}
    public val restaurantsMarker: HashMap<String,Restaurant> = hashMapOf()
    //= emptyList<Restaurant>() as LiveData<MutableList<Restaurant>>
    override fun onCleared() {
        super.onCleared()
    }
    private fun loadRestaurants ()
    {
        val firestore = FirebaseFirestore.getInstance();
        firestore.collection("restaurants")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (documents in querySnapshot)
                {
                    val userRatings = mutableListOf<RestaurantReview>()
                    val userRatingsFromFirestore =  documents.get("user_ratings") as List<HashMap<String, Any>>

                    for (review in userRatingsFromFirestore)
                    {
                        userRatings.add(

                            RestaurantReview(
                                userID = review.get("userID") as String,
                                userProfilePictureUrl = review.get("userProfilePictureUrl") as String,
                                userName = review.get("userName") as String,
                                rating = (review.get("rating") as Number).toFloat(),
                                review = review.get("review") as String,
                                kidFriendly = review.get("kidFriendly") as Boolean,
                                noisy = review.get("noisy") as Boolean
                            )
                        )
                    }
                    val restaurant = Restaurant(
                        tag = documents.id,
                        name = documents.get("name").toString(),
                        kidFriendly = documents.getBoolean("kid_friendly")!!,
                        location = documents.get("location") as HashMap<String, Any>,
                        noiseLevel = documents.getDouble("noise_level")?.toInt(),
                        phone = documents.getDouble("phone_no"),
                        price = documents.getLong("price")?.toInt(),
                        rating = documents.getLong("rating")?.toInt(),
                        website = documents.getString("website"),
                        userRatings = userRatings
                    )

                    restaurants.value!!.add(restaurant)
                    restaurants.notifyObserver()
                }
            }
    }
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
    //public fun getAllRestaurants() : MutableLiveData<MutableList<Restaurant>> = restaurants
}