package com.hooitis.hoo.eyenosemouse.di

import com.hooitis.hoo.eyenosemouse.di.module.*
import com.hooitis.hoo.eyenosemouse.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
@Suppress("unused")
abstract class ActivityBuilder{
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivityModule(): MainActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivityModule(): SplashActivity

    @ContributesAndroidInjector(modules = [QuizStartActivityModule::class])
    abstract fun bindQuizStartActivityModule(): QuizStartActivity

    @ContributesAndroidInjector(modules = [QuizResultFragmentModule::class])
    abstract fun bindQuizResultFragmentModule(): QuizResultFragment

    @ContributesAndroidInjector(modules = [QuizResultActivityModule::class])
    abstract fun bindQuizResultActivityModule(): QuizResultActivity

    @ContributesAndroidInjector(modules = [BeforeQuizActivityModule::class])
    abstract fun bindBeforeQuizActivityModule(): BeforeQuizActivity
}