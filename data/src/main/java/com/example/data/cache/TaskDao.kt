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
import kotlinx.coroutines.flow.Flow

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
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id =:id")
    fun getTask(id: Int): Flow<TaskEntity>

    @Query("SELECT * FROM task_group")
    fun getAllGroups(): Flow<List<TaskGroupEntity>>

    @Query("SELECT * FROM task_group WHERE groupId =:id")
    fun getGroup(id: Int): Flow<TaskGroupEntity>

    @Transaction
    @Query("SELECT * FROM task_group WHERE groupId = :groupId LIMIT 1")
    fun getGroupWithTasks(groupId: Int): Flow<GroupWithTasks>

    @Query("SELECT * FROM task WHERE periodic_type =:period")
    fun getAllTasksByPeriodType(period: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM task")
    suspend fun clearAll()

}