package com.hooitis.hoo.eyenosemouse.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.hooitis.hoo.eyenosemouse.model.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
@Suppress("unused")
class AppModule(private val app: Application){

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    @Named("appContext")
    fun provideContext(app:Application): Context = app

    @Provides
    @Singleton
    fun provideDatabase(app:Application): AppDatabase =
            Room.databaseBuilder(app, AppDatabase::class.java, "hooitis.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideFirebaseStore(firebaseFirestoreSettings: FirebaseFirestoreSettings): FirebaseFirestore {
        val firebaseStore = FirebaseFirestore.getInstance()
        firebaseStore.firestoreSettings = firebaseFirestoreSettings
        return firebaseStore
    }

    @Provides
    @Singleton
    fun provideFirebaseSetting(): FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
}