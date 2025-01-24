package com.example.quitassist

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LifetimeTable::class, DailyTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lifetimeDao(): LifetimeDao
    abstract fun DailyDao(): DailyDao
}