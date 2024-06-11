package com.example.data.cache

import com.example.data.Mapper
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity
import com.example.data.toPeriodicType
import com.example.domain.model.Group
import com.example.domain.model.Task

class GroupCacheMapper: Mapper<TaskGroupEntity, Group> {
    override fun mapFromEntity(type: TaskGroupEntity): Group {
        with(type) {
            return Group(
                groupId = groupId,
                groupName = groupName,
                groupColor = groupColor,
            )
        }
    }

    override fun mapToEntity(type: Group): TaskGroupEntity {
        with(type) {
            return TaskGroupEntity(
                groupId = groupId,
                groupName = groupName,
                groupColor = groupColor,
            )
        }
    }

}