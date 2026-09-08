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
import com.example.data.db.RelicEntity
import com.example.ui.theme.*

@Composable
fun RelicsScreen(
    relics: List<RelicEntity>,
    isHindi: Boolean,
    onBack: () -> Unit
) {
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
                    .testTag("relics_back_button")
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
                    text = if (isHindi) "प्राचीन महा-अवशेष कक्ष" else "Ancient Relics Vault",
                    style = MaterialTheme.typography.titleLarge,
                    color = ArcaneGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isHindi) "रहस्यमयी दुनिया के 4 दिव्य रत्न" else "4 Sacred Artifacts of the Lost Realm",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(relics) { relic ->
                val icon = when (relic.worldId) {
                    1 -> "🌿"
                    2 -> "☀️"
                    3 -> "🗡️"
                    else -> "🌌"
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (relic.isUnlocked) MysticSurfaceElevated else MysticSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color = if (relic.isUnlocked) MysticPurpleLight else MysticBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Relic Emblem Box
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (relic.isUnlocked) MysticPurpleDark.copy(alpha = 0.5f) else MysticDarkBg
                                )
                                .border(
                                    1.dp,
                                    if (relic.isUnlocked) MysticPurpleLight else MysticBorder,
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (relic.isUnlocked) {
                                Text(text = icon, fontSize = 32.sp)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextMuted,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) relic.nameHi else relic.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (relic.isUnlocked) ArcaneGoldBright else TextMuted,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (relic.isUnlocked) {
                                    if (isHindi) relic.descriptionHi else relic.descriptionEn
                                } else {
                                    if (isHindi) "संसार ${relic.worldId} के अंतिम कक्ष को जीतकर प्राप्त करें" else "Conquer World ${relic.worldId} boss chamber to unlock"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (relic.isUnlocked) TextPrimary else TextSecondary,
                                lineHeight = 18.sp
                            )

                            if (relic.isUnlocked) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isHindi) "✓ अनलॉक किया गया" else "✓ Unlocked & Awakened",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestEmeraldLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
