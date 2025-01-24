package com.example.quitassist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LifetimeDao {
    @Query("SELECT currentDailyUsage FROM LifetimeTable WHERE id=  1")
    suspend fun getCDU(): Int

    @Query("UPDATE LifetimeTable SET currentDailyUsage=:usage  WHERE id=  1")
    suspend fun setCDU(usage: Int)

    @Insert
    fun defaultRow(lifetimeTable: LifetimeTable)
}