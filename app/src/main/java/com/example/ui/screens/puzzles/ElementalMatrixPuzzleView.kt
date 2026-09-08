package com.example.ui.screens.puzzles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*

@Composable
fun ElementalMatrixPuzzleView(
    levelDef: LevelDefinition,
    matrixState: List<Boolean>,
    isHindi: Boolean,
    onToggleNode: (Int) -> Unit
) {
    val config = levelDef.matrixConfig ?: return
    val size = config.size
    val activeCount = matrixState.count { it }
    val totalCount = matrixState.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isHindi) "सक्रिय वेदियां: $activeCount / $totalCount (सभी 9 को जलाएं)" else "Active Shrines: $activeCount / $totalCount (Ignite all 9)",
            style = MaterialTheme.typography.bodyMedium,
            color = if (activeCount == totalCount) ForestEmeraldLight else ArcaneGoldBright,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3x3 Grid
        Column(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MysticSurface)
                .border(2.dp, MysticBorder, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (row in 0 until size) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (col in 0 until size) {
                        val index = row * size + col
                        val isActive = matrixState.getOrElse(index) { false }

                        val backgroundColor by animateColorAsState(
                            targetValue = if (isActive) ForestEmerald.copy(alpha = 0.25f) else MysticDarkBg,
                            animationSpec = spring(),
                            label = "matrix_bg"
                        )
                        val borderColor by animateColorAsState(
                            targetValue = if (isActive) ForestEmeraldLight else MysticBorder,
                            animationSpec = spring(),
                            label = "matrix_border"
                        )

                        val runeSymbol = config.symbols.getOrElse(index % config.symbols.size) { "⚡" }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(backgroundColor)
                                .border(2.dp, borderColor, RoundedCornerShape(14.dp))
                                .clickable { onToggleNode(index) }
                                .testTag("matrix_cell_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = runeSymbol,
                                    fontSize = 28.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isActive) "ON" else "OFF",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isActive) ForestEmeraldLight else TextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isHindi) "संकेत: एक वेदी को छूने पर उसके ऊपर, नीचे, बाएं और दाएं की वेदियां भी बदलती हैं。" else "Rule: Tapping any shrine toggles it and its 4 direct neighbors.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
        )
    }
}
