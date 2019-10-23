package com.example.resturant27.aricle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ArticleViewModel : ViewModel() {
    public val articlesReady : MutableLiveData<Boolean> = MutableLiveData()
    public val articles  = mutableListOf<Article>().apply{
        loadArticles()
    }
    private fun loadArticles()
    {
        FirebaseFirestore.getInstance().collection("articles").orderBy("timestamp",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (docs in querySnapshot)
                {
                    articles.add(docs.toObject(Article::class.java))
                }
                articlesReady.postValue(true)

            }

    }
}