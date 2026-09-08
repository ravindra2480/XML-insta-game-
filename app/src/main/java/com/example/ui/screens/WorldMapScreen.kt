package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
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
import com.example.data.db.ProgressEntity
import com.example.data.model.LevelDefinition
import com.example.data.model.PuzzleType
import com.example.data.model.WorldInfo
import com.example.ui.theme.*

@Composable
fun WorldMapScreen(
    worlds: List<WorldInfo>,
    levels: List<LevelDefinition>,
    progressList: List<ProgressEntity>,
    profile: PlayerProfileEntity?,
    isHindi: Boolean,
    onSelectLevel: (Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedWorldId by remember { mutableIntStateOf(1) }
    val currentWorld = worlds.find { it.id == selectedWorldId } ?: worlds.first()
    val worldLevels = levels.filter { it.worldId == selectedWorldId }

    val totalStars = progressList.sumOf { it.stars }
    val sparks = profile?.sparks ?: 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MysticDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MysticSurfaceElevated)
                        .testTag("map_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = ArcaneGoldBright
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isHindi) "साहसिक मानचित्र" else "Adventure Map",
                        style = MaterialTheme.typography.titleLarge,
                        color = ArcaneGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isHindi) "20 गुप्त कक्ष" else "20 Cryptic Chambers",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Stats pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MysticSurfaceElevated)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "⭐ $totalStars", style = MaterialTheme.typography.labelMedium, color = ArcaneGoldBright, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "✨ $sparks", style = MaterialTheme.typography.labelMedium, color = AstralCyanLight, fontWeight = FontWeight.Bold)
            }
        }

        // World Selection Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(worlds) { world ->
                val isSelected = world.id == selectedWorldId
                val worldProgress = progressList.filter { it.worldId == world.id }
                val completedInWorld = worldProgress.count { it.isCompleted }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedWorldId = world.id }
                        .testTag("world_tab_${world.id}"),
                    color = if (isSelected) Color(world.accentColorHex).copy(alpha = 0.2f) else MysticSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = if (isSelected) Color(world.accentColorHex) else MysticBorder
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (isHindi) world.nameHi else world.nameEn,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color(world.accentColorHex) else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$completedInWorld / 5 " + (if (isHindi) "पूर्ण" else "Done"),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // World Banner Description
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            color = MysticSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentWorld.accentColorHex).copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentWorld.id) {
                        1 -> "🌲"
                        2 -> "☀️"
                        3 -> "🌑"
                        else -> "🌌"
                    },
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isHindi) currentWorld.nameHi else currentWorld.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(currentWorld.accentColorHex),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isHindi) currentWorld.subtitleHi else currentWorld.subtitleEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Level Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(worldLevels) { level ->
                val progress = progressList.find { it.levelId == level.id }
                val isUnlocked = progress?.isUnlocked == true
                val isCompleted = progress?.isCompleted == true
                val stars = progress?.stars ?: 0

                val typeLabel = when (level.puzzleType) {
                    PuzzleType.RUNIC_DIAL -> if (isHindi) "जादुई चक्र संरेखन" else "Runic Dial"
                    PuzzleType.ANCIENT_RIDDLE -> if (isHindi) "ऋषि की गूढ़ पहेली" else "Oracle Riddle"
                    PuzzleType.ELEMENTAL_MATRIX -> if (isHindi) "तत्व आव्यूह संतुलन" else "Elemental Matrix"
                    PuzzleType.CHAMBER_MAZE -> if (isHindi) "पाषाण भूलभुलैया" else "Chamber Maze"
                    PuzzleType.CIPHER_DECODER -> if (isHindi) "प्राचीन कूट-लिपि" else "Cipher Decoder"
                    PuzzleType.LIGHT_BEAM -> if (isHindi) "प्रकाश किरण" else "Light Beam"
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(enabled = isUnlocked) { onSelectLevel(level.id) }
                        .testTag("level_item_${level.id}"),
                    color = when {
                        !isUnlocked -> MysticDarkBg
                        isCompleted -> ForestEmerald.copy(alpha = 0.12f)
                        else -> MysticSurfaceElevated
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = when {
                            !isUnlocked -> MysticBorder.copy(alpha = 0.5f)
                            isCompleted -> ForestEmeraldLight
                            else -> ArcaneGold
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Level Number Box
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            !isUnlocked -> MysticSurface
                                            isCompleted -> ForestEmerald.copy(alpha = 0.3f)
                                            else -> ArcaneGold.copy(alpha = 0.2f)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isUnlocked) ArcaneGold else MysticBorder,
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isUnlocked) {
                                    Text(
                                        text = "${level.id}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isCompleted) ForestEmeraldLight else ArcaneGoldBright,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = if (isHindi) level.titleHi else level.titleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isUnlocked) TextPrimary else TextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = typeLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isUnlocked) AstralCyanLight else TextMuted
                                )
                            }
                        }

                        // Stars or Lock Status
                        if (isUnlocked) {
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (s in 1..3) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star $s",
                                        tint = if (s <= stars) ArcaneGoldBright else MysticBorder,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = if (isHindi) "बंद" else "Locked",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
