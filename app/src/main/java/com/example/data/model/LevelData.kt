package com.example.data.model

enum class PuzzleType {
    RUNIC_DIAL,
    ELEMENTAL_MATRIX,
    ANCIENT_RIDDLE,
    CHAMBER_MAZE,
    CIPHER_DECODER,
    LIGHT_BEAM
}

data class WorldInfo(
    val id: Int,
    val nameEn: String,
    val nameHi: String,
    val subtitleEn: String,
    val subtitleHi: String,
    val accentColorHex: Long,
    val iconName: String,
    val levelRange: IntRange
)

data class LevelDefinition(
    val id: Int,
    val worldId: Int,
    val titleEn: String,
    val titleHi: String,
    val loreEn: String,
    val loreHi: String,
    val puzzleType: PuzzleType,
    val parMoves: Int,
    val rewardSparks: Int,
    val relicUnlockId: String? = null,
    val hint1En: String,
    val hint1Hi: String,
    val hint2En: String,
    val hint2Hi: String,
    val solutionExplanationEn: String,
    val solutionExplanationHi: String,
    // Specific puzzle configurations
    val runicConfig: RunicConfig? = null,
    val matrixConfig: MatrixConfig? = null,
    val riddleConfig: RiddleConfig? = null,
    val mazeConfig: MazeConfig? = null,
    val cipherConfig: CipherConfig? = null,
    val lightBeamConfig: LightBeamConfig? = null
)

// Puzzle 1: Runic Dial Configuration
data class RunicConfig(
    val ringCount: Int = 3,
    val initialAngles: List<Int>, // e.g. [90, 180, 270]
    val targetAngles: List<Int>,  // e.g. [0, 0, 0]
    val runes: List<String> = listOf("ᚱ", "ᚦ", "ᚨ", "ᚹ", "ᚺ", "ᛏ", "ᛒ", "ᛗ"),
    val dialLabelsEn: List<String> = listOf("Sun Ring", "Moon Ring", "Star Ring"),
    val dialLabelsHi: List<String> = listOf("सूर्य चक्र", "चंद्र चक्र", "नक्षत्र चक्र"),
    // Some dials might turn interconnected dials!
    val dialConnections: Map<Int, List<Int>> = emptyMap() // e.g. 0 moves 0 and 1
)

// Puzzle 2: Elemental Matrix Configuration
data class MatrixConfig(
    val size: Int = 3, // 3x3 grid
    val initialGrid: List<Boolean>, // true = active, false = inactive
    val targetAllActive: Boolean = true,
    val symbols: List<String> = listOf("🔥", "💧", "🌿", "⚡", "💨")
)

// Puzzle 3: Riddle Configuration
data class RiddleConfig(
    val questionEn: String,
    val questionHi: String,
    val optionsEn: List<String>,
    val optionsHi: List<String>,
    val correctIndex: Int,
    val loreFactEn: String,
    val loreFactHi: String
)

// Puzzle 4: Chamber Maze Configuration
enum class TileType {
    EMPTY, WALL, START, EXIT, KEY_BRONZE, DOOR_BRONZE, KEY_GOLD, DOOR_GOLD, TRAP, GEM
}

data class MazeConfig(
    val width: Int = 5,
    val height: Int = 5,
    val grid: List<TileType>, // Row-major
    val maxEnergy: Int = 20
)

// Puzzle 5: Cipher Configuration
data class CipherConfig(
    val promptEn: String,
    val promptHi: String,
    val cipherText: String,
    val cluesEn: List<String>,
    val cluesHi: List<String>,
    val numericAnswer: Int? = null,
    val textAnswer: String? = null,
    val options: List<String> = emptyList()
)

// Puzzle 6: Light Beam Reflection Configuration
enum class MirrorType {
    EMPTY, SOURCE, TARGET, MIRROR_RIGHT, MIRROR_LEFT, WALL
}

data class LightBeamConfig(
    val width: Int = 4,
    val height: Int = 4,
    val initialGrid: List<MirrorType>,
    val targetLaserPathCount: Int = 4
)
