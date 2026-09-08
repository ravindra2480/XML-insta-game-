package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM player_progress ORDER BY levelId ASC")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM player_progress WHERE levelId = :levelId LIMIT 1")
    suspend fun getProgressForLevel(levelId: Int): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProgress(progressList: List<ProgressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Update
    suspend fun updateProgress(progress: ProgressEntity)

    // Relics
    @Query("SELECT * FROM relic_item ORDER BY worldId ASC")
    fun getAllRelics(): Flow<List<RelicEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRelics(relics: List<RelicEntity>)

    @Query("UPDATE relic_item SET isUnlocked = 1, unlockedAt = :timestamp WHERE relicId = :relicId")
    suspend fun unlockRelic(relicId: String, timestamp: Long)

    // Player Profile
    @Query("SELECT * FROM player_profile WHERE id = 1 LIMIT 1")
    fun getPlayerProfile(): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayerProfile(profile: PlayerProfileEntity)

    @Query("UPDATE player_profile SET sparks = sparks + :delta WHERE id = 1")
    suspend fun addSparks(delta: Int)

    @Query("UPDATE player_profile SET language = :lang WHERE id = 1")
    suspend fun setLanguage(lang: String)

    @Query("UPDATE player_profile SET soundEnabled = :enabled WHERE id = 1")
    suspend fun setSoundEnabled(enabled: Boolean)

    @Query("UPDATE player_profile SET hapticsEnabled = :enabled WHERE id = 1")
    suspend fun setHapticsEnabled(enabled: Boolean)
}
