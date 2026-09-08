package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ProgressEntity
import com.example.ui.theme.*

data class AchievementItem(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val icon: String,
    val isCompleted: Boolean
)

@Composable
fun JournalScreen(
    progressList: List<ProgressEntity>,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    val completedCount = progressList.count { it.isCompleted }
    val totalStars = progressList.sumOf { it.stars }

    val achievements = listOf(
        AchievementItem(
            id = "first_step",
            titleEn = "The First Step",
            titleHi = "प्रथम पग",
            descriptionEn = "Solve the first chamber of the Whispering Forest",
            descriptionHi = "रहस्यमयी वन का प्रथम कक्ष पार करें",
            icon = "🌱",
            isCompleted = progressList.any { it.levelId == 1 && it.isCompleted }
        ),
        AchievementItem(
            id = "forest_sage",
            titleEn = "Forest Conqueror",
            titleHi = "वन विजेता",
            descriptionEn = "Clear all 5 chambers of the Whispering Forest",
            descriptionHi = "रहस्यमयी वन के सभी 5 कक्ष पूर्ण करें",
            icon = "🌲",
            isCompleted = progressList.filter { it.worldId == 1 }.all { it.isCompleted } && progressList.isNotEmpty()
        ),
        AchievementItem(
            id = "sun_champion",
            titleEn = "Sun King's Pride",
            titleHi = "सूर्य सम्राट का गौरव",
            descriptionEn = "Solve the Sunken Temple of Enigma (Levels 6-10)",
            descriptionHi = "प्राचीन सूर्य मन्दिर के सभी कक्ष जीतें (कक्ष 6-10)",
            icon = "☀️",
            isCompleted = progressList.filter { it.worldId == 2 }.all { it.isCompleted } && progressList.count { it.worldId == 2 } == 5
        ),
        AchievementItem(
            id = "shadow_master",
            titleEn = "Shadow Stalker",
            titleHi = "छाया का संहारक",
            descriptionEn = "Overcome the Citadel of Shadows (Levels 11-15)",
            descriptionHi = "छायाओं के दुर्ग के रहस्य सुलझाएं (कक्ष 11-15)",
            icon = "🗡️",
            isCompleted = progressList.filter { it.worldId == 3 }.all { it.isCompleted } && progressList.count { it.worldId == 3 } == 5
        ),
        AchievementItem(
            id = "stellar_god",
            titleEn = "Master of Destiny",
            titleHi = "कालचक्र का स्वामी",
            descriptionEn = "Conquer the Astral Sanctuary and clear Level 20!",
            descriptionHi = "नक्षत्र लोक की परीक्षा और कक्ष 20 को पूर्ण करें!",
            icon = "🌌",
            isCompleted = progressList.any { it.levelId == 20 && it.isCompleted }
        ),
        AchievementItem(
            id = "star_collector",
            titleEn = "Stellar Luminosity",
            titleHi = "नक्षत्र प्रकाशक",
            descriptionEn = "Earn 30 or more stars across all chambers",
            descriptionHi = "सभी कक्षों से 30 या अधिक सितारे एकत्रित करें",
            icon = "⭐",
            isCompleted = totalStars >= 30
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MysticDarkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MysticSurfaceElevated)
                    .testTag("journal_back_button")
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
                    text = if (isHindi) "साहसिक गाथा एवं उपलब्धियां" else "Chronicles & Achievements",
                    style = MaterialTheme.typography.titleLarge,
                    color = ArcaneGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$completedCount / 20 " + (if (isHindi) "कक्ष पूर्ण" else "Chambers Cleared"),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Lore History Section
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MysticSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isHindi) "📜 खोई हुई सभ्यता का इतिहास" else "📜 Lore of the Lost Civilization",
                            style = MaterialTheme.typography.titleMedium,
                            color = ArcaneGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isHindi) {
                                "हज़ारों वर्ष पूर्व, महा-शिल्पियों ने पृथ्वी और नक्षत्रों के संतुलन की रक्षा हेतु चार दिव्य धामों का निर्माण किया। प्रत्येक धाम में ज्ञान की कठिन पहेलियां और पाषाण चक्रव्यूह स्थापित किए गए, ताकि केवल सच्चा बुद्धिमान साधक ही अंतिम ब्रह्मांडीय महा-रत्न तक पहुँच सके。"
                            } else {
                                "Millennia ago, the Grand Architects forged four sacred realms across earth and stars to guard cosmic harmony. Only an adventurer of profound wisdom and courage who unravels every riddle and mechanical dial can awaken the Cosmic Heart Core."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (isHindi) "🏆 विशिष्ट उपलब्धियां" else "🏆 Heroic Trophies",
                    style = MaterialTheme.typography.titleMedium,
                    color = ArcaneGoldBright,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(achievements) { ach ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = if (ach.isCompleted) ForestEmerald.copy(alpha = 0.12f) else MysticSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (ach.isCompleted) ForestEmeraldLight else MysticBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = ach.icon, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) ach.titleHi else ach.titleEn,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (ach.isCompleted) ForestEmeraldLight else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isHindi) ach.descriptionHi else ach.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        if (ach.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = ForestEmeraldLight,
                                modifier = Modifier.size(24.dp)
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
                }
            }
        }
    }
}
