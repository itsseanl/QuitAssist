package com.example.quitassist
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LifetimeTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo(name = "currentDailyUsage") val currentDailyUsage: Int?,
    @ColumnInfo(name = "goalDailyUsage") val goalDailyUsage: Int?,
    @ColumnInfo(name = "costPerPouch") val costPerPouch: Double?,
    @ColumnInfo(name = "totalSavings") val actualUsage: Double?
)
