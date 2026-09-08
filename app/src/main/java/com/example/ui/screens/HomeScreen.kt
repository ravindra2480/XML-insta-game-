package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.PlayerProfileEntity
import com.example.data.db.ProgressEntity
import com.example.data.db.RelicEntity
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    progressList: List<ProgressEntity>,
    relicsList: List<RelicEntity>,
    profile: PlayerProfileEntity?,
    isHindi: Boolean,
    onStartAdventure: () -> Unit,
    onOpenRelics: () -> Unit,
    onOpenJournal: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit
) {
    val totalStars = progressList.sumOf { it.stars }
    val completedLevels = progressList.count { it.isCompleted }
    val unlockedRelics = relicsList.count { it.isUnlocked }
    val sparks = profile?.sparks ?: 60

    // Subtle breathing animation for central mystic emblem
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MysticDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Settings & Sparks
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sparks counter
            Surface(
                color = MysticSurfaceElevated,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "✨", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$sparks",
                        style = MaterialTheme.typography.titleSmall,
                        color = AstralCyanLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Quick toggles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Language toggle
                IconButton(
                    onClick = onToggleLanguage,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MysticSurfaceElevated)
                        .border(1.dp, MysticBorder, CircleShape)
                        .testTag("toggle_language_btn")
                ) {
                    Text(
                        text = if (isHindi) "EN" else "हिं",
                        style = MaterialTheme.typography.labelMedium,
                        color = ArcaneGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Sound toggle
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MysticSurfaceElevated)
                        .border(1.dp, MysticBorder, CircleShape)
                        .testTag("toggle_sound_btn")
                ) {
                    Icon(
                        imageVector = if (profile?.soundEnabled != false) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sound",
                        tint = if (profile?.soundEnabled != false) ArcaneGoldBright else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Hero Mystic Emblem
        Box(
            modifier = Modifier
                .size(140.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MysticPurpleDark.copy(alpha = 0.8f),
                            MysticSurfaceElevated,
                            MysticDarkBg
                        )
                    )
                )
                .border(2.5.dp, ArcaneGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🧭", fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title & Slogan
        Text(
            text = if (isHindi) "मिस्टिक क्वेस्ट" else "Mystic Quest",
            style = MaterialTheme.typography.headlineMedium,
            color = ArcaneGoldBright,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (isHindi) "रहस्यमयी पहेलियां और साहसिक सफर" else "Offline Adventure & Riddle Odyssey",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Player Stats Overview Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MysticSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Completed Levels
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$completedLevels / 20",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isHindi) "कक्ष पार" else "Levels",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                        .background(MysticBorder)
                )

                // Stars
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$totalStars / 60",
                            style = MaterialTheme.typography.titleMedium,
                            color = ArcaneGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "⭐", fontSize = 14.sp)
                    }
                    Text(
                        text = if (isHindi) "सितारे" else "Stars",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                        .background(MysticBorder)
                )

                // Relics
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$unlockedRelics / 4",
                            style = MaterialTheme.typography.titleMedium,
                            color = MysticPurpleLight,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "💎", fontSize = 14.sp)
                    }
                    Text(
                        text = if (isHindi) "महा-अवशेष" else "Relics",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Main Play Button (Primary CTA)
        Button(
            onClick = onStartAdventure,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .testTag("play_adventure_button"),
            colors = ButtonDefaults.buttonColors(containerColor = ArcaneGold),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isHindi) "साहसिक यात्रा शुरू करें" else "Enter Adventure",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Secondary Buttons: Relics Vault & Journal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Relics Button
            OutlinedButton(
                onClick = onOpenRelics,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("relics_vault_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MysticPurpleLight),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MysticSurfaceElevated)
            ) {
                Text(
                    text = if (isHindi) "💎 अवशेष कक्ष" else "💎 Relics Vault",
                    color = MysticPurpleLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Journal Button
            OutlinedButton(
                onClick = onOpenJournal,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .testTag("journal_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AstralCyanLight),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MysticSurfaceElevated)
            ) {
                Text(
                    text = if (isHindi) "📜 इतिहास पोथी" else "📜 Codex Lore",
                    color = AstralCyanLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Offline Game Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(ForestEmerald.copy(alpha = 0.12f))
                .border(1.dp, ForestEmeraldLight.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(text = "🛡️ 100% Offline Game", fontSize = 12.sp, color = ForestEmeraldLight, fontWeight = FontWeight.SemiBold)
        }
    }
}
