package com.example.ui.screens.puzzles

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LevelDefinition
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RunicDialPuzzleView(
    levelDef: LevelDefinition,
    dialAngles: List<Int>,
    isHindi: Boolean,
    onRotateDial: (Int) -> Unit
) {
    val config = levelDef.runicConfig ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isHindi) "संरेखण लक्ष्य: सभी रुन शीर्ष (उत्तर) पर लाएं" else "Target: Align all symbols to the North Star (Top)",
            style = MaterialTheme.typography.bodyMedium,
            color = ArcaneGoldBright,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large concentric canvas
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MysticSurfaceElevated,
                            MysticDarkBg
                        )
                    )
                )
                .border(2.dp, MysticBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Animated angles for each ring
            val animatedAngles = dialAngles.map { angle ->
                animateFloatAsState(
                    targetValue = angle.toFloat(),
                    animationSpec = spring(stiffness = 300f),
                    label = "ring_rotation"
                ).value
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = size.width / 2f - 24f

                // Draw alignment target marker at top (12 o'clock)
                val markerY = 16f
                drawLine(
                    color = ArcaneGoldBright,
                    start = Offset(center.x, 6f),
                    end = Offset(center.x, markerY + 12f),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )

                val ringStep = maxRadius / (config.ringCount + 1)

                for (ring in 0 until config.ringCount) {
                    val r = maxRadius - (ring * ringStep)
                    val angle = animatedAngles.getOrElse(ring) { 0f }

                    // Ring background circle
                    drawCircle(
                        color = when (ring) {
                            0 -> MysticPurple.copy(alpha = 0.35f)
                            1 -> AstralCyan.copy(alpha = 0.35f)
                            else -> ArcaneGold.copy(alpha = 0.35f)
                        },
                        radius = r,
                        center = center,
                        style = Stroke(width = 4f)
                    )

                    // Draw notch / rune on ring according to rotation
                    rotate(degrees = angle, pivot = center) {
                        // Notch pointing up when angle is 0
                        val notchY = center.y - r
                        drawCircle(
                            color = when (ring) {
                                0 -> MysticPurpleLight
                                1 -> AstralCyanLight
                                else -> ArcaneGoldBright
                            },
                            radius = 9f,
                            center = Offset(center.x, notchY)
                        )

                        // Secondary cosmetic pips around the dial
                        for (i in 1..7) {
                            val rad = i * (PI / 4.0)
                            val px = center.x + (r * sin(rad)).toFloat()
                            val py = center.y - (r * cos(rad)).toFloat()
                            drawCircle(
                                color = Color.White.copy(alpha = 0.25f),
                                radius = 4f,
                                center = Offset(px, py)
                            )
                        }
                    }
                }

                // Center core jewel
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(ArcaneGoldBright, ArcaneGoldDark),
                        center = center,
                        radius = 20f
                    ),
                    radius = 20f,
                    center = center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rotating Controls for each ring
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (ringIndex in 0 until config.ringCount) {
                val label = if (isHindi) {
                    config.dialLabelsHi.getOrElse(ringIndex) { "चक्र ${ringIndex + 1}" }
                } else {
                    config.dialLabelsEn.getOrElse(ringIndex) { "Ring ${ringIndex + 1}" }
                }
                val currentAngle = dialAngles.getOrElse(ringIndex) { 0 }
                val isAligned = currentAngle == 0

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onRotateDial(ringIndex) }
                        .testTag("dial_ring_$ringIndex"),
                    color = if (isAligned) ForestEmerald.copy(alpha = 0.15f) else MysticSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isAligned) ForestEmerald else MysticBorder
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isAligned) ForestEmeraldLight else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isAligned) (if (isHindi) "पूर्ण संरेखित ✓" else "Aligned ✓") else "$currentAngle°",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isAligned) ForestEmeraldLight else TextSecondary
                            )
                        }

                        Button(
                            onClick = { onRotateDial(ringIndex) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAligned) ForestEmerald else ArcaneGold
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                                .testTag("rotate_button_$ringIndex")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RotateRight,
                                contentDescription = "Rotate $label",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "घुमाएं (+45°)" else "Rotate (+45°)",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
