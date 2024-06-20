package com.example.data.repositories

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.example.data.cache.TaskStorage
import com.example.data.reminder.Constants
import com.example.data.reminder.Constants.ACTION_CANCEL_ALL
import com.example.data.reminder.Constants.ACTION_DELETE
import com.example.data.reminder.Constants.ACTION_DISMISS
import com.example.data.reminder.Constants.ACTION_SET_INACTIVE
import com.example.data.reminder.RemindAlarmManager
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class TaskRepositoryImpl(
    private val taskStorage: TaskStorage,
    private val context: Context,
    private val reminderReceiverClass: Class<*>
) : TaskRepository, KoinComponent {

    private val remindAlarmManager: RemindAlarmManager by inject()

    override suspend fun addTask(task: Task) {
        taskStorage.addTask(task).collect{
            remindAlarmManager.createAlarm(it)
        }
    }

    override suspend fun editTask(task: Task) {
        if (task.isActive) {
            remindAlarmManager.createAlarm(task)
        }
        else {
            val intent = Intent(context, reminderReceiverClass).putExtra(
                Constants.TASK_EXTRA,
                bundleOf(
                    Constants.TASK_ID_EXTRA to task.id,
                    Constants.TASK_NAME_EXTRA to task.name,
                    Constants.TASK_DESCRIPTION_EXTRA to task.description
                )
            ).setAction(ACTION_SET_INACTIVE)
            context.sendBroadcast(intent)
        }
        taskStorage.editTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        val intent = Intent(context, reminderReceiverClass).putExtra(
            Constants.TASK_EXTRA,
            bundleOf(
                Constants.TASK_ID_EXTRA to task.id,
                Constants.TASK_NAME_EXTRA to task.name,
                Constants.TASK_DESCRIPTION_EXTRA to task.description
            )
        ).setAction(ACTION_DISMISS)
        context.sendBroadcast(intent)

        taskStorage.deleteTask(task)
    }

    override suspend fun deleteTask(id: Int) {
        getTask(id).collect{
            deleteTask(it)
        }
    }

    override suspend fun deleteAll() {
        val intent = Intent(context, reminderReceiverClass).setAction(ACTION_CANCEL_ALL)
        context.sendBroadcast(intent)
        taskStorage.clearAll()
    }

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

        val groupWithTasksFlow = getGroupWithTasks(groupId)

        val groupWithTasks = groupWithTasksFlow.first()

        groupWithTasks.tasks.forEach { task ->
            deleteTask(task)
        }

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
