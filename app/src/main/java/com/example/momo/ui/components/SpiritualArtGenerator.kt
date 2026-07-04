package com.example.momo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun hslToColor(hue: Float, saturation: Float, lightness: Float, alpha: Float = 1f): Color {
    val c = (1f - Math.abs(2f * lightness - 1f)) * saturation
    val x = c * (1f - Math.abs((hue / 60f) % 2f - 1f))
    val m = lightness - c / 2f
    val (r, g, b) = when {
        hue < 60f -> Triple(c, x, 0f)
        hue < 120f -> Triple(x, c, 0f)
        hue < 180f -> Triple(0f, c, x)
        hue < 240f -> Triple(0f, x, c)
        hue < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    return Color(
        red = (r + m).coerceIn(0f, 1f),
        green = (g + m).coerceIn(0f, 1f),
        blue = (b + m).coerceIn(0f, 1f),
        alpha = alpha
    )
}

@Composable
fun SpiritualArt(
    artType: String,
    hue: Float,
    modifier: Modifier = Modifier
) {
    val color1 = hslToColor(hue, 0.8f, 0.5f)
    val color2 = hslToColor((hue + 45f) % 360f, 0.8f, 0.3f)
    val colorBg = hslToColor((hue + 180f) % 360f, 0.2f, 0.08f) // Complementary dark background

    Box(
        modifier = modifier
            .background(Brush.verticalGradient(listOf(colorBg, Color(0xFF0F0F15))))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxRadius = Math.min(size.width, size.height) * 0.42f

            // 1. Draw glowing background aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color1.copy(alpha = 0.3f), Color.Transparent),
                    center = center,
                    radius = maxRadius * 1.5f
                ),
                center = center,
                radius = maxRadius * 1.5f
            )

            when (artType.uppercase()) {
                "MANDALA" -> {
                    // Draw outer ring with dots
                    drawCircle(color = color1.copy(alpha = 0.5f), center = center, radius = maxRadius, style = Stroke(width = 2.dp.toPx()))

                    val numDots = 24
                    for (i in 0 until numDots) {
                        val angle = (i * 2 * PI / numDots).toFloat()
                        val dotCenter = Offset(
                            x = center.x + maxRadius * cos(angle),
                            y = center.y + maxRadius * sin(angle)
                        )
                        drawCircle(color = color1, center = dotCenter, radius = 4.dp.toPx())
                    }

                    // Draw inner petals/loops
                    val numPetals = 12
                    for (i in 0 until numPetals) {
                        rotate(degrees = (i * 360f / numPetals), pivot = center) {
                            drawOval(
                                color = color1.copy(alpha = 0.4f),
                                topLeft = Offset(center.x - maxRadius * 0.25f, center.y - maxRadius * 0.85f),
                                size = Size(maxRadius * 0.5f, maxRadius * 0.8f),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }

                    // Inner geometric rings
                    drawCircle(color = color2, center = center, radius = maxRadius * 0.4f, style = Stroke(width = 2.dp.toPx()))
                    drawCircle(color = color1, center = center, radius = maxRadius * 0.15f)
                }

                "YANTRA" -> {
                    // Draw square outer gateway (Bhupura)
                    val squareSize = maxRadius * 1.8f
                    val strokeWidth = 3.dp.toPx()
                    val squarePath = Path().apply {
                        val left = center.x - squareSize / 2
                        val right = center.x + squareSize / 2
                        val top = center.y - squareSize / 2
                        val bottom = center.y + squareSize / 2
                        val gatewaySize = squareSize * 0.2f

                        // Draw square with T-shaped gateways on 4 sides
                        moveTo(left, top)
                        lineTo(center.x - gatewaySize / 2, top)
                        lineTo(center.x - gatewaySize / 2, top + gatewaySize * 0.2f)
                        lineTo(center.x + gatewaySize / 2, top + gatewaySize * 0.2f)
                        lineTo(center.x + gatewaySize / 2, top)
                        lineTo(right, top)

                        lineTo(right, center.y - gatewaySize / 2)
                        lineTo(right - gatewaySize * 0.2f, center.y - gatewaySize / 2)
                        lineTo(right - gatewaySize * 0.2f, center.y + gatewaySize / 2)
                        lineTo(right, center.y + gatewaySize / 2)
                        lineTo(right, bottom)

                        lineTo(center.x + gatewaySize / 2, bottom)
                        lineTo(center.x + gatewaySize / 2, bottom - gatewaySize * 0.2f)
                        lineTo(center.x - gatewaySize / 2, bottom - gatewaySize * 0.2f)
                        lineTo(center.x - gatewaySize / 2, bottom)
                        lineTo(left, bottom)

                        lineTo(left, center.y + gatewaySize / 2)
                        lineTo(left + gatewaySize * 0.2f, center.y + gatewaySize / 2)
                        lineTo(left + gatewaySize * 0.2f, center.y - gatewaySize / 2)
                        lineTo(left, center.y - gatewaySize / 2)
                        close()
                    }
                    drawPath(path = squarePath, color = color2.copy(alpha = 0.8f), style = Stroke(width = strokeWidth))

                    // Draw concentric lotus circles
                    drawCircle(color = color1.copy(alpha = 0.6f), center = center, radius = maxRadius * 0.75f, style = Stroke(width = 2.dp.toPx()))

                    // Draw intersecting triangles (Shatkona style)
                    val triSize = maxRadius * 0.6f
                    val pathTriUp = Path().apply {
                        moveTo(center.x, center.y - triSize)
                        lineTo(center.x + triSize * 0.86f, center.y + triSize * 0.5f)
                        lineTo(center.x - triSize * 0.86f, center.y + triSize * 0.5f)
                        close()
                    }
                    drawPath(path = pathTriUp, color = color1.copy(alpha = 0.5f), style = Stroke(width = 2.dp.toPx()))

                    val pathTriDown = Path().apply {
                        moveTo(center.x, center.y + triSize)
                        lineTo(center.x + triSize * 0.86f, center.y - triSize * 0.5f)
                        lineTo(center.x - triSize * 0.86f, center.y - triSize * 0.5f)
                        close()
                    }
                    drawPath(path = pathTriDown, color = color1.copy(alpha = 0.5f), style = Stroke(width = 2.dp.toPx()))

                    // Central Bindu
                    drawCircle(color = color1, center = center, radius = 5.dp.toPx())
                }

                "CHARIOT_WHEEL" -> {
                    // Outer rim
                    drawCircle(color = color1, center = center, radius = maxRadius, style = Stroke(width = 10.dp.toPx()))
                    drawCircle(color = color2, center = center, radius = maxRadius - 10.dp.toPx(), style = Stroke(width = 2.dp.toPx()))

                    // Inner hub
                    drawCircle(color = color1, center = center, radius = maxRadius * 0.25f, style = Stroke(width = 5.dp.toPx()))
                    drawCircle(color = color2, center = center, radius = maxRadius * 0.12f)

                    // Spokes (Dharma Wheel style - 12 spokes)
                    val spokes = 12
                    for (i in 0 until spokes) {
                        val angle = (i * 2 * PI / spokes).toFloat()
                        val start = Offset(
                            x = center.x + maxRadius * 0.25f * cos(angle),
                            y = center.y + maxRadius * 0.25f * sin(angle)
                        )
                        val end = Offset(
                            x = center.x + (maxRadius - 10.dp.toPx()) * cos(angle),
                            y = center.y + (maxRadius - 10.dp.toPx()) * sin(angle)
                        )
                        drawLine(color = color1.copy(alpha = 0.8f), start = start, end = end, strokeWidth = 4.dp.toPx())
                    }
                }

                else -> { // SACRED_GEOMETRY / FLOWER_OF_LIFE
                    val step = maxRadius * 0.35f
                    // Draw primary center seed circles
                    drawCircle(color = color1.copy(alpha = 0.4f), center = center, radius = step, style = Stroke(width = 1.5.dp.toPx()))
                    for (i in 0 until 6) {
                        val angle = (i * 2 * PI / 6).toFloat()
                        val circleCenter = Offset(
                            x = center.x + step * cos(angle),
                            y = center.y + step * sin(angle)
                        )
                        drawCircle(color = color1.copy(alpha = 0.4f), center = circleCenter, radius = step, style = Stroke(width = 1.5.dp.toPx()))
                    }

                    // Outer containment circle
                    drawCircle(color = color1, center = center, radius = maxRadius, style = Stroke(width = 3.dp.toPx()))
                    drawCircle(color = color2, center = center, radius = maxRadius * 0.95f, style = Stroke(width = 1.dp.toPx()))
                }
            }
        }
    }
}

@Composable
fun SpiritualAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier
) {
    if (avatarUrl.startsWith("spiritual://AVATAR")) {
        val colorHex = avatarUrl.substringAfter("color=").substringBefore("&name=").replace("%23", "#")
        val name = java.net.URLDecoder.decode(avatarUrl.substringAfter("name="), "UTF-8")
        val initial = name.firstOrNull()?.toString()?.uppercase() ?: "K"
        val backgroundColor = try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            Color(0xFFFFD700)
        }

        Box(
            modifier = modifier
                .background(backgroundColor, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    } else {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}
