package com.example.newsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

}

/**
 * companion object {
 *         @Volatile
 *         private var INSTANCE: ArticleDatabase? = null
 *
 *         fun getDatabase(context: Context): ArticleDatabase =
 *             INSTANCE ?: synchronized(this) {
 *                 val instance = Room.databaseBuilder(
 *                     context.applicationContext,
 *                     ArticleDatabase::class.java,
 *                     "article_database"
 *                 ).build()
 *                 INSTANCE = instance
 *                 instance
 *             }
 *     }
 */

/**
 * companion object {
 *         @Volatile
 *         private var INSTANCE: ArticleDatabase? = null
 *         private val LOCK = Any()
 *
 *         operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
 *             INSTANCE ?: createDatabase(context).also {
 *                 INSTANCE = it
 *             }
 *         }
 *
 *         private fun createDatabase(context: Context) =
 *             Room.databaseBuilder(
 *                 context.applicationContext,
 *                 ArticleDatabase::class.java,
 *                 "article_database"
 *             ).build()
 *     }
 */