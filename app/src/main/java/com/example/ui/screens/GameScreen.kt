package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.PlayerProfileEntity
import com.example.data.model.PuzzleType
import com.example.ui.components.HintDialog
import com.example.ui.components.VictoryDialog
import com.example.ui.screens.puzzles.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveLevelState

@Composable
fun GameScreen(
    state: ActiveLevelState,
    profile: PlayerProfileEntity?,
    isHindi: Boolean,
    onBackToMap: () -> Unit,
    onRotateDial: (Int) -> Unit,
    onToggleMatrixNode: (Int) -> Unit,
    onSelectRiddleOption: (Int) -> Unit,
    onMovePlayer: (Int, Int) -> Unit,
    onSubmitCipherAnswer: (String) -> Unit,
    onOpenHint: () -> Unit,
    onCloseHint: () -> Unit,
    onUnlockNextHint: () -> Unit,
    onRestartLevel: () -> Unit,
    onNextLevel: () -> Unit,
    onCloseVictory: () -> Unit
) {
    val levelDef = state.levelDef
    val currentSparks = profile?.sparks ?: 60
    var loreExpanded by remember { mutableStateOf(false) }

    val minutes = state.elapsedSeconds / 60
    val seconds = state.elapsedSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MysticDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackToMap,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MysticSurfaceElevated)
                            .testTag("game_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to map",
                            tint = ArcaneGoldBright
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "कक्ष ${levelDef.id}: ${levelDef.titleHi}" else "Chamber ${levelDef.id}: ${levelDef.titleEn}",
                            style = MaterialTheme.typography.titleMedium,
                            color = ArcaneGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "⏱️ $timeFormatted  •  👟 ${state.moves} " + (if (isHindi) "चालें" else "moves"),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                // Hint Button
                IconButton(
                    onClick = onOpenHint,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ArcaneGold.copy(alpha = 0.2f))
                        .border(1.dp, ArcaneGoldBright, CircleShape)
                        .testTag("game_hint_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Oracle Hint",
                        tint = ArcaneGoldBright
                    )
                }
            }

            // Collapsible Lore Prologue Accordion
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { loreExpanded = !loreExpanded },
                color = MysticSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📜", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "कथानक एवं रहस्य (कथा)" else "Chamber Lore & Prologue",
                                style = MaterialTheme.typography.labelMedium,
                                color = ArcaneGoldBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (loreExpanded) "▲" else "▼",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }

                    AnimatedVisibility(visible = loreExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) levelDef.loreHi else levelDef.loreEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Dynamic Puzzle View
            when (levelDef.puzzleType) {
                PuzzleType.RUNIC_DIAL -> {
                    RunicDialPuzzleView(
                        levelDef = levelDef,
                        dialAngles = state.dialAngles,
                        isHindi = isHindi,
                        onRotateDial = onRotateDial
                    )
                }
                PuzzleType.ELEMENTAL_MATRIX -> {
                    ElementalMatrixPuzzleView(
                        levelDef = levelDef,
                        matrixState = state.matrixState,
                        isHindi = isHindi,
                        onToggleNode = onToggleMatrixNode
                    )
                }
                PuzzleType.ANCIENT_RIDDLE -> {
                    RiddlePuzzleView(
                        levelDef = levelDef,
                        selectedOption = state.riddleSelectedOption,
                        answerStatus = state.riddleAnswerStatus,
                        isHindi = isHindi,
                        onSelectOption = onSelectRiddleOption
                    )
                }
                PuzzleType.CHAMBER_MAZE -> {
                    ChamberMazePuzzleView(
                        levelDef = levelDef,
                        playerRow = state.playerRow,
                        playerCol = state.playerCol,
                        mazeGrid = state.mazeGrid,
                        hasBronzeKey = state.hasBronzeKey,
                        hasGoldKey = state.hasGoldKey,
                        remainingEnergy = state.remainingEnergy,
                        statusMessage = state.mazeStatusMessage,
                        isHindi = isHindi,
                        onMovePlayer = onMovePlayer
                    )
                }
                PuzzleType.CIPHER_DECODER, PuzzleType.LIGHT_BEAM -> {
                    CipherPuzzleView(
                        levelDef = levelDef,
                        selectedOption = state.cipherSelectedOption,
                        cipherStatus = state.cipherStatus,
                        isHindi = isHindi,
                        onSubmitAnswer = onSubmitCipherAnswer
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Quick Controls (Restart & Hint)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onRestartLevel,
                    modifier = Modifier.testTag("game_restart_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = TextSecondary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "पुनः प्रारंभ" else "Reset Chamber",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onOpenHint,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MysticSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ArcaneGold)
                ) {
                    Text(text = "💡 ", fontSize = 14.sp)
                    Text(
                        text = if (isHindi) "संकेत ($currentSparks ✨)" else "Hints ($currentSparks ✨)",
                        color = ArcaneGoldBright,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Dialogs
        if (state.showHintDialog) {
            HintDialog(
                levelDef = levelDef,
                unlockedTier = state.unlockedHintTier,
                currentSparks = currentSparks,
                isHindi = isHindi,
                onUnlockNext = onUnlockNextHint,
                onDismiss = onCloseHint
            )
        }

        if (state.showVictoryDialog) {
            VictoryDialog(
                levelDef = levelDef,
                stars = state.starsEarned,
                moves = state.moves,
                elapsedSeconds = state.elapsedSeconds,
                rewardSparks = levelDef.rewardSparks,
                newlyUnlockedRelic = state.newlyUnlockedRelic,
                isHindi = isHindi,
                onNextLevel = onNextLevel,
                onWorldMap = onCloseVictory
            )
        }
    }
}
