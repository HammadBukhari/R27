package com.example.resturant27.directions

import com.google.android.gms.maps.model.LatLng
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.net.URL

class DistanceMatrixUtils {
    companion object{
        val DISTANCE_API_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix"
        val DISTANCE_API_OUTPUT_FORMAT = "json"
        val DISTANCE_API_ORIGINS_KEY = "origins"
        val DISTANCE_API_DESTINATIONS_KEY = "destinations"
        val DISTANCE_API_GOOGLE_MAPS_API_KEY = "key"
        val DISTANCE_API_GOOGLE_MAPS_API_VALUE = "AIzaSyA8hn0dzYiUfaKAiPrpp3HKsugkVq2WhX8"
        fun distanceHttpRequest (url : String) : String? {
            val httpClient = OkHttpClient()
            val request = Request.Builder()
                .url(URL(url))
                .build()
            var result : String? = null
            try {
                val response = httpClient.newCall(request).execute()
                result = response.body().string()
            }
            finally {
                return result
            }
        }
        fun buildDistanceMatrixUrl(origin : LatLng , destination : LatLng) : String?
                = HttpUrl.parse(DISTANCE_API_BASE_URL).newBuilder()
            .addPathSegment(DISTANCE_API_OUTPUT_FORMAT)
            .addQueryParameter(DISTANCE_API_ORIGINS_KEY,origin.latitude.toString() + "," + origin.longitude.toString())
            .addQueryParameter(DISTANCE_API_DESTINATIONS_KEY, destination.latitude.toString() + "," + destination.longitude.toString())
            .addQueryParameter(DISTANCE_API_GOOGLE_MAPS_API_KEY, DISTANCE_API_GOOGLE_MAPS_API_VALUE)
            .build()
            .toString()

    }


}