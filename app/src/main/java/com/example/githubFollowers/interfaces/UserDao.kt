package com.example.githubFollowers.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.githubFollowers.models.UserData

@Dao
interface UserDao {

    //REPLACE will replace the old data if it has the same key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data:UserData)

    // A FlowData stream from the DB to handle live continuous data
    @Query("SELECT * FROM sample_table ORDER BY id ASC")
    fun getOrderedNetworkDataFlow(): Flow<List<UserData>>

    //Delete data from table
    @Query("DELETE FROM sample_table")
    fun clearAll()
}