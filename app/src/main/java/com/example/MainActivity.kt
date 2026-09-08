package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MysticDarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GameViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MysticDarkBg
                ) {
                    MysticQuestApp()
                }
            }
        }
    }
}

@Composable
fun MysticQuestApp(gameViewModel: GameViewModel = viewModel()) {
    val currentScreen by gameViewModel.currentScreen.collectAsStateWithLifecycle()
    val progressList by gameViewModel.allProgress.collectAsStateWithLifecycle()
    val relicsList by gameViewModel.allRelics.collectAsStateWithLifecycle()
    val profile by gameViewModel.playerProfile.collectAsStateWithLifecycle()
    val activeLevelState by gameViewModel.activeLevelState.collectAsStateWithLifecycle()

    val isHindi = (profile?.language ?: "hi") == "hi"

    // Back handling
    BackHandler(enabled = currentScreen !is Screen.Home) {
        when (currentScreen) {
            is Screen.LevelPlay -> gameViewModel.navigateTo(Screen.WorldMap)
            else -> gameViewModel.navigateTo(Screen.Home)
        }
    }

    when (val screen = currentScreen) {
        is Screen.Home -> {
            HomeScreen(
                progressList = progressList,
                relicsList = relicsList,
                profile = profile,
                isHindi = isHindi,
                onStartAdventure = { gameViewModel.navigateTo(Screen.WorldMap) },
                onOpenRelics = { gameViewModel.navigateTo(Screen.RelicsVault) },
                onOpenJournal = { gameViewModel.navigateTo(Screen.CodexJournal) },
                onToggleLanguage = { gameViewModel.toggleLanguage() },
                onToggleSound = { gameViewModel.toggleSound() },
                onToggleHaptics = { gameViewModel.toggleHaptics() }
            )
        }
        is Screen.WorldMap -> {
            WorldMapScreen(
                worlds = gameViewModel.repository.worlds,
                levels = gameViewModel.repository.levels,
                progressList = progressList,
                profile = profile,
                isHindi = isHindi,
                onSelectLevel = { levelId -> gameViewModel.startLevel(levelId) },
                onBack = { gameViewModel.navigateTo(Screen.Home) }
            )
        }
        is Screen.LevelPlay -> {
            activeLevelState?.let { state ->
                GameScreen(
                    state = state,
                    profile = profile,
                    isHindi = isHindi,
                    onBackToMap = { gameViewModel.navigateTo(Screen.WorldMap) },
                    onRotateDial = { ringIndex -> gameViewModel.rotateDial(ringIndex) },
                    onToggleMatrixNode = { nodeIndex -> gameViewModel.toggleMatrixNode(nodeIndex) },
                    onSelectRiddleOption = { optionIndex -> gameViewModel.selectRiddleOption(optionIndex) },
                    onMovePlayer = { dRow, dCol -> gameViewModel.movePlayer(dRow, dCol) },
                    onSubmitCipherAnswer = { answer -> gameViewModel.submitCipherAnswer(answer) },
                    onOpenHint = { gameViewModel.openHintDialog() },
                    onCloseHint = { gameViewModel.closeHintDialog() },
                    onUnlockNextHint = { gameViewModel.unlockNextHint() },
                    onRestartLevel = { gameViewModel.restartActiveLevel() },
                    onNextLevel = { gameViewModel.nextLevel() },
                    onCloseVictory = { gameViewModel.closeVictoryDialog() }
                )
            }
        }
        is Screen.RelicsVault -> {
            RelicsScreen(
                relics = relicsList,
                isHindi = isHindi,
                onBack = { gameViewModel.navigateTo(Screen.Home) }
            )
        }
        is Screen.CodexJournal -> {
            JournalScreen(
                progressList = progressList,
                isHindi = isHindi,
                onBack = { gameViewModel.navigateTo(Screen.Home) }
            )
        }
    }
}

