package com.example.data.cache

import android.util.Log
import com.example.data.Mapper
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.relation.GroupWithTasks
import com.example.domain.model.GroupWithTasks as DomainGroupWithTasks
import com.example.domain.model.Task

class GroupWithTasksCacheMapper(
    private val groupCacheMapper: GroupCacheMapper,
    private val taskCacheMapper: TaskCacheMapper
): Mapper<GroupWithTasks, DomainGroupWithTasks> {
    override fun mapFromEntity(type: GroupWithTasks): DomainGroupWithTasks {
        with(type) {
            val group = type.group
            val tasks = type.tasks
            val result = DomainGroupWithTasks(
                group = groupCacheMapper.mapFromEntity(Pair(group, tasks.size) ),
                tasks = if(tasks != null) tasks.map {
                    taskEntity: TaskEntity ->
                    Log.d("MY LOG", "GroupWithTasksCacheMapper mapFromEntity taskCacheMapper.mapFromEntity")
                    taskCacheMapper.mapFromEntity(taskEntity)
                } else listOf()
            )
            return result
        }
    }

    override fun mapToEntity(type: DomainGroupWithTasks): GroupWithTasks {
        with(type) {
            return GroupWithTasks(
                group = groupCacheMapper.mapToEntity(type.group).first,
                tasks = if(tasks != null) tasks.map { task: Task ->  taskCacheMapper.mapToEntity(task)} else listOf()
            )
        }
    }

}