package com.example.resturant27

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturant27.directions.DistanceMatrixUtils
import com.example.resturant27.restaurant.Restaurant
import com.example.resturant27.restaurant.RestaurantReview
import com.example.resturant27.restaurant.ReviewAdapter
import com.example.resturant27.restaurant.ReviewEditorActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import org.json.JSONObject


class NearbyFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener
{

    private lateinit var mMap: GoogleMap

    private lateinit var viewModel: NearbyViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userCurrentLocation : Location? = null
    private lateinit var detailCardView : CardView
    private lateinit var userReviewsRecyclerView: RecyclerView
    private lateinit var userReviewViewAdapter: ReviewAdapter
    private lateinit var userReviewLayoutManager : RecyclerView.LayoutManager
    companion object {
        private val locationPermissionRequestCode = 201
        public val RC_REVIEW_EDITOR_DATA = 2
        public val INTENT_REVIEW_EDITOR_RB_KEY = "rb_value"
        public val INTENT_REVIEW_EDITOR_PLACE_NAME = "place_name"
        val INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID = "document_id"
        var IS_RESTAURANT_CLICKED = false
    }


    override fun onMarkerClick(selectedMarker: Marker?): Boolean {
        IS_RESTAURANT_CLICKED = true
        detailCardView = view?.findViewById(R.id.cv_nearby_res)!!
        detailCardView.visibility = View.VISIBLE
        detailCardView.findViewById<ProgressBar>(R.id.pb_nearby_res).visibility = View.VISIBLE
        val selectedRestaurant = viewModel.restaurantsMarker.get(selectedMarker?.tag as String)

        val origin = LatLng(userCurrentLocation?.latitude!!,userCurrentLocation?.longitude!!)
        val destination = selectedMarker?.position!!
        val url = DistanceMatrixUtils.buildDistanceMatrixUrl(origin,destination)
//        doAsync {
//
//            val result = DistanceMatrixUtils.distanceHttpRequest(url!!)
//            uiThread {
//            displayDetailedRestaurantView(result!!,selectedRestaurant!!)
//            }
//        }
        return true
    }
    private fun displayDetailedRestaurantView(distanceJSON : String , restaurant: Restaurant)
    {
        detailCardView.findViewById<ProgressBar>(R.id.pb_nearby_res).visibility = View.GONE
        val jsonObject = JSONObject(distanceJSON);
        if (jsonObject.get("status") == "OK")
        {
            val dataRow = jsonObject.getJSONArray("rows").get(0) as JSONObject
            val data = dataRow.getJSONArray("elements").get(0) as JSONObject
            val distance = data.getJSONObject("distance").getString("text")
            val time = data.getJSONObject("duration").getString("text")
            detailCardView.findViewById<TextView>(R.id.tv_nearby_res_desc).text = distance + "--" + time

        }
//        tv_nearby_res_name?.text = restaurant.name
        view?.findViewById<TextView>(R.id.tv_nearby_res_name)?.text = restaurant.name
        view?.findViewById<TextView>(R.id.tv_nearby_res_phone)?.text = restaurant.phone?.toLong().toString()
//        tv_nearby_res_phone?.text = restaurant.phone?.toLong().toString()
        view?.findViewById<RatingBar>(R.id.rb_res_main)?.apply { rating = restaurant.rating?.toFloat()?:5.0f; visibility = View.VISIBLE }
//        rb_res_main?.rating = restaurant.rating?.toFloat()?:5.0f

//        rb_res_main?.visibility = View.VISIBLE
        view?.findViewById<MaterialRatingBar>(R.id.rb_nearby_user_review)?.setOnRatingChangeListener { ratingBar, rating ->
            val dataIntent = Intent(this.activity,ReviewEditorActivity::class.java)
            dataIntent
                .putExtra(INTENT_REVIEW_EDITOR_RB_KEY,rating)
                .putExtra(INTENT_REVIEW_EDITOR_PLACE_NAME,restaurant.name)
                .putExtra(INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID,restaurant.tag)
            startActivityForResult(dataIntent, RC_REVIEW_EDITOR_DATA)
        }
//        rb_nearby_user_review.setOnRatingChangeListener { ratingBar, rating ->
//            val dataIntent = Intent(this.activity,ReviewEditorActivity::class.java)
//            dataIntent
//                .putExtra(INTENT_REVIEW_EDITOR_RB_KEY,rating)
//                .putExtra(INTENT_REVIEW_EDITOR_PLACE_NAME,restaurant.name)
//                .putExtra(INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID,restaurant.tag)
//            startActivityForResult(dataIntent, RC_REVIEW_EDITOR_DATA)
//        }

        userReviewsRecyclerView = view?.findViewById<RecyclerView>(R.id.rv_nearby_res_main)!!.apply {
            setHasFixedSize(true)
            userReviewViewAdapter = ReviewAdapter(context,restaurant.userRatings)
            userReviewLayoutManager = LinearLayoutManager(context)
            layoutManager = userReviewLayoutManager
            adapter = userReviewViewAdapter

        }

    }
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        requestLocation()
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_food_48px)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").icon(bitmapDescriptor))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        val polygon = googleMap.addPolygon(PolygonOptions()
//            .clickable(true)
//            .add(LatLng(-35.016, 143.321),
//             LatLng(-34.747, 145.592),
//             LatLng(-34.364, 147.891),
//             LatLng(-33.501, 150.217),
//            LatLng(-32.306, 149.248),
//            LatLng(-32.491, 147.309)))
//        polygon.tag = "A"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(R.layout.fragment_nearby,container,false);

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager?.findFragmentById(R.id.map) as? SupportMapFragment
//        val mapFragment = fragManager?.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)



    }

    @SuppressLint("MissingPermission")
    private fun enableLocationFeatures()
    {
        mMap.isMyLocationEnabled = true
        // getting last known location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity!!)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                location?.apply {
                    userCurrentLocation = this
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location?.latitude!!,location?.longitude!!)))
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                latitude,
                                longitude
                            ), 13f
                        )
                    )
                }

            }
        getRestaurants()
        // Initing ViewModel
        viewModel = ViewModelProviders.of(activity!!)[NearbyViewModel::class.java]
        viewModel.restaurants.observe(activity!!, Observer<MutableList<Restaurant>> {restaurants ->
            if (restaurants.isNotEmpty())
            {
                for (unit in restaurants)
                {
                    val latLng = LatLng(unit.location.get("latitude") as Double,unit.location.get("longitude") as Double)
                    val bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_food_48px)
                    val marker = mMap.addMarker(MarkerOptions()
                        .icon(bitmapDescriptor)
                        .position(latLng)
                        .title(unit.name))
                    marker.tag = unit.tag
                    viewModel.restaurantsMarker.put(marker.tag as String, unit)
                }
            }
        })






    }
    private fun getRestaurants()
    {

    }
    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode)
        }
        else
        {
            // already have permissions
            enableLocationFeatures()
        }

    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            locationPermissionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted
                    enableLocationFeatures()

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_REVIEW_EDITOR_DATA -> {
                if (resultCode == Activity.RESULT_OK)
                {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userRestaurantReview = RestaurantReview(
                        userID = user?.uid,
                        noisy = data?.getBooleanExtra(ReviewEditorActivity.INTENT_EXTRA_REVIEW_Q2_key,false)!!,
                        kidFriendly = data?.getBooleanExtra(ReviewEditorActivity.INTENT_EXTRA_REVIEW_Q1_KEY,false)!!,
                        review= data?.getStringExtra(ReviewEditorActivity.INTENT_EXTRA_REVIEW_TEXT_KEY),
                        rating = data?.getFloatExtra(ReviewEditorActivity.INTENT_EXTRA_REVIEW_RATING,0f),
                        userName = user?.displayName,
                        userProfilePictureUrl = user?.photoUrl.toString()
                    )
                    val firestoreInstance = FirebaseFirestore.getInstance()
                    firestoreInstance.collection("restaurants").document(data?.getStringExtra(
                        INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID)!!)
                        .update("user_ratings",FieldValue.arrayUnion(userRestaurantReview))
                        .addOnSuccessListener { void ->
                            Toast.makeText(this.context," posted", Toast.LENGTH_LONG).show()
                            userReviewViewAdapter.reviews.add(userRestaurantReview)
                            userReviewViewAdapter.notifyDataSetChanged()
                        }
                }
                else if (resultCode == Activity.RESULT_CANCELED)
                {
                    Toast.makeText(this.context,"Review posting is cancelled", Toast.LENGTH_LONG).show()
                }

            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}