package com.hooitis.hoo.eyenosemouse.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.base.BaseActivity
import com.hooitis.hoo.eyenosemouse.databinding.ActivityMainBinding
import com.hooitis.hoo.eyenosemouse.di.ViewModelFactory
import com.hooitis.hoo.eyenosemouse.model.SharedPreferenceHelper
import com.hooitis.hoo.eyenosemouse.utils.*
import com.hooitis.hoo.eyenosemouse.vm.MainVM
import javax.inject.Inject


class MainActivity: BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityMainBinding
    private lateinit var mInterstitialAd:InterstitialAd
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation


    private val DELAY: Long = 500
    private val STOP_DELAY: Long = 250
    private val mDelayHandler: Handler by lazy {
        Handler()
    }


    private val mAdCount: AdCount by lazy {
        AdCount.getInstance()
    }

    private val mShowAdView: Runnable = Runnable {
        if(!isFinishing){
            if(mInterstitialAd.isLoaded)
                mInterstitialAd.show()
            else
                showResultActivity()
        }else
            showResultActivity()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)

        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade)
        fadeInAnim.duration = 800
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)
        fadeOutAnim.duration = 800
        fadeOutAnim.startOffset = 800

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        MobileAds.initialize(this, getString(R.string.admob))
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = getString(R.string.admob_id)
            loadAd(AdRequest.Builder().build())
            adListener = object : AdListener(){
                override fun onAdClosed() {
                    super.onAdClosed()
                    showResultActivity()
                    Log.d("Result", "Closed")
                }
            }
        }

        viewModel.wrong.observe(this, android.arch.lifecycle.Observer {
            if(it!!){
                mDelayHandler.postDelayed(mShowAdView, DELAY)
            }else{
                mDelayHandler.removeCallbacks(mShowAdView)
            }
        })

        viewModel.animation.observe(this, android.arch.lifecycle.Observer {
            if(it!!){
                binding.quizImage.startAnimation(fadeInAnim)
                binding.quizImage.startAnimation(fadeOutAnim)
            }
        })


        binding.quizList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

            addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean { return true }
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {} })

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(binding.quizList)
            isLayoutFrozen = true
        }
        binding.answerList.apply {
            layoutManager = GridLayoutManager(context, 3)
        }

        if(viewModel.sharedPreferenceHelper.getInt(SharedPreferenceHelper.KEY.SEX) == MALE)
            viewModel.loadManQuizData()
        else
            viewModel.loadWomenQuizData()

    }

    private fun showResultActivity(){
        try {
            UiUtils.replaceNewFragment(this, QuizResultFragment.newInstance(Bundle()), R.id.container_main)
        }catch (e: IllegalStateException){
            finish()
        }
    }
}