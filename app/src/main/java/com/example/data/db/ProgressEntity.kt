package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_progress")
data class ProgressEntity(
    @PrimaryKey val levelId: Int,
    val worldId: Int,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val stars: Int = 0,
    val bestMoves: Int = 0,
    val bestTimeSeconds: Int = 0
)

@Entity(tableName = "relic_item")
data class RelicEntity(
    @PrimaryKey val relicId: String,
    val nameEn: String,
    val nameHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val worldId: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long = 0L
)

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val sparks: Int = 50, // Arcane hints currency
    val language: String = "hi", // "hi" or "en"
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true
)
