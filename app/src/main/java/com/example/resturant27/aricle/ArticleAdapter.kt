//package com.example.resturant27.aricle
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.resturant27.R
//import com.example.resturant27.utils.TimeAgo
//
//
//class ArticleAdapter (val context : Context, val articles : MutableList<Article>,val onClickListener: View.OnClickListener): RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_header,parent,false)
//        view.setOnClickListener(onClickListener)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = articles.size
//
//    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
//
//        val currentArticle = articles[position]
////        val ago : CharSequence = DateUtils.getRelativeTimeSpanString(currentArticle.timestamp!!, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
//        val ago = TimeAgo.getTimeAgo(currentArticle.timestamp!!)
//        holder.v.findViewById<TextView>(R.id.tv_home_article_header_username_timestamp).text = currentArticle.userName + " - "+ ago
//        val userProfilePic = holder.v.findViewById<ImageView>(R.id.civ_home_article_header_user_dp)
//        Glide.with(context)
//            .load(currentArticle.userProfilePictureUrl)
//            .placeholder(R.drawable.user_48px)
//            .into(userProfilePic)
//        holder.v.findViewById<TextView>(R.id.tv_home_article_header_title).text = currentArticle.title
//        holder.v.findViewById<TextView>(R.id.tv_home_article_header_text).text = currentArticle.articleText
//
//    }
//    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
//
//    override fun getItemId(position: Int): Long {
//        return super.getItemId(position)
//    }
//}