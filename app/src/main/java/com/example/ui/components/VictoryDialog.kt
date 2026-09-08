package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.db.RelicEntity
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*

@Composable
fun VictoryDialog(
    levelDef: LevelDefinition,
    stars: Int,
    moves: Int,
    elapsedSeconds: Int,
    rewardSparks: Int,
    newlyUnlockedRelic: RelicEntity?,
    isHindi: Boolean,
    onNextLevel: () -> Unit,
    onWorldMap: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MysticSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, ArcaneGold)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Victory Trophy Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(ArcaneGold.copy(alpha = 0.2f))
                        .border(2.dp, ArcaneGoldBright, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏆", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isHindi) "कक्ष विजय! (Level Cleared)" else "Chamber Conquered!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = ArcaneGoldBright,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isHindi) levelDef.titleHi else levelDef.titleEn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stars display (1 to 3)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..3) {
                        val isStarActive = i <= stars
                        val scale by animateFloatAsState(
                            targetValue = if (isStarActive) 1.2f else 0.9f,
                            animationSpec = spring(),
                            label = "star_scale"
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $i",
                            tint = if (isStarActive) ArcaneGoldBright else MysticBorder,
                            modifier = Modifier
                                .size(36.dp)
                                .scale(scale)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Moves
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MysticSurfaceElevated
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isHindi) "चालें" else "Moves",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(
                                text = "$moves",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Time
                    val minutes = elapsedSeconds / 60
                    val seconds = elapsedSeconds % 60
                    val timeStr = String.format("%02d:%02d", minutes, seconds)

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MysticSurfaceElevated
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isHindi) "समय" else "Time",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(
                                text = timeStr,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Reward Sparks
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MysticSurfaceElevated
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isHindi) "मंत्र" else "Sparks",
                                style = MaterialTheme.typography.labelSmall,
                                color = AstralCyanLight
                            )
                            Text(
                                text = "+$rewardSparks ✨",
                                style = MaterialTheme.typography.titleMedium,
                                color = AstralCyanLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Relic Unlocked Banner if any
                if (newlyUnlockedRelic != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MysticPurple.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, MysticPurpleLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💎", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "प्राचीन महा-अवशेष प्राप्त!" else "Ancient Relic Unlocked!",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MysticPurpleLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isHindi) newlyUnlockedRelic.nameHi else newlyUnlockedRelic.nameEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Next Level Button
                Button(
                    onClick = onNextLevel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("victory_next_level_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ArcaneGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isHindi) "अगला कक्ष (Next Level) →" else "Next Level →",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Return to Map
                TextButton(
                    onClick = onWorldMap,
                    modifier = Modifier.testTag("victory_world_map_button")
                ) {
                    Text(
                        text = if (isHindi) "विश्व मानचित्र देखें (World Map)" else "World Map",
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
