package com.hooitis.hoo.eyenosemouse.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.base.BaseActivity
import com.hooitis.hoo.eyenosemouse.databinding.ActivitySplashBinding
import com.hooitis.hoo.eyenosemouse.di.ViewModelFactory
import com.hooitis.hoo.eyenosemouse.utils.Utils
import com.hooitis.hoo.eyenosemouse.vm.VersionVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class SplashActivity: BaseActivity(){
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val SPLASH_DELAY: Long = 500
    private val mDelayHandler: Handler by lazy {
        Handler()
    }
    private lateinit var viewModel: VersionVM
    private lateinit var binding: ActivitySplashBinding



    private val mRunnable: Runnable = Runnable {
        if(!isFinishing){
            val intent = Intent(applicationContext, QuizStartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionVM::class.java)

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        val cm: ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if(!(networkInfo != null && networkInfo.isConnected)){
            val dialog = AlertDialog.Builder(this).apply {
                setMessage(R.string.need_network)
                        .setPositiveButton(R.string.confirm) { _, _ ->
                            finish()
                        }
            }

            dialog.create()
            dialog.show()
            return
        }

        viewModel.checkVersion()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({serverVersion ->
                val localVersion = viewModel.versionsRepository.loadLocalVersions()
                if(serverVersion.dbVersion != localVersion.dbVersion){
                    viewModel.versionsRepository.quizRepository.loadQuizFromStore()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            viewModel.versionsRepository.versionsDao.deleteAll()
                            viewModel.versionsRepository.versionsDao.insert(serverVersion)
                            mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
                        }
                }
                else
                    mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
                if(serverVersion.appVersion != localVersion.appVersion){
                    // update application from store
                }
            }, {
                val dialog = AlertDialog.Builder(this).apply {
                    setMessage(R.string.what_went_wrong)
                        .setPositiveButton(R.string.confirm) { _, _ ->
                            finish()
                        }
                }

                dialog.create()
                dialog.show()
            })

    }
}