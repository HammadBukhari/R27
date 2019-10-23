package com.example.resturant27.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resturant27.R
import com.example.resturant27.database.Comments
import com.example.resturant27.utils.TimeAgo
import com.example.resturant27.viewModels.Activity27ViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class ArticleCommentAdapter(val context : Context) :  RecyclerView.Adapter<ArticleCommentAdapter.ViewHolder>(){
    var allComments : List<Comments>? = null
    private val viewModel by lazy { ViewModelProviders.of (context as FragmentActivity)[Activity27ViewModel::class.java] }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleCommentAdapter.ViewHolder {
               val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_comment, parent, false)
////        view.setOnClickListener(onClickListener)
            return ViewHolder(view)
    }


    override fun getItemCount(): Int = allComments?.size ?:0


    override fun onBindViewHolder(holder: ArticleCommentAdapter.ViewHolder, position: Int) {
        val comment : Comments? = allComments?.get(position)
        comment?.let {
            holder.tvUploadTime.text = TimeAgo.getTimeAgo(it.upload_timestamp)
            holder.tvComment.text = it.body
            GlobalScope.launch(Dispatchers.Main) {
                val user = viewModel.articleRepoInstance.getUserByUid(it.uid)!!
                holder.tvUserName.text = user.name
                Glide.with(context)
                    .load(user.photo_url)
                    .into(holder.ivUserPic)

            }
        }

    }

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        val tvUserName = v.findViewById<TextView>(R.id.tv_article_comment_user_name)
        val ivUserPic = v.findViewById<ImageView>(R.id.civ_article_comment_user_pic)
        val tvComment = v.findViewById<TextView>(R.id.tv_article_comment_body)
        val tvUploadTime = v.findViewById<TextView>(R.id.tv_article_comment_upload_time)
    }

}


//
//class ArticleHeaderAdapter(val context: Context) : RecyclerView.Adapter<ArticleHeaderAdapter.ViewHolder>() {
//
//    val viewModel: Activity27ViewModel =
//        ViewModelProviders.of(context as FragmentActivity).get(Activity27ViewModel::class.java)
//    var articles: List<Article>? = null
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article_header, parent, false)
////        view.setOnClickListener(onClickListener)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = articles?.size ?: 0
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val article = articles?.get(position)
//        GlobalScope.launch(Dispatchers.Main) {
//            article?.let {
//                val owner = viewModel.articleRepoInstance.getUserByUid(article.owner_id)
//                holder.tvUserName.text = owner?.name
//                holder.tvTime.text = TimeAgo.getTimeAgo(article.upload_timestamp)
//                holder.tvBody.text = article.body
//                Glide.with(context)
//                    .load(owner?.photo_url)
//                    .into(holder.civUserPic)
//                val media = viewModel.articleRepoInstance.getMediaByAid(article.aid)
//                if (media.isNotEmpty())
//                    Glide.with(context)
//                        .load(media.get(0).url)
//                        .into(holder.ivMainImage)
//                viewModel.articleRepoInstance.getNoOfUpvotesByAid(article.aid)
//                    .observe(context as FragmentActivity, Observer { noOfUpvotes ->
//                        if (noOfUpvotes != null) {
//                            holder.mibLike.text = noOfUpvotes.toString()
//                        } else {
//                            holder.mibLike.text = "0"
//                        }
//                    })
//                viewModel.articleRepoInstance.
//                    isUserUpvotedArticle(viewModel.appUser.uid,article.aid).
//                    observe(context as FragmentActivity, Observer {
//                        if (it != null){
//
//                            holder.mibLike.icon = (context.resources.getDrawable(R.drawable.ic_heart_red_filled_50px))
//                            holder.mibLike.iconTint = context.resources.getColorStateList(R.color.upvoted)
//
//                        }else{
//
//                            holder.mibLike.iconTint = context.resources.getColorStateList(R.color.colorPrimary)
//                            holder.mibLike.icon = context.resources.getDrawable(R.drawable.ic_heart_50px)
//                        }
//                    } )
//
//                holder.mibLike.setOnClickListener {
//                    viewModel.articleRepoInstance.incUpvotesByAid(viewModel.appUser.uid, article.aid)
//                }
//                holder.mibComment.setOnClickListener {
//                    val commentFragment = CommentFragment()
//                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_home,commentFragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
//
//            }
//        }
//
//    }
//
//    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
//        val tvUserName = v.findViewById<TextView>(R.id.tv_article_header_user_name)
//        val tvTime = v.findViewById<TextView>(R.id.tv_article_header_time)
//        val tvBody = v.findViewById<TextView>(R.id.tv_article_header_main)
//        val civUserPic = v.findViewById<ImageView>(R.id.civ_article_header_user_pic)
//        val ivMainImage = v.findViewById<ImageView>(R.id.iv_article_header_main)
//        val mibLike = v.findViewById<MaterialButton>(R.id.mib_article_header_like)
//        val mibComment = v.findViewById<MaterialButton>(R.id.mib_article_header_comment)
//        val mibShare = v.findViewById<MaterialButton>(R.id.mib_article_header_share)
//
//    }
//
//}