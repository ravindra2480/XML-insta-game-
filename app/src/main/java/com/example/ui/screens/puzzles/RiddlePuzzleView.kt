package com.example.ui.screens.puzzles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*

@Composable
fun RiddlePuzzleView(
    levelDef: LevelDefinition,
    selectedOption: Int?,
    answerStatus: Boolean?,
    isHindi: Boolean,
    onSelectOption: (Int) -> Unit
) {
    val config = levelDef.riddleConfig ?: return
    val question = if (isHindi) config.questionHi else config.questionEn
    val options = if (isHindi) config.optionsHi else config.optionsEn

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ancient parchment scroll card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MysticSurfaceElevated
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ArcaneGold.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📜", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "ऋषि की गूढ़ पहेली" else "The Oracle's Enigma",
                        style = MaterialTheme.typography.titleMedium,
                        color = ArcaneGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Options List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrect = answerStatus == true && isSelected
                val isWrong = answerStatus == false && isSelected

                val cardColor = when {
                    isCorrect -> ForestEmerald.copy(alpha = 0.25f)
                    isWrong -> RubyDanger.copy(alpha = 0.25f)
                    isSelected -> MysticPurple.copy(alpha = 0.25f)
                    else -> MysticSurface
                }

                val borderColor = when {
                    isCorrect -> ForestEmeraldLight
                    isWrong -> RubyDangerLight
                    isSelected -> MysticPurpleLight
                    else -> MysticBorder
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectOption(index) }
                        .testTag("riddle_option_$index"),
                    color = cardColor,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(borderColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('A' + index).toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = borderColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (isCorrect) {
                            Text(text = "✓ सही उत्तर", color = ForestEmeraldLight, fontWeight = FontWeight.Bold)
                        } else if (isWrong) {
                            Text(text = "✗ पुनः विचार करें", color = RubyDangerLight, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lore Fact upon correct answer
        AnimatedVisibility(visible = answerStatus == true) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ForestEmerald.copy(alpha = 0.15f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, ForestEmeraldLight)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isHindi) "📖 प्राचीन ज्ञान:" else "📖 Lore Fragment:",
                        style = MaterialTheme.typography.labelLarge,
                        color = ForestEmeraldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isHindi) config.loreFactHi else config.loreFactEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
