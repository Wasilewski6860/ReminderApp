package com.example.data.cache

import com.example.data.Mapper
import com.example.data.cache.entity.TaskEntity
import com.example.data.toPeriodicType
import com.example.domain.model.Exception
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType

class TaskCacheMapper: Mapper<TaskEntity, Task> {
    override fun mapFromEntity(type: TaskEntity): Task {
        with(type) {
            return Task(
                id= id,
                name = name,
                description = description,
                timestamp = timestamp,
                timeTarget = timeTarget,
                type = periodicType.toPeriodicType(),
                color
            )
        }
    }

    override fun mapToEntity(type: Task): TaskEntity {
        with(type) {
            return TaskEntity(
                id= id,
                name = name,
                description = description,
                timestamp = timestamp,
                timeTarget = timeTarget,
                periodicType = type.toString(),
                color
            )
        }
    }


}