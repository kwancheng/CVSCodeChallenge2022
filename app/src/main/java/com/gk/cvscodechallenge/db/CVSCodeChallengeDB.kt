package com.gk.cvscodechallenge.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SearchTerm::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class CVSCodeChallengeDB: RoomDatabase() {
    abstract fun searchTermDao(): SearchTermDao
}