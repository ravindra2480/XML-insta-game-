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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*

@Composable
fun CipherPuzzleView(
    levelDef: LevelDefinition,
    selectedOption: String?,
    cipherStatus: Boolean?,
    isHindi: Boolean,
    onSubmitAnswer: (String) -> Unit
) {
    val config = levelDef.cipherConfig ?: return
    val prompt = if (isHindi) config.promptHi else config.promptEn
    val clues = if (isHindi) config.cluesHi else config.cluesEn

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mysterious Inscription Tablet
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MysticSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MysticPurpleLight)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔮", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "प्राचीन कूट-लिपि" else "Ancient Runic Cipher",
                        style = MaterialTheme.typography.titleMedium,
                        color = MysticPurpleLight,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = prompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Cipher Board
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MysticDarkBg)
                        .border(1.dp, MysticBorder, RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = config.cipherText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = FontFamily.Monospace,
                        color = ArcaneGoldBright,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Clues accordion
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MysticSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MysticBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (isHindi) "💡 शिलालेख के सूत्र:" else "💡 Inscribed Clues:",
                    style = MaterialTheme.typography.labelMedium,
                    color = ArcaneGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                clues.forEach { clue ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text(text = "• ", color = ArcaneGold)
                        Text(
                            text = clue,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Answer options grid (2x2)
        Text(
            text = if (isHindi) "गुप्त कोड दर्ज करें:" else "Select Secret Key:",
            style = MaterialTheme.typography.labelLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        val options = config.options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val chunked = options.chunked(2)
            chunked.forEach { rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowOptions.forEach { opt ->
                        val isSelected = selectedOption == opt
                        val isCorrect = cipherStatus == true && isSelected
                        val isWrong = cipherStatus == false && isSelected

                        val bgColor = when {
                            isCorrect -> ForestEmerald.copy(alpha = 0.25f)
                            isWrong -> RubyDanger.copy(alpha = 0.25f)
                            isSelected -> MysticPurple.copy(alpha = 0.25f)
                            else -> MysticSurfaceElevated
                        }

                        val borderColor = when {
                            isCorrect -> ForestEmeraldLight
                            isWrong -> RubyDangerLight
                            isSelected -> MysticPurpleLight
                            else -> MysticBorder
                        }

                        Button(
                            onClick = { onSubmitAnswer(opt) },
                            modifier = Modifier
                                .weight(1f)
                                .defaultMinSize(minHeight = 52.dp)
                                .testTag("cipher_opt_$opt"),
                            colors = ButtonDefaults.buttonColors(containerColor = bgColor),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                        ) {
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isCorrect) ForestEmeraldLight else if (isWrong) RubyDangerLight else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(visible = cipherStatus == false) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isHindi) "कूट मेल नहीं खाया! संकेतों को पुनः पढ़ें。" else "Incorrect cipher resonance! Re-read the clues.",
                style = MaterialTheme.typography.bodySmall,
                color = RubyDangerLight,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
