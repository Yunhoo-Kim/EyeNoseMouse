package com.hooitis.hoo.eyenosemouse

import android.app.Activity
import android.app.Application
import com.hooitis.hoo.eyenosemouse.di.AppModule
import com.hooitis.hoo.eyenosemouse.di.DaggerAppComponent
import com.hooitis.hoo.eyenosemouse.di.module.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class EyeNoseMouseApp: Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .networkModule(NetworkModule)
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

}
