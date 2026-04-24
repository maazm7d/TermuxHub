package com.maazm7d.termuxhub.data.source.local

import android.content.Context
import com.maazm7d.termuxhub.data.local.HallOfFameDao
import com.maazm7d.termuxhub.data.local.ToolDao
import com.maazm7d.termuxhub.data.local.entities.HallOfFameEntity
import com.maazm7d.termuxhub.data.local.entities.ToolEntity
import com.maazm7d.termuxhub.data.remote.dto.MetadataDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

interface LocalDataSource {
    fun getAllToolsFlow(): Flow<List<ToolEntity>>
    fun getFavoritesFlow(): Flow<List<ToolEntity>>
    suspend fun getToolById(id: String): ToolEntity?
    suspend fun insertTools(tools: List<ToolEntity>)
    suspend fun updateTool(tool: ToolEntity)
    suspend fun loadMetadataFromAssets(fileName: String): MetadataDto?

    fun getAllHallOfFameFlow(): Flow<List<HallOfFameEntity>>
    suspend fun insertHallOfFameMembers(members: List<HallOfFameEntity>)
    suspend fun clearHallOfFame()
}

class LocalDataSourceImpl @Inject constructor(
    private val toolDao: ToolDao,
    private val hallOfFameDao: HallOfFameDao,
    private val appContext: Context,
    private val moshi: Moshi
) : LocalDataSource {
    override fun getAllToolsFlow(): Flow<List<ToolEntity>> = toolDao.getAllToolsFlow()
    override fun getFavoritesFlow(): Flow<List<ToolEntity>> = toolDao.getFavoritesFlow()
    override suspend fun getToolById(id: String): ToolEntity? = toolDao.getToolById(id)
    override suspend fun insertTools(tools: List<ToolEntity>) = toolDao.insertAll(tools)
    override suspend fun updateTool(tool: ToolEntity) = toolDao.update(tool)

    override suspend fun loadMetadataFromAssets(fileName: String): MetadataDto? = withContext(Dispatchers.IO) {
        try {
            val input = appContext.assets.open(fileName)
            val text = BufferedReader(InputStreamReader(input)).use { it.readText() }
            val adapter = moshi.adapter(MetadataDto::class.java)
            adapter.fromJson(text)
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllHallOfFameFlow(): Flow<List<HallOfFameEntity>> = hallOfFameDao.getAll()
    override suspend fun insertHallOfFameMembers(members: List<HallOfFameEntity>) = hallOfFameDao.insertAll(members)
    override suspend fun clearHallOfFame() = hallOfFameDao.clear()
}
