package com.example.resturant27

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resturant27.adapters.ArticleCommentAdapter
import com.example.resturant27.viewModels.Activity27ViewModel
import com.google.android.material.button.MaterialButton


//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CommentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CommentFragment(val aid : String) : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    private var listener: OnFragmentInteractionListener? = null
    private val mbPost by lazy {view?.findViewById<MaterialButton>(R.id.mb_article_comment_post)}
    private val etComment by lazy {view?.findViewById<EditText>(R.id.et_article_comment_main)}
    private val civAppUserPic by lazy {view?.findViewById<ImageView>(R.id.civ_article_comment_app_user_pic)}
    private val viewModel by lazy {ViewModelProviders.of(context as FragmentActivity)[Activity27ViewModel::class.java]}
    private var recyclerView: RecyclerView? = null
    lateinit var viewAdapter: ArticleCommentAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  =  inflater.inflate(R.layout.fragment_comment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initializing recyclerView
        recyclerView = view?.findViewById<RecyclerView>(R.id.rv_article_comment_main).apply {
            viewManager = LinearLayoutManager(context!!);
            viewAdapter = ArticleCommentAdapter(context!!)
            this?.layoutManager = viewManager
            this?.adapter = viewAdapter
        }
        viewModel.articleRepoInstance.getAllCommentsOfArticle(aid).observe(context as FragmentActivity, Observer {
            viewAdapter.allComments = it
            viewAdapter.notifyDataSetChanged()
        })

        mbPost?.isEnabled = false
        etComment?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isEmpty()){
                    mbPost?.isEnabled = false
                }
                else{
                    mbPost?.isEnabled = true
                }
            }

        })
        mbPost?.setOnClickListener {
            viewModel.articleRepoInstance.insertNewComment(etComment?.text.toString().trim(),aid,viewModel.appUser.uid)
            etComment?.setText("")
        }

        // App User lower left Profile Picture
        civAppUserPic?.let {
            Glide.with(context as FragmentActivity)
                .load(viewModel.appUser.photo_url)
                .into(it)
        }


    }

}
