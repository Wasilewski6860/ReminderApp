package com.example.data.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = TaskGroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "start_time") val startTime: Long?,
    @ColumnInfo(name = "time_period") val timePeriod: Long?,
    @ColumnInfo(name = "periodic_type") val periodicType: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @ColumnInfo(name = "flag") val flag: Boolean,
    val groupId: Int?,
)