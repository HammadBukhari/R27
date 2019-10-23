package com.example.resturant27.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.resturant27.CloudFirestore
import com.example.resturant27.database.LocalDatabase
import com.example.resturant27.database.User
import com.example.resturant27.repository.ArticleRepo
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Activity27ViewModel(app : Application) : AndroidViewModel(app){
    val firebaseAuthInstance = FirebaseAuth.getInstance()
    val firestoreInstance by lazy {FirebaseFirestore.getInstance()}
    val dbInstance by lazy { LocalDatabase.getInstance(app)}
    val articleRepoInstance by lazy {ArticleRepo.getInstance(app)}
    lateinit var appUser : User

    init{
        viewModelScope.launch(Dispatchers.Main) {
             articleRepoInstance.getAllArticle()
        }
    }
    suspend fun isAppUserInDB(uid : String)
    {
        withContext(Dispatchers.IO) {
            val userFromDB = dbInstance.userDao().getUserByUid(uid)
            if (userFromDB == null) {
                // User is registered but not in DB -> can be new installation
                val userDocument = Tasks.await(firestoreInstance.collection("user").whereEqualTo("uid",firebaseAuthInstance.uid!!).get())
                userDocument?.apply{
                    for (docs in this) {
                        val userFromFirebase = User(
                            uid = docs.getString(CloudFirestore.User.uid)!!,
                            name = docs.getString(CloudFirestore.User.name)!!,
                            desc = docs.getString(CloudFirestore.User.desc)!!,
                            photo_url = docs.getString(CloudFirestore.User.photo_url)!!,
                            update_timestamp = docs.getLong(CloudFirestore.User.update_timestamp)!!
                        )
                        dbInstance.userDao().addUser(userFromFirebase)
                        appUser = userFromFirebase
                    }
                }

            }
            else {
                appUser = userFromDB
            }
        }
    }



}