package com.hooitis.hoo.eyenosemouse.di.module

import com.hooitis.hoo.eyenosemouse.di.ActivityScope
import com.hooitis.hoo.eyenosemouse.ui.MainActivity
import com.hooitis.hoo.eyenosemouse.ui.SplashActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class SplashActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideSplashActivity(activity: SplashActivity): SplashActivity
}