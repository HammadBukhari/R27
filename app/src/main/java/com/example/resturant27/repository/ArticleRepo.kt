package com.example.resturant27.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.resturant27.CloudFirestore
import com.example.resturant27.database.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ArticleRepo private constructor(val app: Application){

    companion object{
    @Volatile private var INSTANCE : ArticleRepo? = null
    fun getInstance (app: Application) = INSTANCE?: synchronized(this){
        INSTANCE?: ArticleRepo(app).apply{ INSTANCE = this}
    }
}
    val localDBInstance by lazy { LocalDatabase.getInstance(app)}
    val firebaseFirestore by lazy {FirebaseFirestore.getInstance()}
    val liveArticles : LiveData<List<Article>> = localDBInstance.ArticleDao().getAllArticle()

    suspend fun getAllArticle()  {

        withContext(Dispatchers.IO) {
            val latestArticleInDb = localDBInstance.ArticleDao().getLatestArticleUploadTime()
            var timestamp = 0L
            latestArticleInDb?.apply {
                timestamp = this

            }
            val query = firebaseFirestore.collection("article").whereGreaterThan(CloudFirestore.Article.upload_timestamp,timestamp)
            val result = Tasks.await(query.get())
            for (fireArticle in result)
            {
                val fireArticleOwner = fireArticle.getString(CloudFirestore.Article.owner_id)!!
                if (localDBInstance.userDao().getUserByUid(fireArticleOwner)  == null) {
                    val queryResult = Tasks.await(
                        firebaseFirestore.collection("user").whereEqualTo(
                            CloudFirestore.User.uid,
                            fireArticleOwner
                        ).limit(1).get()
                    )
                    addUserFromFirebaseUser(queryResult)
                }
                val localArticle = Article(
                    aid = fireArticle.getString(CloudFirestore.Article.aid)!!,
                    owner_id = fireArticle.getString(CloudFirestore.Article.owner_id)!!,
                    body =  fireArticle.getString(CloudFirestore.Article.body)!!,
                    upload_timestamp = (fireArticle.get(CloudFirestore.Article.upload_timestamp)!! as Number).toLong(),
                    update_timestamp = (fireArticle.get(CloudFirestore.Article.update_timestamp)!! as Number).toLong()
                )
                localDBInstance.ArticleDao().addAricle(localArticle)
                val media = fireArticle.get(CloudFirestore.Article.Media.objectName) as ArrayList<HashMap<String,String>>
                for (unit in media){

                    val localMedia = Media(
                        aid = localArticle.aid,
                        url = unit.get(CloudFirestore.Article.Media.url)!!,
                        type = unit.get(CloudFirestore.Article.Media.type)!!
                    )
                    localDBInstance.MediaDao().addMedia(localMedia)
                }
            }

        }

    }
    suspend fun getUserByUid(uid : String) : User? =
        withContext(Dispatchers.IO) {
            return@withContext localDBInstance.userDao().getUserByUid(uid)
        }
    suspend fun getMediaByAid(aid : String) : List<Media> =
        withContext(Dispatchers.IO) {
            return@withContext localDBInstance.MediaDao().findMediaByArticleId(aid)
        }
    suspend fun addUserFromFirebaseUser (querySnapshot: QuerySnapshot){
        withContext(Dispatchers.IO) {
            for (user in querySnapshot) {
                localDBInstance.userDao().addUser(
                    User(
                        uid = user.getString(CloudFirestore.User.uid)!!,
                        desc = user.getString(CloudFirestore.User.desc)!!,
                        name = user.getString(CloudFirestore.User.name)!!,
                        update_timestamp = (user.get(CloudFirestore.User.update_timestamp)!! as Number).toLong(),
                        photo_url = user.getString(CloudFirestore.User.photo_url)!!
                    )
                )
            }
        }
    }
    fun getNoOfUpvotesByAid(aid : String) : LiveData<Int> =
          localDBInstance.upvotesDao().getNoOfUpvotesByAid(aid)

    fun incUpvotesByAid(uid : String, aid : String){
        GlobalScope.launch(Dispatchers.IO) {
            if (localDBInstance.upvotesDao().getUpvoteOfUserByAid(uid, aid) == null) {
                localDBInstance.upvotesDao().addUpvote(Upvotes(uid, aid))
                val result =
                    Tasks.await(firebaseFirestore.collection("article").whereEqualTo("aid", aid).limit(1).get())
                Tasks.await(
                    result.documents[0].reference.update(
                        CloudFirestore.Article.upvote,
                        FieldValue.arrayUnion(uid)
                    )
                )

            }
            else{
                localDBInstance.upvotesDao().deleteUpvote(Upvotes(uid,aid))
                val result =
                    Tasks.await(firebaseFirestore.collection("article").whereEqualTo("aid", aid).limit(1).get())
                Tasks.await(
                    result.documents[0].reference.update(
                        CloudFirestore.Article.upvote,
                        FieldValue.arrayRemove(uid)
                    )
                )
            }
        }

    }
    fun isUserUpvotedArticle(uid : String, aid : String ) : LiveData<Upvotes>{
        return localDBInstance.upvotesDao().getUpvoteOfUserByAidRx(aid,uid)
    }

    fun insertNewComment(commentBody : String, aid: String, uid : String){
        GlobalScope.launch(Dispatchers.IO) {
            val commentId = UUID.randomUUID().toString()
            val currentTime = System.currentTimeMillis()
            val comment = Comments(
                uid = uid,
                aid = aid,
                body = commentBody,
                upload_timestamp = currentTime,
                comment_id = commentId
            )
            localDBInstance.commentsDao().insertComment(comment)
            val findArticleResult= Tasks.await(
                (firebaseFirestore.collection("article").whereEqualTo(CloudFirestore.Article.aid, aid)
                    .get())
            )
            if (findArticleResult.documents.isNotEmpty()){
                // create firebase comment
                val firebaseComment = hashMapOf(
                    CloudFirestore.Article.Comment.uid to uid,
                    CloudFirestore.Article.Comment.body to commentBody,
                    CloudFirestore.Article.Comment.comment_id to commentId,
                    CloudFirestore.Article.Comment.upload_timestamp to currentTime
                )
                Tasks.await(findArticleResult.documents[0].reference.update(
                    CloudFirestore.Article.Comment.objectName,
                    firebaseComment
                ))

            }
        }
    }
    fun getAllCommentsOfArticle (aid : String) : LiveData<List<Comments>>{
        return localDBInstance.commentsDao().getAllCommentsOfArticle(aid)
    }


}