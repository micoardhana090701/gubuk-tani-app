package com.futureengineerdev.gubugtani.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Articles::class, RemoteKeys::class], version = 3, exportSchema = false)
abstract class ArticlesDatabase: RoomDatabase(){
    abstract fun articlesDao(): ArticlesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: ArticlesDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ArticlesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesDatabase::class.java, "articles_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}