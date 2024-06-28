package com.example.data.cache

import com.example.data.Mapper
import com.example.data.cache.entity.TaskEntity
import com.example.data.toPeriodicType
import com.example.domain.model.Task

class TaskCacheMapper : Mapper<TaskEntity, Task> {
    override fun mapFromEntity(type: TaskEntity): Task {
        with(type) {
            return Task(
                id = id,
                name = name,
                description = description,
                reminderCreationTime = timestamp,
                reminderTime = startTime,
                reminderTimePeriod = timePeriod,
                type = periodicType.toPeriodicType(),
                isActive = isActive,
                groupId = groupId,
                isMarkedWithFlag = flag
            )
        }
    }

    override fun mapToEntity(type: Task): TaskEntity {
        with(type) {
            return TaskEntity(
                id = id,
                name = name,
                description = description,
                timestamp = reminderCreationTime,
                startTime = reminderTime,
                timePeriod = reminderTimePeriod,
                periodicType = type.type.toString(),
                isActive = isActive,
                groupId = groupId,
                flag = isMarkedWithFlag
            )
        }
    }

}