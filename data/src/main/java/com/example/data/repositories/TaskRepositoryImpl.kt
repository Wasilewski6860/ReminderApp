package com.example.data.repositories

import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TaskRepositoryImpl(private val taskStorage: TaskStorage) : TaskRepository {

    override suspend fun addTask(task: Task) = taskStorage.addTask(task)

    override suspend fun editTask(task: Task) = taskStorage.editTask(task)

    override suspend fun deleteTask(task: Task) = taskStorage.deleteTask(task)

    override suspend fun deleteTask(id: Int) {
        taskStorage.deleteTask(id)
    }

    override suspend fun deleteAll() = taskStorage.clearAll()

    override fun getTask(id: Int): Flow<Task> = taskStorage.getTask(id)

    override fun getAllTasksByPeriodType(period: TaskPeriodType): Flow<List<Task>> =
        taskStorage.getAllTasksByPeriodType(period.name)

    override fun getAllTasks(): Flow<List<Task>> = taskStorage.getAllTasks()
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

    override suspend fun addGroup(group: Group) = taskStorage.addGroup(group)

    override fun getAllGroups(): Flow<List<Group>> = taskStorage.getAllGroups()

    override fun getGroup(id: Int): Flow<Group> = taskStorage.getGroup(id)

    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> =
        taskStorage.getGroupWithTasks(id)

    override suspend fun deleteGroup(groupId: Int) {
        taskStorage.deleteGroup(groupId)
    }

    fun isToday(timeInMillis: Long): Boolean {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance()
        target.timeInMillis = timeInMillis

        return now.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
    }


}
