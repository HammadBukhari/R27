package com.example.resturant27.restaurant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.resturant27.NearbyFragment
import com.example.resturant27.R
import kotlinx.android.synthetic.main.activity_review_editor.*

class ReviewEditorActivity : AppCompatActivity() {

    companion object{
        val INTENT_EXTRA_REVIEW_TEXT_KEY = "review_text"
        val INTENT_EXTRA_REVIEW_Q1_KEY = "q1"
        val INTENT_EXTRA_REVIEW_Q2_key = "q2"
        val INTENT_EXTRA_REVIEW_RATING = "rating_bar_value"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_editor)
        setSupportActionBar(toolbar_review_editor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(NearbyFragment.INTENT_REVIEW_EDITOR_PLACE_NAME)
        rb_revieweditor_main?.rating = intent.getFloatExtra(NearbyFragment.INTENT_REVIEW_EDITOR_RB_KEY,0f)




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.review_editor_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)
        {
            (R.id.menu_item_revieweditor_post) -> {
                val returnIntent = Intent()
                returnIntent
                    .putExtra(INTENT_EXTRA_REVIEW_TEXT_KEY,et_revieweditor_text.text.toString())
                    .putExtra(INTENT_EXTRA_REVIEW_Q1_KEY,cb_revieweditor_q1.isChecked)
                    .putExtra(INTENT_EXTRA_REVIEW_Q2_key,cb_revieweditor_q2.isChecked)
                    .putExtra(INTENT_EXTRA_REVIEW_RATING,rb_revieweditor_main.rating)
                    .putExtra(NearbyFragment.INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID,intent.getStringExtra(NearbyFragment.INTENT_REVIEW_EDITOR_RESTAURANT_DOC_ID))
                setResult(Activity.RESULT_OK,returnIntent)
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)


        }
//
    }
}
