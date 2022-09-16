package com.example.githubFollowers.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Sample data class
 * Ensure each entry is unique
 */
@Entity(tableName = "user_table", indices = [Index(value = ["id"], unique = true)])
data class UserData(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    val name: String = "",
    val login: String,
    val avatar_url: String?,
    val public_repos: Int = 0,
    val public_gists: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    val created_at: String = "1970",
    val bio: String? = "",
    val location: String? = ""
)