package com.example.resturant27

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.resturant27.viewModels.Activity27ViewModel
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_27.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Activity27 : AppCompatActivity() {

    private lateinit var mPager : ViewPager
    val firebaseAuth = FirebaseAuth.getInstance();
    private val viewModel by lazy {ViewModelProviders.of(this)[Activity27ViewModel::class.java]}
    companion object{
        private val RC_SIGN_IN = 1;
        private val RC_NEW_USER_REGISTER = 2;

    }
    private val fragmentsList = arrayListOf<Fragment>(HomeFragment(),NearbyFragment(),ProfileFragment());


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)


        // Auth


            if (firebaseAuth.currentUser != null) {
                startActivity()
            }
            else {
                val providers= arrayListOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()
                )
                val customLayout = AuthMethodPickerLayout
                    .Builder(R.layout.layout_auth)
                    .setGoogleButtonId(R.id.cv_auth_google)
                    .setEmailButtonId(R.id.view)
                    .build()
                //.createSignInIntentBuilder()
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.AppThemeFirebaseAuth)
                        .build(),
                    RC_SIGN_IN
                )
            }



    }
    private fun inflateViews()
    {
        setContentView(R.layout.layout_27)
        setSupportActionBar(findViewById(R.id.tb_home))
        supportActionBar?.setTitle("Res27")

        Glide.with(this)
            .load(viewModel.appUser?.photo_url)
            .placeholder(R.drawable.user_48px)
            .into(civ_main_user_pic)

        // Pager and BottomNavBar

        mPager = findViewById<ViewPager>(R.id.pager);
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager,fragmentsList)
        mPager.adapter = pagerAdapter;
        val bubbleNavigationConstraintView : BubbleNavigationConstraintView = findViewById(R.id.bnb);
        bubbleNavigationConstraintView.setBadgeValue(0,"");
        bubbleNavigationConstraintView.setBadgeValue(1,"2");
        bubbleNavigationConstraintView.setBadgeValue(2,null);

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bubbleNavigationConstraintView.setCurrentActiveItem(position);
            }
        }

        )
        bubbleNavigationConstraintView.setNavigationChangeListener { view, position ->
            mPager.setCurrentItem(position,true);

        }
    }
    override fun onBackPressed() {
       if (mPager.currentItem == 1) // && NearbyFragment.IS_RESTAURANT_CLICKED
        supportFragmentManager?.fragments?.get(1)?.view?.findViewById<CardView>(R.id.cv_nearby_res)?.visibility = View.GONE
        else if (mPager.currentItem == 0)
        super.onBackPressed()
        else {
            mPager.currentItem = mPager.currentItem - 1;

        }
    }
    fun startActivity()
    {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@Activity27,"Welcome back",Toast.LENGTH_LONG).show();
            viewModel.isAppUserInDB(firebaseAuth.uid!!)
            inflateViews()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK && response != null && response.isNewUser)
            {
                val newUserActivityIntent = Intent(this,NewUserWelcomeActivity::class.java)
                startActivityForResult(newUserActivityIntent,RC_NEW_USER_REGISTER)
            }
            else if (resultCode == Activity.RESULT_OK )
            {
                startActivity()
            }
            else if (response == null)
                super.onBackPressed();
        }
        else if (requestCode == RC_NEW_USER_REGISTER)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                startActivity()
            }
        }

    }


    private inner class ScreenSlidePagerAdapter (fm:FragmentManager,val fragments : List<Fragment>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
             return fragments[position];
            }

        override fun getCount(): Int = fragments.size
        }


    }

