package com.example.data.cache

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
            return DomainGroupWithTasks(
                group = groupCacheMapper.mapFromEntity(Pair(group, tasks.size) ),
                tasks = tasks.map { taskEntity: TaskEntity ->  taskCacheMapper.mapFromEntity(taskEntity)}
            )
        }
    }

    override fun mapToEntity(type: DomainGroupWithTasks): GroupWithTasks {
        with(type) {
            return GroupWithTasks(
                group = groupCacheMapper.mapToEntity(type.group).first,
                tasks = tasks.map { task: Task ->  taskCacheMapper.mapToEntity(task)}
            )
        }
    }

}