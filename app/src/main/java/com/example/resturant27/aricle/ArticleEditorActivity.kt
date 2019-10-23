package com.example.resturant27.aricle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.resturant27.R
import kotlinx.android.synthetic.main.activity_article_editor.*

class ArticleEditorActivity : AppCompatActivity() {

    companion object {
        val ARTICLE_EDITOR_TITLE = "title"
        val ARTICLE_EDITOR_BODY = "body"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_editor)
        setSupportActionBar(toolbar_article_editor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "New Article"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_article_editor_post -> {
                val intent = Intent()
                intent.putExtra(ARTICLE_EDITOR_TITLE, et_article_editor_title.text.toString().trim())
                    .putExtra(ARTICLE_EDITOR_BODY, et_article_editor_body.text.toString())
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}
