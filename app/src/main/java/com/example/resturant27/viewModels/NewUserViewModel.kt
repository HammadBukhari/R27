package com.example.resturant27.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.resturant27.CloudFirestore
import com.example.resturant27.database.LocalDatabase
import com.example.resturant27.database.User
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException


class NewUserViewModel(context: Application) : AndroidViewModel(context) {
    val databaseInstance = LocalDatabase.getInstance(context)
    companion object{
        fun buildUserFromFirebase(firebaseAuthInstance : FirebaseAuth, userDescription: String) : Pair<User,HashMap<String,Any?>>
        {
            val uploadTime: Long = System.currentTimeMillis()
            val name : String = firebaseAuthInstance.currentUser?.displayName ?: "newuser"
            val uid : String = firebaseAuthInstance.currentUser?.uid ?:""
            val photoUrl : String = firebaseAuthInstance.currentUser?.photoUrl.toString()
            val desc : String= userDescription
            val newUserForAppDatabase = User (uid!!,name!!,uploadTime,photoUrl!!,desc)
            val newUserForFirebase = hashMapOf<String,Any?>(
                CloudFirestore.User.name to name,
                CloudFirestore.User.uid to uid,
                CloudFirestore.User.photo_url to photoUrl,
                CloudFirestore.User.update_timestamp to uploadTime,
                CloudFirestore.User.desc to desc
            )
            return Pair(newUserForAppDatabase,newUserForFirebase)

        }
    }
    suspend fun insertNewUser(firebaseUser: HashMap<String, Any?>, appUser: User): String {
        var result = ""
        withContext(Dispatchers.IO) {
            val task = FirebaseFirestore.getInstance().collection("user").add(firebaseUser)

            databaseInstance.userDao().addUser(appUser)
            try {
                // Block on a task and get the result synchronously. This is generally done
                // when executing a task inside a separately managed background thread. Doing this
                // on the main (UI) thread can cause your application to become unresponsive.
                result = Tasks.await(task).id
                2+3
            } catch (e: ExecutionException) {
                1 + 1
                // The Task failed, this is the same exception you'd get in a non-blocking
                // failure handler.
                // ...
            } catch (e: InterruptedException) {
                // An interrupt occurred while waiting for the task to complete.
                2 + 3
                // ...
            }
        }
        return result
    }
}
