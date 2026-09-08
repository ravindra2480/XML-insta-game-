package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*

@Composable
fun HintDialog(
    levelDef: LevelDefinition,
    unlockedTier: Int,
    currentSparks: Int,
    isHindi: Boolean,
    onUnlockNext: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MysticSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ArcaneGold)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = ArcaneGoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "ऋषि का दिव्य संकेत" else "Oracle's Guidance",
                            style = MaterialTheme.typography.titleMedium,
                            color = ArcaneGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                // Sparks Counter
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MysticSurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✨", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "उपलब्ध मंत्र-स्फुलिंग: $currentSparks" else "Available Sparks: $currentSparks",
                        style = MaterialTheme.typography.labelMedium,
                        color = AstralCyanLight,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tier 1: Gentle Clue
                HintTierCard(
                    tier = 1,
                    title = if (isHindi) "चरण 1: सामान्य संकेत" else "Tier 1: Subtle Clue",
                    cost = 10,
                    isUnlocked = unlockedTier >= 1,
                    content = if (isHindi) levelDef.hint1Hi else levelDef.hint1En
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tier 2: Strong Guidance
                HintTierCard(
                    tier = 2,
                    title = if (isHindi) "चरण 2: स्पष्ट मार्गदर्शन" else "Tier 2: Strong Guidance",
                    cost = 15,
                    isUnlocked = unlockedTier >= 2,
                    content = if (isHindi) levelDef.hint2Hi else levelDef.hint2En
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tier 3: Complete Solution Insight
                HintTierCard(
                    tier = 3,
                    title = if (isHindi) "चरण 3: पूर्ण समाधान" else "Tier 3: Solution Revealed",
                    cost = 25,
                    isUnlocked = unlockedTier >= 3,
                    content = if (isHindi) levelDef.solutionExplanationHi else levelDef.solutionExplanationEn
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action button to unlock next tier
                if (unlockedTier < 3) {
                    val nextCost = when (unlockedTier) {
                        0 -> 10
                        1 -> 15
                        else -> 25
                    }
                    val canAfford = currentSparks >= nextCost

                    Button(
                        onClick = onUnlockNext,
                        enabled = canAfford,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("unlock_hint_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ArcaneGold,
                            disabledContainerColor = MysticSurfaceElevated
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isHindi) "अगला संकेत खोलें ($nextCost ✨)" else "Unlock Next Clue ($nextCost ✨)",
                            color = if (canAfford) Color.Black else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isHindi) "समझ गया, आगे बढ़ें" else "Understood, Continue",
                            color = ArcaneGoldBright
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HintTierCard(
    tier: Int,
    title: String,
    cost: Int,
    isUnlocked: Boolean,
    content: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = if (isUnlocked) ForestEmerald.copy(alpha = 0.12f) else MysticSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) ForestEmeraldLight.copy(alpha = 0.6f) else MysticBorder
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isUnlocked) ForestEmeraldLight else TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                if (!isUnlocked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$cost ✨", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                }
            }

            if (isUnlocked) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
