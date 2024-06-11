package com.example.data.cache

import com.example.data.Mapper
import com.example.data.cache.entity.TaskEntity
import com.example.data.toPeriodicType
import com.example.domain.model.Task

class TaskCacheMapper: Mapper<TaskEntity, Task> {
    override fun mapFromEntity(type: TaskEntity): Task {
        with(type) {
            return Task(
                id= id,
                name = name,
                description = description,
                reminderCreationTime = timestamp,
                reminderTimeTarget = timeTarget,
                type = periodicType.toPeriodicType(),
                color = color,
                isActive = isActive
            )
        }
    }

    override fun mapToEntity(type: Task): TaskEntity {
        with(type) {
            return TaskEntity(
                id= id,
                name = name,
                description = description,
                timestamp = reminderCreationTime,
                timeTarget = reminderTimeTarget,
                periodicType = type.type.toString(),
                color = color,
                isActive = isActive
            )
        }
    }

}