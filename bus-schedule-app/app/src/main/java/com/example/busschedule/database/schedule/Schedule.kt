package com.example.busschedule.database.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @NotNull @ColumnInfo(name = "stop_name") val stopName: String,
    @NotNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
)