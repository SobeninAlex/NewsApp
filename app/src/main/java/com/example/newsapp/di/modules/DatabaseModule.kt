package com.example.newsapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "article_database"
        ).build()

    @Provides
    fun providesArticleDao(appDatabase: AppDatabase) =
        appDatabase.articleDao()

}