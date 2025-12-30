package com.maazm7d.termuxhub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maazm7d.termuxhub.data.local.entities.HallOfFameEntity

@Dao
interface HallOfFameDao {

    @Query("SELECT * FROM hall_of_fame")
    suspend fun getAll(): List<HallOfFameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HallOfFameEntity>)

    @Query("DELETE FROM hall_of_fame")
    suspend fun clear()
}
