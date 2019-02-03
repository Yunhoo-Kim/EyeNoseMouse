package com.hooitis.hoo.eyenosemouse.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.hooitis.hoo.eyenosemouse.model.eyenosemouse.Versions
import com.hooitis.hoo.eyenosemouse.model.eyenosemouse.VersionsDao
import com.hooitis.hoo.eyenosemouse.model.quiz.Quiz
import com.hooitis.hoo.eyenosemouse.model.quiz.QuizDao

@Database(entities = [Versions::class, Quiz::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun versionsDao(): VersionsDao
    abstract fun quizDao(): QuizDao
//    abstract fun userDao(): UserDao
//    abstract fun foodDao(): FoodDao
//    abstract fun dietDao(): DietDao
}
