package com.example.reminderapp.di

import android.icu.util.Calendar
import androidx.test.platform.app.InstrumentationRegistry
import com.example.data.cache.entity.TaskEntity
import com.example.data.cache.entity.TaskGroupEntity
import com.example.domain.model.Group
import com.example.domain.model.GroupWithTasks
import com.example.domain.model.Task
import com.example.domain.model.TaskPeriodType
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.ImageUtils

object TestData {

    private val todayTimeInMillis = System.currentTimeMillis()
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    val firstTask = Task(
        id = 1,
        name = "Task 1",
        description = "Description",
        reminderCreationTime = 0L,
        reminderTime = 0L,
        reminderTimePeriod = 300000L,
        type = TaskPeriodType.PERIODIC,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1
    )

    val firstTaskEdited = Task(
        id = 1,
        name = "Task 1 edited",
        description = "Description edited",
        reminderCreationTime = 0L,
        reminderTime = 0L,
        reminderTimePeriod = 300000L,
        type = TaskPeriodType.NO_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1
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
        groupId = 2
    )

    val thirdTask = Task(
        id = 3,
        name = "Task 3",
        description = "Description 3",
        reminderCreationTime = 430L,
        reminderTime = 101L,
        reminderTimePeriod = 5540L,
        type = TaskPeriodType.NO_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1
    )

    val fourthTask = Task(
        id = 5,
        name = "Task 4",
        description = "Description 3",
        reminderCreationTime = 430L,
        reminderTime = 101L,
        reminderTimePeriod = 5540L,
        type = TaskPeriodType.ONE_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1
    )
    val taskWithoutGroup = Task(
        id = 6,
        name = "Task without group",
        description = "Description 4",
        reminderCreationTime = 430L,
        reminderTime = 101L,
        reminderTimePeriod = 5540L,
        type = TaskPeriodType.ONE_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = null
    )

    val todayTask = Task(
        id = 4,
        name = "Task today",
        description = "Description 3",
        reminderCreationTime = 430L,
        reminderTime = 5540L,
        reminderTimePeriod = todayTimeInMillis,
        type = TaskPeriodType.ONE_TIME,
        isActive = false,
        isMarkedWithFlag = true,
        groupId = 1
    )

    val tasks = listOf(firstTask, secondTask, thirdTask, fourthTask, todayTask, taskWithoutGroup)
    val noTimeTasks = tasks.filter { it.type == TaskPeriodType.NO_TIME }
    val tasksWithoutGroup = tasks.filter { it.groupId == null }
    val countOfNoTimeTasks = noTimeTasks.size

    val periodicTimeTasks = listOf(firstTask)
    val plannedTasks = tasks.filter { it.type == TaskPeriodType.ONE_TIME }
    val todayTasks = listOf(todayTask)
    val tasksWithFlag = tasks.filter { it.isMarkedWithFlag }

    val firstTaskEntity = TaskEntity(
        id = 1,
        name = "Task 1",
        description = "Description",
        timestamp = 0L,
        startTime = 0L,
        timePeriod = 300000L,
        periodicType = TaskPeriodType.PERIODIC.toString(),
        isActive = false,
        flag = true,
        groupId = 1
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
        groupId = 1
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
        groupId = 2
    )

    val thirdTaskEntity = TaskEntity(
        id = 3,
        name = "Task 3",
        description = "Description 3",
        timestamp = 430L,
        startTime = 101L,
        timePeriod = 5540L,
        periodicType = TaskPeriodType.NO_TIME.toString(),
        isActive = false,
        flag = true,
        groupId = 1
    )

    val taskEntities = listOf(firstTaskEntity, secondTaskEntity, thirdTaskEntity)
    val noTimeTaskEntities = listOf(thirdTaskEntity)


    val firstGroup = Group(
        groupId = 1,
        groupName = "Group 1",
        groupColor = ColorsUtils(context).colors[0].color,
        groupImage = ImageUtils(context).onlyImages[0].image,
        tasksCount = 2
    )

    val secondGroup = Group(
        groupId = 2,
        groupName = "Group 2",
        groupColor = ColorsUtils(context).colors[1].color,
        groupImage = ImageUtils(context).onlyImages[1].image,
        tasksCount = 1
    )

    val thirdGroup = Group(
        groupId = 3,
        groupName = "Group 3",
        groupColor = ColorsUtils(context).colors[2].color,
        groupImage = ImageUtils(context).onlyImages[2].image,
        tasksCount = 0
    )

    val groupWithTasks = GroupWithTasks(
        group = firstGroup,
        tasks = tasks.filter { it.groupId == firstGroup.groupId }
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