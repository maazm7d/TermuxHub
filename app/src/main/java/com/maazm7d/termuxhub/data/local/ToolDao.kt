package com.maazm7d.termuxhub.data.local

import androidx.room.*
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolDao {

    @Query("SELECT * FROM tools ORDER BY name COLLATE NOCASE ASC")
    fun getAllToolsFlow(): Flow<List<ToolEntity>>

    @Query("SELECT * FROM tools WHERE id = :id LIMIT 1")
    suspend fun getToolById(id: String): ToolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tools: List<ToolEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tool: ToolEntity)

    @Update
    suspend fun update(tool: ToolEntity)

    @Query("DELETE FROM tools")
    suspend fun clearAll()

    @Query("SELECT * FROM tools WHERE is_favorite = 1 ORDER BY name COLLATE NOCASE ASC")
    fun getFavoritesFlow(): Flow<List<ToolEntity>>
}