package com.hooitis.hoo.eyenosemouse.di.module

import com.hooitis.hoo.eyenosemouse.di.ActivityScope
import com.hooitis.hoo.eyenosemouse.ui.QuizResultFragment
import dagger.Binds
import dagger.Module


@Module
@Suppress("unused")
abstract class QuizResultFragmentModule {
    @ActivityScope
    @Binds
    abstract fun provideQuizResultFragment(fragment : QuizResultFragment): QuizResultFragment
}