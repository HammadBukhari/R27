package com.example.resturant27.viewModels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.resturant27.CloudFirestore
import com.example.resturant27.database.Article
import com.example.resturant27.database.LocalDatabase
import com.example.resturant27.database.Media
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*

class NewArticleViewModel(app: Application) : AndroidViewModel(app) {
    val loadedImages =  MutableLiveData<Bitmap>()

    private val MEIDA_PATH = "media"
    val firebaseStorageInstance by lazy { FirebaseStorage.getInstance() }
    val imageUploadingStatus by lazy {MutableLiveData<String>("")}
    val localDBInstance by lazy {LocalDatabase.getInstance(app)}
    fun uploadArticle (articleBody : String){
        imageUploadingStatus.value="uploading"
        var imageDownloadUrl = ""

        viewModelScope.launch(Dispatchers.IO) {
            val fileReference = firebaseStorageInstance.getReference(MEIDA_PATH+"/"+UUID.randomUUID()+".jpeg")
            //        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            val selectedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedImage)
            val selectedImageBitmap = loadedImages.value
            selectedImageBitmap?.let {
                val baos = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG,100,baos)
                val byteData = baos.toByteArray()
                val uploadTask = fileReference.putBytes(byteData)
                Tasks.await(uploadTask)
                imageDownloadUrl = Tasks.await(fileReference.downloadUrl).toString()

            }
            val currentTime = System.currentTimeMillis()
            val articleId = UUID.randomUUID().toString()
            val ownerId = FirebaseAuth.getInstance().uid
            var media = arrayListOf<HashMap<String,String>>()
            if (!imageDownloadUrl.isEmpty())
            media = arrayListOf(hashMapOf(
                CloudFirestore.Article.Media.url to imageDownloadUrl,
                CloudFirestore.Article.Media.type to CloudFirestore.Article.Media.TYPE_IMAGE

            ))
            val userArticle = hashMapOf(
                CloudFirestore.Article.aid to UUID.randomUUID().toString(),
                CloudFirestore.Article.body to articleBody,
                CloudFirestore.Article.owner_id to FirebaseAuth.getInstance().uid,
                CloudFirestore.Article.update_timestamp to currentTime,
                CloudFirestore.Article.upload_timestamp to currentTime,
                CloudFirestore.Article.Media.objectName to arrayListOf(
                    hashMapOf(
                        CloudFirestore.Article.Media.url to imageDownloadUrl,
                        CloudFirestore.Article.Media.type to CloudFirestore.Article.Media.TYPE_IMAGE

                    )
                )
            )
            Tasks.await(FirebaseFirestore.getInstance().collection("article").add(userArticle))
            val articleForLocalDB= Article(
                aid = articleId,
                body = articleBody,
                owner_id = ownerId!!,
                update_timestamp = currentTime,
                upload_timestamp = currentTime
            )
            localDBInstance.ArticleDao().addAricle(articleForLocalDB)
            for (units in media)
            {
                val media = Media(aid = articleId,
                    type = units.get(CloudFirestore.Article.Media.type)!!,
                    url = units.get(CloudFirestore.Article.Media.url)!!)
                localDBInstance.MediaDao().addMedia(media)
            }
            setImageUploadingStatus("done")
        }

    }


    suspend fun setImageUploadingStatus (value : String)
    {
        withContext(Dispatchers.Main)
        {
            imageUploadingStatus.value = value
        }
    }
    suspend fun setLoadImages(bitmap: Bitmap)
    {
        withContext(Dispatchers.Main){
            loadedImages.value = bitmap
        }

    }

}