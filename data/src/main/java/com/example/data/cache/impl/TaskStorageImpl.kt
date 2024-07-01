package com.example.data.cache.impl

import android.util.Log
import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDao
import com.example.data.cache.TaskStorage
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class TaskStorageImpl(
    val taskCacheMapper: TaskCacheMapper,
    val groupCacheMapper: GroupCacheMapper,
    val groupWithTasksCacheMapper: GroupWithTasksCacheMapper,
    val taskDao: TaskDao
) : TaskStorage {

    override suspend fun addTask(task: Task): Flow<Task> {
        return flow {
            // Вставляем запись и получаем её идентификатор
            val taskId = taskDao.addTask(taskCacheMapper.mapToEntity(task))
            emit(taskCacheMapper.mapFromEntity(taskDao.getTask(taskId.toInt())))
        }
    }

    override suspend fun editTask(task: Task) = taskDao.editTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(taskCacheMapper.mapToEntity(task))

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteById(id)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { list ->
            list.map {  taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override fun getNoTimeTasks(): Flow<List<Task>> {
        return taskDao.getNoTimeTasks().map { list ->
            list.map {  taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override fun getCountOfNoTimeTasks(): Flow<Int> = taskDao.getCountOfNoTimeTasks()

    override fun getAllTasksByPeriodType(period: String): Flow<List<Task>> {
        return taskDao.getAllTasksByPeriodType(period).map { list ->
            list.map {  taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override suspend fun addGroup(group: Group): Long {
        return taskDao.addGroup(groupCacheMapper.mapToEntity(group).first)
    }

    override suspend fun editGroup(group: Group) = taskDao.editGroup(groupCacheMapper.mapToEntity(group).first)

    override fun getTask(id: Int): Flow<Task> {
        return flow {
            emit(taskCacheMapper.mapFromEntity(taskDao.getTask(id)))
        }
    }


    override fun getAllGroups(): Flow<List<Group>> {

        val emptyListFlow: Flow<List<Group>> = flowOf(emptyList())

        return taskDao.getAllGroups().combine(flowOf(emptyListFlow)) { taskGroups, _ ->
            taskGroups.map { taskGroup ->
                val tasksCount = taskDao.getCountOfTasksInGroup(taskGroup.groupId)
                Group(
                    groupId = taskGroup.groupId,
                    groupName = taskGroup.groupName,
                    groupColor = taskGroup.groupColor,
                    groupImage = taskGroup.groupImage,
                    tasksCount = tasksCount
                )
            }
        }

    }

    override fun getGroup(id: Int): Flow<Group> {
        return taskDao.getGroup(id)
            .zip(getCountOfTasksInGroup(id)) { entity , count ->
                groupCacheMapper.mapFromEntity(Pair(entity, count))
            }
    }

    override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> = taskDao.getGroupWithTasks(id).map {
        groupWithTasksCacheMapper.mapFromEntity(it)
    }

    override fun getCountOfTasksInGroup(id: Int): Flow<Int> {
        return flow{
            emit(taskDao.getCountOfTasksInGroup(id))
        }
    }

    override suspend fun clearAll() = taskDao.clearAllTasks()

    override suspend fun deleteGroup(groupId: Int) {
        taskDao.deleteGroup(groupId)
    }

    override suspend fun getTasksWithoutGroup(): Flow<List<Task>> {
        return taskDao.getTasksWithoutGroup().map { list ->
            list.map { taskEntity ->
                taskCacheMapper.mapFromEntity(taskEntity)
            }
        }
    }

    override suspend fun getTasksWithoutGroupCount(): Flow<Int> = taskDao.getCountOfNoTimeTasks()

}
