package com.example.karnaughmap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Expression::class], version = 3, exportSchema = false)
abstract class KmapDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val expressionDao: ExpressionDao

    companion object {
        // Singleton предотвратит инициализацию множества сущностей базы данных
        @Volatile
        private var INSTANCE: KmapDatabase? = null

        fun getDatabase(context: Context): KmapDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KmapDatabase::class.java,
                        "kmap_database").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}