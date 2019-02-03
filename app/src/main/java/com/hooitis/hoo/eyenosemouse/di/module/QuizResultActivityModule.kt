package com.hooitis.hoo.eyenosemouse.di.module

import com.hooitis.hoo.eyenosemouse.di.ActivityScope
import com.hooitis.hoo.eyenosemouse.ui.QuizResultActivity
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class QuizResultActivityModule {
    @ActivityScope
    @Binds
    abstract fun provideQuizResultActivity(activity: QuizResultActivity): QuizResultActivity
}