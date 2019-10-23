package com.example.resturant27

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturant27.viewModels.Activity27ViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class HomeFragment : Fragment() {
    var activityViewModel : Activity27ViewModel? = null
    private var recyclerView: RecyclerView? = null
    lateinit var viewAdapter: ArticleHeaderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        // getting view model instance of main activity
        activity?.let {
            activityViewModel = ViewModelProviders.of(it).get(Activity27ViewModel::class.java)
        }

        // New Article FAB
        view.findViewById<ExtendedFloatingActionButton>(R.id.efab_home).setOnClickListener {
            val articleEditorIntent = Intent(context,NewArticleActivity::class.java)
            startActivity(articleEditorIntent)
        }
        recyclerView = view?.findViewById<RecyclerView>(R.id.rv_home_main).apply {
            viewManager = LinearLayoutManager(context!!);
            viewAdapter = ArticleHeaderAdapter(context!!)
            this?.layoutManager = viewManager
            this?.adapter = viewAdapter
        }
        activityViewModel?.articleRepoInstance?.liveArticles?.observe(this, Observer {
            viewAdapter.articles = it
            viewAdapter.notifyDataSetChanged()
        })
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



 }

