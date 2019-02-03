package com.hooitis.hoo.eyenosemouse.di.module

import com.hooitis.hoo.eyenosemouse.di.ActivityScope
import com.hooitis.hoo.eyenosemouse.ui.MainActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class MainActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideMainActivity(activity: MainActivity): MainActivity
}