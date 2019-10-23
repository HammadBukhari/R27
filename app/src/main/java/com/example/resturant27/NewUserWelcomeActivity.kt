package com.example.resturant27

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.resturant27.viewModels.NewUserViewModel
import com.example.resturant27.viewModels.NewUserViewModel.Companion.buildUserFromFirebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewUserWelcomeActivity : AppCompatActivity() {

    val provider by lazy{ ViewModelProviders.of(this@NewUserWelcomeActivity)[NewUserViewModel::class.java]}

    val userNameTextView by lazy { findViewById<TextView>(R.id.tv_new_user_name) }
    val descriptionEditText by lazy {findViewById<EditText>(R.id.et_new_user_description)}
    val doneButton by lazy { findViewById<Button>(R.id.btn_new_user_done) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user_welcome)
        val firebaseAuthInstance = FirebaseAuth.getInstance()
        userNameTextView.text = "Hi" + firebaseAuthInstance.currentUser?.displayName ?:"New User"
        doneButton.setOnClickListener{
            val user = buildUserFromFirebase(FirebaseAuth.getInstance(),descriptionEditText.text.toString().trim())

            GlobalScope.launch {
                val result = provider.insertNewUser(user.second,user.first) // ON IO
                Toast.makeText(this@NewUserWelcomeActivity,result,Toast.LENGTH_LONG).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

        }

    }
}
