package com.example.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity

@Database(
    entities = [
        TaskEntity::class,
        TaskGroupEntity::class
    ],
    version = 9
)
abstract class TaskDatabase : RoomDatabase() {

    abstract val dao: TaskDao

    companion object {

        private var INSTANCE: TaskDatabase? = null

        fun getDataBase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TaskDatabase::class.java,
                    "task_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}