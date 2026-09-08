package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.db.AppDatabase
import com.example.data.db.PlayerProfileEntity
import com.example.data.db.ProgressEntity
import com.example.data.db.RelicEntity
import com.example.data.model.*
import com.example.data.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface Screen {
    data object Home : Screen
    data object WorldMap : Screen
    data class LevelPlay(val levelId: Int) : Screen
    data object RelicsVault : Screen
    data object CodexJournal : Screen
}

data class ActiveLevelState(
    val levelDef: LevelDefinition,
    val moves: Int = 0,
    val elapsedSeconds: Int = 0,
    val isSolved: Boolean = false,
    val starsEarned: Int = 0,
    val unlockedHintTier: Int = 0, // 0 = none, 1 = clue 1, 2 = clue 2, 3 = solution
    val showHintDialog: Boolean = false,
    val showVictoryDialog: Boolean = false,
    val newlyUnlockedRelic: RelicEntity? = null,

    // Runic Dial state
    val dialAngles: List<Int> = emptyList(),

    // Elemental Matrix state
    val matrixState: List<Boolean> = emptyList(),

    // Riddle state
    val riddleSelectedOption: Int? = null,
    val riddleAnswerStatus: Boolean? = null, // true = correct, false = wrong

    // Chamber Maze state
    val playerRow: Int = 0,
    val playerCol: Int = 0,
    val mazeGrid: List<TileType> = emptyList(),
    val hasBronzeKey: Boolean = false,
    val hasGoldKey: Boolean = false,
    val remainingEnergy: Int = 20,
    val mazeStatusMessage: String? = null,

    // Cipher state
    val cipherSelectedOption: String? = null,
    val cipherStatus: Boolean? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    val repository = GameRepository(database)
    val soundManager = SoundManager(application)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    val allProgress: StateFlow<List<ProgressEntity>> = repository.allProgress
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val allRelics: StateFlow<List<RelicEntity>> = repository.allRelics
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val playerProfile: StateFlow<PlayerProfileEntity?> = repository.playerProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _activeLevelState = MutableStateFlow<ActiveLevelState?>(null)
    val activeLevelState: StateFlow<ActiveLevelState?> = _activeLevelState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    fun navigateTo(screen: Screen) {
        soundManager.playClick(soundEnabled = isSoundEnabled())
        soundManager.vibrateShort(hapticsEnabled = isHapticsEnabled())
        if (screen !is Screen.LevelPlay) {
            timerJob?.cancel()
            _activeLevelState.value = null
        }
        _currentScreen.value = screen
    }

    fun startLevel(levelId: Int) {
        val levelDef = repository.levels.find { it.id == levelId } ?: return
        timerJob?.cancel()

        // Initialize puzzle state depending on type
        val initialState = when (levelDef.puzzleType) {
            PuzzleType.RUNIC_DIAL -> {
                val angles = levelDef.runicConfig?.initialAngles ?: listOf(90, 180, 270)
                ActiveLevelState(levelDef = levelDef, dialAngles = angles)
            }
            PuzzleType.ELEMENTAL_MATRIX -> {
                val grid = levelDef.matrixConfig?.initialGrid ?: List(9) { false }
                ActiveLevelState(levelDef = levelDef, matrixState = grid)
            }
            PuzzleType.ANCIENT_RIDDLE -> {
                ActiveLevelState(levelDef = levelDef)
            }
            PuzzleType.CHAMBER_MAZE -> {
                val grid = levelDef.mazeConfig?.grid ?: emptyList()
                val startIndex = grid.indexOf(TileType.START).let { if (it >= 0) it else 0 }
                val width = levelDef.mazeConfig?.width ?: 5
                ActiveLevelState(
                    levelDef = levelDef,
                    playerRow = startIndex / width,
                    playerCol = startIndex % width,
                    mazeGrid = grid,
                    remainingEnergy = levelDef.mazeConfig?.maxEnergy ?: 20
                )
            }
            PuzzleType.CIPHER_DECODER -> {
                ActiveLevelState(levelDef = levelDef)
            }
            PuzzleType.LIGHT_BEAM -> {
                ActiveLevelState(levelDef = levelDef)
            }
        }

        _activeLevelState.value = initialState
        _currentScreen.value = Screen.LevelPlay(levelId)

        // Start elapsed timer
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _activeLevelState.update { current ->
                    if (current != null && !current.isSolved) {
                        current.copy(elapsedSeconds = current.elapsedSeconds + 1)
                    } else current
                }
            }
        }
    }

    // --- Runic Dial Logic ---
    fun rotateDial(ringIndex: Int) {
        val state = _activeLevelState.value ?: return
        if (state.isSolved) return
        val config = state.levelDef.runicConfig ?: return

        soundManager.playDialTurn(isSoundEnabled())
        soundManager.vibrateShort(isHapticsEnabled())

        val currentAngles = state.dialAngles.toMutableList()
        // Rotate this dial by 45 degrees
        currentAngles[ringIndex] = (currentAngles[ringIndex] + 45) % 360

        // Handle connected dials
        config.dialConnections[ringIndex]?.forEach { connectedIndex ->
            if (connectedIndex in currentAngles.indices) {
                currentAngles[connectedIndex] = (currentAngles[connectedIndex] + 45) % 360
            }
        }

        val newMoves = state.moves + 1
        val isSolved = currentAngles.all { it == 0 }

        _activeLevelState.value = state.copy(
            dialAngles = currentAngles,
            moves = newMoves,
            isSolved = isSolved
        )

        if (isSolved) {
            onLevelSolved(newMoves)
        }
    }

    // --- Elemental Matrix Logic ---
    fun toggleMatrixNode(index: Int) {
        val state = _activeLevelState.value ?: return
        if (state.isSolved) return
        val config = state.levelDef.matrixConfig ?: return
        val size = config.size

        soundManager.playClick(isSoundEnabled())
        soundManager.vibrateShort(isHapticsEnabled())

        val grid = state.matrixState.toMutableList()
        val row = index / size
        val col = index % size

        fun flip(r: Int, c: Int) {
            if (r in 0 until size && c in 0 until size) {
                val i = r * size + c
                grid[i] = !grid[i]
            }
        }

        // Toggle clicked node and 4 adjacent orthogonal neighbors
        flip(row, col)
        flip(row - 1, col)
        flip(row + 1, col)
        flip(row, col - 1)
        flip(row, col + 1)

        val newMoves = state.moves + 1
        val isSolved = grid.all { it }

        _activeLevelState.value = state.copy(
            matrixState = grid,
            moves = newMoves,
            isSolved = isSolved
        )

        if (isSolved) {
            onLevelSolved(newMoves)
        }
    }

    // --- Riddle Logic ---
    fun selectRiddleOption(optionIndex: Int) {
        val state = _activeLevelState.value ?: return
        if (state.isSolved) return
        val config = state.levelDef.riddleConfig ?: return

        val isCorrect = optionIndex == config.correctIndex
        val newMoves = state.moves + 1

        if (isCorrect) {
            soundManager.playSuccessStep(isSoundEnabled())
            soundManager.vibrateSuccess(isHapticsEnabled())
            _activeLevelState.value = state.copy(
                riddleSelectedOption = optionIndex,
                riddleAnswerStatus = true,
                moves = newMoves,
                isSolved = true
            )
            onLevelSolved(newMoves)
        } else {
            soundManager.playTrapError(isSoundEnabled())
            soundManager.vibrateShort(isHapticsEnabled())
            _activeLevelState.value = state.copy(
                riddleSelectedOption = optionIndex,
                riddleAnswerStatus = false,
                moves = newMoves
            )
        }
    }

    // --- Chamber Maze Logic ---
    fun movePlayer(dRow: Int, dCol: Int) {
        val state = _activeLevelState.value ?: return
        if (state.isSolved || state.remainingEnergy <= 0) return
        val config = state.levelDef.mazeConfig ?: return
        val width = config.width
        val height = config.height

        val newRow = state.playerRow + dRow
        val newCol = state.playerCol + dCol

        // Boundary check
        if (newRow !in 0 until height || newCol !in 0 until width) return

        val targetIndex = newRow * width + newCol
        val targetTile = state.mazeGrid[targetIndex]

        if (targetTile == TileType.WALL) {
            soundManager.playTrapError(isSoundEnabled())
            return
        }

        var hasBronze = state.hasBronzeKey
        var hasGold = state.hasGoldKey
        val newGrid = state.mazeGrid.toMutableList()
        var energyDelta = -1
        var statusMsg: String? = null

        when (targetTile) {
            TileType.KEY_BRONZE -> {
                hasBronze = true
                newGrid[targetIndex] = TileType.EMPTY
                soundManager.playSuccessStep(isSoundEnabled())
                statusMsg = if (isHindi()) "कांस्य चाबी मिल गई! 🗝️" else "Bronze Key Acquired! 🗝️"
            }
            TileType.KEY_GOLD -> {
                hasGold = true
                newGrid[targetIndex] = TileType.EMPTY
                soundManager.playSuccessStep(isSoundEnabled())
                statusMsg = if (isHindi()) "स्वर्ण चाबी मिल गई! 🔑" else "Gold Key Acquired! 🔑"
            }
            TileType.DOOR_BRONZE -> {
                if (hasBronze) {
                    newGrid[targetIndex] = TileType.EMPTY
                    soundManager.playSuccessStep(isSoundEnabled())
                    statusMsg = if (isHindi()) "कांस्य कपाट खुल गया!" else "Bronze Door Unlocked!"
                } else {
                    soundManager.playTrapError(isSoundEnabled())
                    statusMsg = if (isHindi()) "कांस्य चाबी चाहिए!" else "Need Bronze Key!"
                    _activeLevelState.value = state.copy(mazeStatusMessage = statusMsg)
                    return
                }
            }
            TileType.DOOR_GOLD -> {
                if (hasGold) {
                    newGrid[targetIndex] = TileType.EMPTY
                    soundManager.playSuccessStep(isSoundEnabled())
                    statusMsg = if (isHindi()) "स्वर्ण कपाट खुल गया!" else "Gold Door Unlocked!"
                } else {
                    soundManager.playTrapError(isSoundEnabled())
                    statusMsg = if (isHindi()) "स्वर्ण चाबी चाहिए!" else "Need Gold Key!"
                    _activeLevelState.value = state.copy(mazeStatusMessage = statusMsg)
                    return
                }
            }
            TileType.TRAP -> {
                energyDelta = -4
                soundManager.playTrapError(isSoundEnabled())
                soundManager.vibrateShort(isHapticsEnabled())
                statusMsg = if (isHindi()) "सावधान! कांटेदार जाल (-4 ऊर्जा) ⚠️" else "Trap Triggered! (-4 Energy) ⚠️"
            }
            TileType.EXIT -> {
                // Win!
                soundManager.playSuccessStep(isSoundEnabled())
                statusMsg = if (isHindi()) "निकास द्वार मिल गया! विजय!" else "Exit Reached! Victory!"
            }
            else -> {
                soundManager.playClick(isSoundEnabled())
            }
        }

        val remainingEnergy = maxOf(0, state.remainingEnergy + energyDelta)
        val newMoves = state.moves + 1
        val isSolved = targetTile == TileType.EXIT

        _activeLevelState.value = state.copy(
            playerRow = newRow,
            playerCol = newCol,
            mazeGrid = newGrid,
            hasBronzeKey = hasBronze,
            hasGoldKey = hasGold,
            remainingEnergy = remainingEnergy,
            moves = newMoves,
            mazeStatusMessage = statusMsg,
            isSolved = isSolved
        )

        if (isSolved) {
            onLevelSolved(newMoves)
        } else if (remainingEnergy <= 0) {
            soundManager.playTrapError(isSoundEnabled())
            statusMsg = if (isHindi()) "ऊर्जा समाप्त! पुनः प्रयास करें。" else "Out of Energy! Reset chamber."
            _activeLevelState.value = _activeLevelState.value?.copy(mazeStatusMessage = statusMsg)
        }
    }

    // --- Cipher Logic ---
    fun submitCipherAnswer(answer: String) {
        val state = _activeLevelState.value ?: return
        if (state.isSolved) return
        val config = state.levelDef.cipherConfig ?: return

        val isCorrect = if (config.numericAnswer != null) {
            answer.toIntOrNull() == config.numericAnswer
        } else {
            answer.trim().equals(config.textAnswer?.trim(), ignoreCase = true)
        }

        val newMoves = state.moves + 1

        if (isCorrect) {
            soundManager.playSuccessStep(isSoundEnabled())
            soundManager.vibrateSuccess(isHapticsEnabled())
            _activeLevelState.value = state.copy(
                cipherSelectedOption = answer,
                cipherStatus = true,
                moves = newMoves,
                isSolved = true
            )
            onLevelSolved(newMoves)
        } else {
            soundManager.playTrapError(isSoundEnabled())
            soundManager.vibrateShort(isHapticsEnabled())
            _activeLevelState.value = state.copy(
                cipherSelectedOption = answer,
                cipherStatus = false,
                moves = newMoves
            )
        }
    }

    // --- Hint System ---
    fun openHintDialog() {
        soundManager.playClick(isSoundEnabled())
        _activeLevelState.update { it?.copy(showHintDialog = true) }
    }

    fun closeHintDialog() {
        soundManager.playClick(isSoundEnabled())
        _activeLevelState.update { it?.copy(showHintDialog = false) }
    }

    fun unlockNextHint() {
        val state = _activeLevelState.value ?: return
        val currentTier = state.unlockedHintTier
        if (currentTier >= 3) return

        val cost = when (currentTier) {
            0 -> 10 // Clue 1: 10 sparks
            1 -> 15 // Clue 2: 15 sparks
            else -> 25 // Complete solution: 25 sparks
        }

        val currentSparks = playerProfile.value?.sparks ?: 0
        if (currentSparks < cost) return

        viewModelScope.launch {
            repository.useHintSparks(cost)
            soundManager.playHintChime(isSoundEnabled())
            _activeLevelState.update { it?.copy(unlockedHintTier = currentTier + 1) }
        }
    }

    private fun onLevelSolved(moves: Int) {
        timerJob?.cancel()
        val state = _activeLevelState.value ?: return

        // Calculate stars: 3 stars if within parMoves + 2, 2 stars if within parMoves * 2, else 1 star
        val par = state.levelDef.parMoves
        val stars = when {
            moves <= par + 2 -> 3
            moves <= par * 2 + 4 -> 2
            else -> 1
        }

        soundManager.playVictoryFanfare(isSoundEnabled())
        soundManager.vibrateSuccess(isHapticsEnabled())

        viewModelScope.launch {
            repository.completeLevel(
                levelId = state.levelDef.id,
                starsEarned = stars,
                moves = moves,
                timeSeconds = state.elapsedSeconds
            )

            val newlyUnlockedRelic = if (state.levelDef.relicUnlockId != null) {
                repository.defaultRelics.find { it.relicId == state.levelDef.relicUnlockId }
            } else null

            _activeLevelState.update {
                it?.copy(
                    starsEarned = stars,
                    showVictoryDialog = true,
                    newlyUnlockedRelic = newlyUnlockedRelic
                )
            }
        }
    }

    fun closeVictoryDialog() {
        _activeLevelState.update { it?.copy(showVictoryDialog = false) }
        navigateTo(Screen.WorldMap)
    }

    fun restartActiveLevel() {
        val currentId = _activeLevelState.value?.levelDef?.id ?: return
        startLevel(currentId)
    }

    fun nextLevel() {
        val currentId = _activeLevelState.value?.levelDef?.id ?: return
        if (currentId < repository.levels.size) {
            startLevel(currentId + 1)
        } else {
            navigateTo(Screen.WorldMap)
        }
    }

    // --- Profile & Settings ---
    fun toggleLanguage() {
        val current = playerProfile.value?.language ?: "hi"
        val next = if (current == "hi") "en" else "hi"
        viewModelScope.launch {
            repository.setLanguage(next)
        }
    }

    fun toggleSound() {
        val current = isSoundEnabled()
        viewModelScope.launch {
            repository.setSoundEnabled(!current)
        }
    }

    fun toggleHaptics() {
        val current = isHapticsEnabled()
        viewModelScope.launch {
            repository.setHapticsEnabled(!current)
        }
    }

    private fun isSoundEnabled(): Boolean = playerProfile.value?.soundEnabled ?: true
    private fun isHapticsEnabled(): Boolean = playerProfile.value?.hapticsEnabled ?: true
    fun isHindi(): Boolean = (playerProfile.value?.language ?: "hi") == "hi"

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.release()
    }
}
