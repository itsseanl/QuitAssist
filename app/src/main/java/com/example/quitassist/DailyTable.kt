package com.example.quitassist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo(name = "currentDailyUsage") val currentDailyUsage: Int?,
    @ColumnInfo(name = "goalDailyUsage") val goalDailyUsage: Int?,
    @ColumnInfo(name = "dailySavings") val dailySavings: Double?,
    @ColumnInfo(name = "actualUsage") val actualUsage: Int?,
    @ColumnInfo(name = "date") val dailyDate: String?
    )
