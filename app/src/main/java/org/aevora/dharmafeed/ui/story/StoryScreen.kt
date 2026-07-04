package org.aevora.dharmafeed.ui.story

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.aevora.dharmafeed.ui.components.SpiritualAvatar
import org.aevora.dharmafeed.ui.components.SpiritualArt
import org.aevora.dharmafeed.data.DummyData
import org.aevora.dharmafeed.data.StoryItem
import org.aevora.dharmafeed.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryScreen(
    userId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user = remember(userId) { DummyData.getUserById(userId) }
    val stories = remember(userId) { DummyData.getStoriesForUser(userId) }
    
    if (stories.isEmpty()) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "No active stories for ${user.username}", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
        return
    }

    var currentSlide by remember { mutableIntStateOf(0) }
    val totalSlides = stories.size
    val activeStory = stories[currentSlide]
    
    val progress = remember { Animatable(0f) }
    
    // Auto-advance logic
    LaunchedEffect(currentSlide) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
        )
        if (currentSlide < totalSlides - 1) {
            currentSlide++
        } else {
            onBackClick()
        }
    }

    // Reaction and message states
    var replyText by remember { mutableStateOf(TextFieldValue("")) }
    var isLiked by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Content Renderer (Image or Shared Post Frame)
        if (activeStory.isPostShare) {
            // Background Story Gradient
            val storyGradient = Brush.verticalGradient(
                colors = listOf(Color(0xFF7A7369), Color(0xFFB8A695))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(storyGradient),
                contentAlignment = Alignment.Center
            ) {
                // Shared Post Inner Card
                val innerCardGradient = Brush.verticalGradient(
                    colors = listOf(Color(0xFFD9CEC1), Color(0xFFD4BDA9))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(9f / 14f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(innerCardGradient)
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                ) {
                    // Inner Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         SpiritualAvatar(
                            avatarUrl = activeStory.sharedPostUser?.avatarUrl ?: "",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeStory.sharedPostUser?.username ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C)
                        )
                    }
                    
                    // Shared Image
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activeStory.sharedPostImg.startsWith("spiritual://")) {
                            val uri = activeStory.sharedPostImg
                            val parts = uri.substring("spiritual://".length).split("?hue=")
                            val artType = parts.getOrNull(0) ?: "MANDALA"
                            val hue = parts.getOrNull(1)?.toFloatOrNull() ?: 0f
                            SpiritualArt(artType = artType, hue = hue, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f))
                        } else {
                            AsyncImage(
                                model = activeStory.sharedPostImg,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.8f),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    
                    // Bottom space (empty space to match mock design height)
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        } else {
            // Standard Full-screen Image Story
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (activeStory.imageUrl.startsWith("spiritual://")) {
                    val uri = activeStory.imageUrl
                    val parts = uri.substring("spiritual://".length).split("?hue=")
                    val artType = parts.getOrNull(0) ?: "MANDALA"
                    val hue = parts.getOrNull(1)?.toFloatOrNull() ?: 0f
                    SpiritualArt(artType = artType, hue = hue, modifier = Modifier.fillMaxSize())
                } else {
                    AsyncImage(
                        model = activeStory.imageUrl,
                        contentDescription = "Story Media",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                if (activeStory.text.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(24.dp)
                    ) {
                        Text(
                            text = activeStory.text,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // 2. Navigation Click Overlay (Left 40% goes Back, Right 60% goes Forward)
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (currentSlide > 0) {
                            currentSlide--
                        } else {
                            onBackClick()
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (currentSlide < totalSlides - 1) {
                            currentSlide++
                        } else {
                            onBackClick()
                        }
                    }
            )
        }

        // 3. UI Overlays (Header, Progress Indicators, Footer)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header Elements
            Column(modifier = Modifier.fillMaxWidth()) {
                // Progress Bar Rows
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until totalSlides) {
                        val progressVal = when {
                            i < currentSlide -> 1f
                            i == currentSlide -> progress.value
                            else -> 0f
                        }
                        LinearProgressIndicator(
                            progress = progressVal,
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.4f)
                        )
                    }
                }

                // User Row & Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SpiritualAvatar(
                            avatarUrl = user.avatarUrl,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = user.username,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeStory.timeAgo,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { /* More options */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Story",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Footer Elements (Send Message + Reactions)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Reply Text Input
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (replyText.text.isEmpty()) {
                        Text(
                            text = "Send message...",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    BasicTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (replyText.text.isNotBlank()) {
                                    Toast.makeText(context, "Reply sent to ${user.username}!", Toast.LENGTH_SHORT).show()
                                    replyText = TextFieldValue("")
                                }
                            }
                        )
                    )
                }

                // Heart Icon Reaction
                IconButton(
                    onClick = { isLiked = !isLiked }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like Story",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Share / Send Icon
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Shared to Direct Messages!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Share Story",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .rotate(-12f)
                    )
                }
            }
        }
    }
}
