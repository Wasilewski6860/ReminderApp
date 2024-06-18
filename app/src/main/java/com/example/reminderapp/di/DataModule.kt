package com.example.reminderapp.di

import android.content.Context
import com.example.data.cache.GroupCacheMapper
import com.example.data.cache.GroupWithTasksCacheMapper
import com.example.data.cache.TaskCacheMapper
import com.example.data.cache.TaskDatabase
import com.example.data.cache.TaskStorage
import com.example.data.cache.impl.TaskStorageImpl
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.domain.repository.TaskRepository
import com.example.reminderapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module
import kotlin.math.sign

val dataModule = module {

    single<TaskDatabase> {
        TaskDatabase.getDataBase(context = get())
    }

//    single<TaskStorage> {
//        object : TaskStorage {
//            var context: Context = get()
//            var groups: MutableList<Group> = mutableListOf(
//                    Group(
//                        groupId = 0,
//                        groupName = "First list",
//                        groupColor = context.getColor(R.color.red),
//                        groupImage = R.drawable.tea,
//                        tasksCount = 10
//                    ),
//                    Group(
//                        groupId = 1,
//                        groupName = "Second list",
//                        groupColor = context.getColor(R.color.black),
//                        groupImage = R.drawable.box,
//                        tasksCount = 5
//                    ),
//                    Group(
//                        groupId = 2,
//                        groupName = "Third list",
//                        groupColor = context.getColor(R.color.blue),
//                        groupImage = R.drawable.car,
//                        tasksCount = 3
//                    ),
//                    Group(
//                        groupId = 3,
//                        groupName = "Fourth list",
//                        groupColor =context.getColor(R.color.purple_500),
//                        groupImage = R.drawable.beer,
//                        tasksCount = 1
//                    )
//                )
//
//            var tasks: MutableList<Task> = mutableListOf(
//                Task(
//                    id = 0,
//                    name = "task 1",
//                    description =  "desc1",
//                    reminderCreationTime = 0L, reminderTimePeriod = 0L, reminderTime = System.currentTimeMillis(),
//                    isActive = false,
//                    type = TaskPeriodType.ONE_TIME,
//                    isMarkedWithFlag = false,
//                    color = context.getColor(R.color.red),
//                    groupId = 0
//                ),
//                Task(
//                    id = 1,
//                    name = "task 2",
//                    description =  "desc2",
//                    reminderCreationTime = 0L, reminderTimePeriod = 0L, reminderTime = 0L,
//                    isActive = false,
//                    type = TaskPeriodType.PERIODIC,
//                    isMarkedWithFlag = true,
//                    color = context.getColor(R.color.green),
//                    groupId = 0
//                ),
//                Task(
//                    id = 2,
//                    name = "task 3",
//                    description =  "desc3",
//                    reminderCreationTime = 0L, reminderTimePeriod = 0L, reminderTime = 0L,
//                    isActive = false,
//                    type = TaskPeriodType.ONE_TIME,
//                    isMarkedWithFlag = false,
//                    color = context.getColor(R.color.yellow),
//                    groupId = 1
//                ),
//                Task(
//                    id = 3,
//                    name = "task 4",
//                    description =  "desc4",
//                    reminderCreationTime = 0L, reminderTimePeriod = 0L, reminderTime = 0L,
//                    isActive = false,
//                    type = TaskPeriodType.PERIODIC,
//                    isMarkedWithFlag = false,
//                    color = context.getColor(R.color.blue),
//                    groupId = 2
//                )
//            )
//
//            override suspend fun addTask(task: Task): Flow<Long> {
//                task.id = tasks.size
//                tasks.add(task)
//                return flow {
//                    emit(task.id.toLong())
//                }
//            }
//
//            override suspend fun editTask(task: Task) {
//                val pos = tasks.indexOfFirst{it.id == task.id}
//                tasks[pos] = task
//            }
//
//            override suspend fun deleteTask(task: Task) {
//                val pos = tasks.indexOfFirst{it.id == task.id}
//                tasks.removeAt(pos)
//            }
//
//            override suspend fun deleteTask(id: Int) {
//                tasks.removeAt(id)
//            }
//
//            override fun getAllTasks(): Flow<List<Task>> {
//                return flow {
//                    emit(tasks)
//                }
//            }
//
//            override fun getAllTasksByPeriodType(period: String): Flow<List<Task>> {
//                return flow {
//                    emit(tasks.filter { it.type.toString() == period })
//                }
//            }
//
//            override suspend fun addGroup(group: Group) {
//                group.groupId = groups.size
//                groups.add(group)
//            }
//
//            override fun getTask(id: Int): Flow<Task> {
//                return flow {
//                    emit(tasks[tasks.indexOfFirst {it.id == id}])
//                }
//            }
//
//            override fun getAllGroups(): Flow<List<Group>> {
//                return flow {
//                    emit(groups)
//                }
//            }
//
//            override fun getGroup(id: Int): Flow<Group> {
//                return flow {
//                    emit(groups[groups.indexOfFirst {it.groupId == id}])
//                }
//            }
//
//            override fun getGroupWithTasks(id: Int): Flow<GroupWithTasks> {
//                val group = groups[groups.indexOfFirst {it.groupId == id}]
//                val t = tasks.filter{it.groupId == id}
//                return flow{
//                    emit(GroupWithTasks(group, t))
//                }
//            }
//
//            override fun getCountOfTasksInGroup(id: Int): Flow<Int> {
//                val t = tasks.filter{it.groupId == id}
//                return flow {
//                    emit(t.size)
//                }
//            }
//
//            override suspend fun clearAll() {
//               tasks.clear()
//                groups.clear()
//            }
//
//            override suspend fun deleteGroup(groupId: Int) {
//                val pos = groups.indexOfFirst{it.groupId == groupId}
//                groups.removeAt(pos)
//            }
//
//        }
//    }

    single<TaskStorage> {
        TaskStorageImpl(
            taskCacheMapper = get(),
            taskDatabase = get(),
            groupWithTasksCacheMapper = get(),
            groupCacheMapper = get()
        )
    }

    single<TaskRepository> {
        TaskRepositoryImpl(taskStorage = get())
    }

    single<TaskCacheMapper> {
        TaskCacheMapper()
    }

    single<GroupWithTasksCacheMapper> {
        GroupWithTasksCacheMapper(get(), get())
    }

    single<GroupCacheMapper> {
        GroupCacheMapper()
    }

}