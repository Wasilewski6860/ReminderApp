package com.example.data

import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType

object TestData {

    val firstTask = Task(
        id = 1,
        name = "Task 1",
        description = "Description",
        reminderCreationTime = 0L,
        reminderTime = 0L,
        reminderTimePeriod = 0L,
        type = TaskPeriodType.PERIODIC,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1,
        color = 0
    )

    val firstTaskEdited = Task(
        id = 1,
        name = "Task 1 edited",
        description = "Description edited",
        reminderCreationTime = 0L,
        reminderTime = 0L,
        reminderTimePeriod = 0L,
        type = TaskPeriodType.NO_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1,
        color = 6
    )

    val secondTask = Task(
        id = 2,
        name = "Task 2",
        description = "Description 2",
        reminderCreationTime = 10L,
        reminderTime = 10L,
        reminderTimePeriod = 0L,
        type = TaskPeriodType.ONE_TIME,
        isActive = true,
        isMarkedWithFlag = true,
        groupId = 2,
        color = 54
    )

    val thirdTask = Task(
        id = 3,
        name = "Task 3",
        description = "Description 3",
        reminderCreationTime = 430L,
        reminderTime = null,
        reminderTimePeriod = null,
        type = TaskPeriodType.NO_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1,
        color = 67
    )

    val tasks = listOf(firstTask, secondTask, thirdTask)
    val noTimeTasks = listOf(thirdTask)
    val countOfNoTimeTasks = noTimeTasks.size

    val firstTaskEntity = TaskEntity(
        id = 1,
        name = "Task 1",
        description = "Description",
        timestamp = 0L,
        startTime = 0L,
        timePeriod = 0L,
        periodicType = TaskPeriodType.PERIODIC.toString(),
        isActive = false,
        flag = true,
        groupId = 1,
        color = 0
    )

    val firstTaskEntityEdited = TaskEntity(
        id = 1,
        name = "Task 1 edited",
        description = "Description edited",
        timestamp = 0L,
        startTime = 0L,
        timePeriod = 0L,
        periodicType = TaskPeriodType.NO_TIME.toString(),
        isActive = false,
        flag = true,
        groupId = 1,
        color = 6
    )

    val secondTaskEntity = TaskEntity(
        id = 2,
        name = "Task 2",
        description = "Description 2",
        timestamp = 10L,
        startTime = 10L,
        timePeriod = 0L,
        periodicType = TaskPeriodType.ONE_TIME.toString(),
        isActive = true,
        flag = true,
        groupId = 2,
        color = 54
    )

    val thirdTaskEntity = TaskEntity(
        id = 3,
        name = "Task 3",
        description = "Description 3",
        timestamp = 430L,
        startTime = null,
        timePeriod = null,
        periodicType = TaskPeriodType.NO_TIME.toString(),
        isActive = false,
        flag = true,
        groupId = 1,
        color = 67
    )

    val taskEntities = listOf(firstTaskEntity, secondTaskEntity, thirdTaskEntity)
    val noTimeTaskEntities = listOf(thirdTaskEntity)
    val oneTimeTaskEntities = listOf(secondTaskEntity)
    val periodicTaskEntities = listOf(firstTaskEntity)


    val firstGroup = Group(
        groupId = 1,
        groupName = "Group 1",
        groupColor = 1,
        groupImage = 1,
        tasksCount = 2
    )

    val secondGroup = Group(
        groupId = 2,
        groupName = "Group 2",
        groupColor = 2,
        groupImage = 2,
        tasksCount = 1
    )

    val thirdGroup = Group(
        groupId = 3,
        groupName = "Group 3",
        groupColor = 3,
        groupImage = 3,
        tasksCount = 0
    )

    val groups = listOf(firstGroup, secondGroup, thirdGroup)

    val firstGroupEntity = TaskGroupEntity(
        groupId = 1,
        groupName = "Group 1",
        groupColor = 1,
        groupImage = 1
    )

    val secondGroupEntity = TaskGroupEntity(
        groupId = 2,
        groupName = "Group 2",
        groupColor = 2,
        groupImage = 2
    )

    val thirdGroupEntity = TaskGroupEntity(
        groupId = 3,
        groupName = "Group 3",
        groupColor = 3,
        groupImage = 3
    )

    val groupEntities = listOf(firstGroupEntity, secondGroupEntity, thirdGroupEntity)

    val firstGroupWithTasks = GroupWithTasks(
        firstGroup,
        listOf(firstTask, thirdTask)
    )

    val firstGroupWithTasksEntity = com.example.data.cache.relation.GroupWithTasks(
        firstGroupEntity,
        listOf(firstTaskEntity, thirdTaskEntity)
    )

    val secondGroupWithTasks = GroupWithTasks(
        secondGroup,
        listOf(secondTask)
    )

    val secondGroupWithTasksEntity = com.example.data.cache.relation.GroupWithTasks(
        secondGroupEntity,
        listOf(secondTaskEntity)
    )

    val thirdGroupWithTasks = GroupWithTasks(
        thirdGroup,
        listOf()
    )

    val thirdGroupWithTasksEntity = com.example.data.cache.relation.GroupWithTasks(
        thirdGroupEntity,
        listOf()
    )
}