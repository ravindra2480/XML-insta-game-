package com.example.ui.screens.puzzles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelDefinition
import com.example.data.model.TileType
import com.example.ui.theme.*

@Composable
fun ChamberMazePuzzleView(
    levelDef: LevelDefinition,
    playerRow: Int,
    playerCol: Int,
    mazeGrid: List<TileType>,
    hasBronzeKey: Boolean,
    hasGoldKey: Boolean,
    remainingEnergy: Int,
    statusMessage: String?,
    isHindi: Boolean,
    onMovePlayer: (Int, Int) -> Unit
) {
    val config = levelDef.mazeConfig ?: return
    val width = config.width
    val height = config.height
    val maxEnergy = config.maxEnergy

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Inventory & Energy Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MysticSurfaceElevated)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Energy
            Column {
                Text(
                    text = if (isHindi) "साहसिक ऊर्जा: $remainingEnergy / $maxEnergy" else "Energy: $remainingEnergy / $maxEnergy",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (remainingEnergy <= 5) RubyDangerLight else AstralCyanLight,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { remainingEnergy.toFloat() / maxEnergy.toFloat() },
                    modifier = Modifier.width(130.dp),
                    color = if (remainingEnergy <= 5) RubyDanger else AstralCyan,
                    trackColor = MysticDarkBg,
                )
            }

            // Keys acquired
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    color = if (hasBronzeKey) ArcaneGold.copy(alpha = 0.2f) else MysticDarkBg,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (hasBronzeKey) ArcaneGold else MysticBorder
                    )
                ) {
                    Text(
                        text = if (hasBronzeKey) "🗝️ कांस्य" else "🗝️ ✖",
                        fontSize = 12.sp,
                        color = if (hasBronzeKey) ArcaneGoldBright else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = if (hasGoldKey) ArcaneGoldBright.copy(alpha = 0.2f) else MysticDarkBg,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (hasGoldKey) ArcaneGoldBright else MysticBorder
                    )
                ) {
                    Text(
                        text = if (hasGoldKey) "🔑 स्वर्ण" else "🔑 ✖",
                        fontSize = 12.sp,
                        color = if (hasGoldKey) ArcaneGoldBright else TextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (statusMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodySmall,
                color = ArcaneGoldBright,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Maze Dungeon Grid
        Box(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MysticDarkBg)
                .border(2.dp, MysticBorder, RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (r in 0 until height) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (c in 0 until width) {
                            val index = r * width + c
                            val tile = mazeGrid.getOrElse(index) { TileType.EMPTY }
                            val isPlayerHere = playerRow == r && playerCol == c

                            val tileBg = when {
                                isPlayerHere -> MysticPurple.copy(alpha = 0.35f)
                                tile == TileType.WALL -> MysticSurfaceElevated
                                tile == TileType.EXIT -> ForestEmerald.copy(alpha = 0.25f)
                                tile == TileType.TRAP -> RubyDanger.copy(alpha = 0.2f)
                                else -> MysticSurface
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(tileBg)
                                    .border(
                                        1.dp,
                                        if (isPlayerHere) MysticPurpleLight else MysticBorder.copy(alpha = 0.5f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .testTag("maze_tile_${r}_$c"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPlayerHere) {
                                    Text(text = "🧙‍♂️", fontSize = 22.sp)
                                } else {
                                    when (tile) {
                                        TileType.WALL -> Text(text = "🧱", fontSize = 16.sp)
                                        TileType.START -> Text(text = "⛺", fontSize = 14.sp)
                                        TileType.EXIT -> Text(text = "🌀", fontSize = 20.sp)
                                        TileType.KEY_BRONZE -> Text(text = "🗝️", fontSize = 16.sp)
                                        TileType.KEY_GOLD -> Text(text = "🔑", fontSize = 16.sp)
                                        TileType.DOOR_BRONZE -> Text(text = "🚪", fontSize = 16.sp)
                                        TileType.DOOR_GOLD -> Text(text = "⛩️", fontSize = 16.sp)
                                        TileType.TRAP -> Text(text = "⚠️", fontSize = 14.sp)
                                        TileType.EMPTY -> Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(TextMuted.copy(alpha = 0.4f), CircleShape)
                                        )
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Adventure D-Pad Directional Controls (meeting 48dp touch target)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // UP
            IconButton(
                onClick = { onMovePlayer(-1, 0) },
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MysticSurfaceElevated)
                    .border(1.dp, MysticBorder, RoundedCornerShape(12.dp))
                    .testTag("dpad_up")
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move Up", tint = ArcaneGoldBright)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // LEFT, DOWN, RIGHT
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onMovePlayer(0, -1) },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MysticSurfaceElevated)
                        .border(1.dp, MysticBorder, RoundedCornerShape(12.dp))
                        .testTag("dpad_left")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Move Left", tint = ArcaneGoldBright)
                }

                IconButton(
                    onClick = { onMovePlayer(1, 0) },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MysticSurfaceElevated)
                        .border(1.dp, MysticBorder, RoundedCornerShape(12.dp))
                        .testTag("dpad_down")
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move Down", tint = ArcaneGoldBright)
                }

                IconButton(
                    onClick = { onMovePlayer(0, 1) },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MysticSurfaceElevated)
                        .border(1.dp, MysticBorder, RoundedCornerShape(12.dp))
                        .testTag("dpad_right")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Move Right", tint = ArcaneGoldBright)
                }
            }
        }
    }
}
