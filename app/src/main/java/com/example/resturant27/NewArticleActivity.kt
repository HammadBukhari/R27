package com.example.resturant27

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.resturant27.viewModels.NewArticleViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_new_article.*
import java.io.FileDescriptor

class NewArticleActivity : AppCompatActivity() {
    val TAG = this::class.java.name;
    private val RC_IMAGE_PICKER = 1
    val viewModel by lazy { ViewModelProviders.of(this)[NewArticleViewModel::class.java] }
    val addImageButton by lazy {findViewById<MaterialButton>(R.id.mib_new_article_add_image)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_article)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Create Post"
        }

        viewModel.imageUploadingStatus.observe(this, Observer { status ->
            when(status)
            {
                "uploading" -> mcv_new_article_uploading.visibility = View.VISIBLE
                "done" -> {
                    mcv_new_article_uploading.visibility = View.GONE
                    finish()
                }
            }
        })
        viewModel.loadedImages.observe(this, Observer {bitmapImage ->
            if (bitmapImage != null)
            {
                iv_new_article_uploaded.setImageBitmap(bitmapImage)
            }
        })
        addImageButton.setOnClickListener {
            //            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//            val imagePickerIntent = Intent()
//            imagePickerIntent.apply {
//                type = "image/*"
//                action = Intent.ACTION_GET_CONTENT
//            }

            val imagePickerIntent =
                Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(imagePickerIntent, "Select a picture"), RC_IMAGE_PICKER)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_article_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.menu_new_article_post -> {
                viewModel.uploadArticle(et_new_article_body.text.toString().trim())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_IMAGE_PICKER) {
            if (resultCode == RESULT_OK && data != null) {
                val dataUri = data.data
                if (dataUri != null && contentResolver != null) {
//                    viewModel.loadedImages(dataUri, contentResolver!!)
                    try {
//                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver,dataUri)
                        val selectedImageBitmap = getBitmapFromUri(dataUri)
                        viewModel.loadedImages.value = selectedImageBitmap
                    }
                    catch (e : Exception){
                        Log.e(TAG,e.message)
                        Toast.makeText(this,"Error selecting file",Toast.LENGTH_LONG).show()
                    }

                }

            }
        }
    }
}
