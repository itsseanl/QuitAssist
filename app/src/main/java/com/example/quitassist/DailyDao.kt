package com.example.quitassist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailyDao {
    @Query("SELECT actualUsage FROM DailyTable WHERE date=:theDate")
    suspend fun getAU(theDate: String): Int

    @Query("UPDATE DailyTable SET actualUsage=:usage, dailySavings=:savings WHERE date=:theDate")
    suspend fun setAU(savings: Double, usage: Int, theDate: String)

    @Query("INSERT OR IGNORE INTO DailyTable(actualUsage, date) VALUES (0, :theDate)")
    suspend fun createDailyEntry(theDate: String)


    @Insert
    fun defaultRow(lifetimeTable: LifetimeTable)
}