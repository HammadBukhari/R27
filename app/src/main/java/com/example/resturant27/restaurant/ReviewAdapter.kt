package com.example.resturant27.restaurant

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resturant27.R

class ReviewAdapter(val context : Context ,val reviews : MutableList<RestaurantReview>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_review,parent,false)
        return ViewHolder(view)
    }




    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReview = reviews[position]
        holder.v.findViewById<TextView>(R.id.tv_nearby_res_review_username).text = currentReview.userName
        holder.v.findViewById<TextView>(R.id.tv_nearby_res_review_text).text = currentReview.review
        val userDp = holder.v.findViewById<ImageView>(R.id.civ_nearby_res_review_dp)
        Glide.with(context)
            .load(Uri.parse(currentReview.userProfilePictureUrl))
            .placeholder(R.drawable.user_48px)
            .into(userDp)
        val ratingBar = holder.v.findViewById<RatingBar>(R.id.rb_nearby_res_review)
            ratingBar.stepSize = 0.1f
            ratingBar.rating =  (currentReview.rating)




    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}


//class ArticleAdapter (val context: Context): RecyclerView.Adapter<ArticleAdapter.ViewHolder>()
//{
//    var articleHeadersList = arrayListOf<ArticleHeader>()
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_list_item,parent,false)
//        return ViewHolder(view);
//    }
//
//    override fun getItemCount() = articleHeadersList.size
//
//    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
//        val currentHeader = articleHeadersList.get(position);
//        holder.v.findViewById<TextView>(R.id.tv_article_header_name_time).text = currentHeader.username;
////        holder.v.findViewById<ImageView>(R.id.iv_article_header_user_image).setImageResource(R.drawable.user_48px)
//        val ivProfilePic = holder.v.findViewById(R.id.iv_article_header_user_image) as ImageView
//        Glide.with(context).load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(ivProfilePic);
//        holder.v.findViewById<TextView>(R.id.tv_article_header_summary).text = currentHeader.summary
//        holder.v.findViewById<TextView>(R.id.tv_article_header_title).text = currentHeader.title
//    }
//    class ViewHolder (val v : View) : RecyclerView.ViewHolder(v)
//
//
//}