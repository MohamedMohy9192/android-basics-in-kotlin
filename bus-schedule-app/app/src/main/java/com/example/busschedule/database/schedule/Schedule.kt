package com.example.busschedule.database.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * Represents a single table in the database. Each row is a separate instance of the Schedule class.
 * Each property corresponds to a column. Additionally, an ID is needed as a unique identifier for
 * each row in the database.
 */
@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @NotNull @ColumnInfo(name = "stop_name") val stopName: String,
    @NotNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
)