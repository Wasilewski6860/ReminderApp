package com.example.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity
import com.example.data.cache.relation.GroupWithTasks

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(taskEntity: TaskEntity): Long
    @Update
    suspend fun editTask(taskEntity: TaskEntity)
    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<TaskEntity>
    @Query("SELECT * FROM task WHERE id =:id")
    suspend fun getTask(id: Int): TaskEntity

    @Query("SELECT * FROM task_group")
    suspend fun getAllGroups(): List<TaskGroupEntity>
    @Query("SELECT * FROM task_group WHERE groupId =:id")
    suspend fun getGroup(id: Int): TaskGroupEntity

    @Transaction
    @Query("SELECT * FROM task_group WHERE groupId = :groupId LIMIT 1")
    suspend fun getGroupWithTasks(groupId: Int): GroupWithTasks

    @Query("SELECT * FROM task WHERE periodic_type =:period")
    suspend fun getAllTasksByPeriodType(period: String): List<TaskEntity>

    @Query("DELETE FROM task")
    suspend fun clearAll()

}