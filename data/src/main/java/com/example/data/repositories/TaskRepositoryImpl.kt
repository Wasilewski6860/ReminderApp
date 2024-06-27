package com.example.data.repositories

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.os.bundleOf
import com.example.data.cache.TaskStorage
import com.example.data.reminder.Constants
import com.example.data.reminder.Constants.ACTION_CANCEL_ALL
import com.example.data.reminder.Constants.ACTION_DISMISS
import com.example.data.reminder.Constants.ACTION_SET_INACTIVE
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.ITaskRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class TaskRepositoryImpl(
    private val taskStorage: TaskStorage
) : ITaskRepository {


    override suspend fun addTask(task: Task): Flow<Task> {
        Log.d("MY LOG", "TaskRepositoryImpl addTask"+task.toString())
        return taskStorage.addTask(task)
    }

    override suspend fun editTask(task: Task) {
        taskStorage.editTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskStorage.deleteTask(task)
    }

    override suspend fun deleteTask(id: Int) {
        getTask(id).stateIn(GlobalScope).collect{
            deleteTask(it)
        }
    }

    override suspend fun deleteAll() {
        taskStorage.clearAll()
    }

    override fun getTask(id: Int): Flow<Task> = taskStorage.getTask(id)

    override fun getAllTasksByPeriodType(period: TaskPeriodType): Flow<List<Task>> =
        taskStorage.getAllTasksByPeriodType(period.name)

    override fun getAllTasks(): Flow<List<Task>> = taskStorage.getAllTasks()

    override fun getNoTimeTasks(): Flow<List<Task>> = taskStorage.getNoTimeTasks()

    override fun getCountOfNoTimeTasks(): Flow<Int> = taskStorage.getCountOfNoTimeTasks()

    override fun getAllTasksCount(): Flow<Int> {
        return getAllTasks().map { list->
            list.size
        }
    }

    override fun getTasksForToday(): Flow<List<Task>> {
        return getAllTasks().map { list->
            list.filter { isToday(it.reminderTime) }
        }
    }

    override fun getTasksForTodayCount(): Flow<Int> {
        return getTasksForToday().map { list -> list.size }
    }

    override fun getTasksPlanned(): Flow<List<Task>> {
        return getAllTasks().map { list->
            list.filter { it.type == TaskPeriodType.ONE_TIME }
        }
    }

    override fun getTasksPlannedCount(): Flow<Int> {
        return getTasksPlanned().map { list -> list.size }
    }

    override fun getTasksWithFlag(): Flow<List<Task>> {
        return getAllTasks().map { list->
            list.filter { it.isMarkedWithFlag }
        }
    }

    override fun getTasksWithFlagCount(): Flow<Int> {
        return getTasksWithFlag().map { list -> list.size }
    }

    fun isToday(timeInMillis: Long?): Boolean {
        if (timeInMillis==null){
            return false
        }
        val now = Calendar.getInstance()
        val target = Calendar.getInstance()
        target.timeInMillis = timeInMillis

        return now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }



}
