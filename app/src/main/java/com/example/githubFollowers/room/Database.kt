package com.example.githubFollowers.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubFollowers.interfaces.UserDao
import com.example.githubFollowers.models.UserData

/**
 * Declare all entities that exists in the database
 */
@Database(
    entities = [
        UserData::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class Database : RoomDatabase() {
    abstract fun getDao(): UserDao
}