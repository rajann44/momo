package com.example.momo.ui.messages

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.momo.data.DummyData
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FriendshipRecapScreen(
    userId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val friend = remember(userId) { DummyData.getUserById(userId) }
    var currentSlide by remember { mutableIntStateOf(0) }
    val totalSlides = 4
    var isQuizCompleted by remember { mutableStateOf(false) }
    
    // Auto-advance slides every 4 seconds
    LaunchedEffect(currentSlide, isQuizCompleted) {
        if (isQuizCompleted) {
            delay(4000)
            if (currentSlide < totalSlides - 1) {
                currentSlide++
            } else {
                onBackClick() // Auto close on finish
            }
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE1306C), // Pink
            Color(0xFFC13584), // Magenta
            Color(0xFF833AB4), // Purple
            Color(0xFF0A0A0A)  // Deep Black
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .safeDrawingPadding()
    ) {
        if (!isQuizCompleted) {
            FriendshipTriviaQuiz(
                friend = friend,
                onBackClick = onBackClick,
                onQuizCompleted = { isQuizCompleted = true }
            )
        } else {
            // Main Clickable Area split into Left (Back) and Right (Forward)
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (currentSlide > 0) {
                                currentSlide--
                            }
                        }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
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

            // Overlay Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Top Story Progress Indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until totalSlides) {
                        val progress = when {
                            i < currentSlide -> 1f
                            i == currentSlide -> 0.5f // Simple middle state for active slide
                            else -> 0f
                        }
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }
                }

                // Header info (Friend Avatar and Recap Badge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = friend.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Momo Recap with ${friend.name}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Friendship in review ✨",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Recap",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Slide Content with Fade In/Out transition
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = currentSlide,
                        transitionSpec = {
                            fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) with
                            fadeOut(animationSpec = androidx.compose.animation.core.tween(300))
                        }
                    ) { slide ->
                        when (slide) {
                            0 -> StreakSlide(friend)
                            1 -> SharedContentSlide()
                            2 -> StatsSlide()
                            3 -> ConclusionSlide(friend)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreakSlide(friend: com.example.momo.data.User) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "🔥",
            fontSize = 84.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "You & ${friend.username}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "are on a ${friend.streakCount}-day streak!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFF9A15E), // Orange flame glow
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Keeping the conversations alive daily. Don't let it die!",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SharedContentSlide() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "🐰🍿",
            fontSize = 60.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Top Shared Reel",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Big Buck Bunny",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You shared this 15 times this month. Talk about an inside joke!",
            fontSize = 15.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun StatsSlide() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "📊💬",
            fontSize = 60.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Friendship Stats",
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("24", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Inside Jokes", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("142", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Post Likes", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("312", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Reactions", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }
        }
        
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Most used word: \"composition\" 💻",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFF96167),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ConclusionSlide(friend: com.example.momo.data.User) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "❤️🤝",
            fontSize = 72.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "True Besties",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "You and ${friend.name} are in the top 1% of Momo friendships. Here's to more genuine moments!",
            fontSize = 15.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FriendshipTriviaQuiz(
    friend: com.example.momo.data.User,
    onBackClick: () -> Unit,
    onQuizCompleted: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()
    
    val questions = remember {
        listOf(
            QuizQuestion(
                question = "Who usually sends the first Reel of the day?",
                options = listOf("Me, early bird! 🌅", "${friend.username} 🦉", "It's a dead tie! ⚖️"),
                correctIndex = 2
            ),
            QuizQuestion(
                question = "What is your shared love language?",
                options = listOf("Spamming Reels 📲", "Late-night essays ✍️", "Emoji reaction spam 🔥"),
                correctIndex = 0
            ),
            QuizQuestion(
                question = "If your chat was a movie genre, it would be:",
                options = listOf("Comedy (non-stop memes) 🎬", "Drama (deep talks) 🎭", "Sci-Fi (tech & future) 🚀"),
                correctIndex = 0
            )
        )
    }
    
    val totalQuestions = questions.size

    val handleOptionSelect: (Int) -> Unit = { index ->
        if (selectedOptionIndex == null) {
            selectedOptionIndex = index
            scope.launch {
                delay(600)
                if (currentQuestionIndex < totalQuestions) {
                    currentQuestionIndex++
                    selectedOptionIndex = null
                }
            }
        }
    }

    LaunchedEffect(currentQuestionIndex) {
        if (currentQuestionIndex == totalQuestions) {
            delay(2500)
            onQuizCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🍑 Momo Sync",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = currentQuestionIndex,
                transitionSpec = {
                    fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) with
                    fadeOut(animationSpec = androidx.compose.animation.core.tween(300))
                }
            ) { index ->
                if (index < totalQuestions) {
                    val q = questions[index]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            for (i in 0 until totalQuestions) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (i == index) Color(0xFFF9A15E)
                                            else Color.White.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }

                        Text(
                            text = "QUESTION ${index + 1} OF $totalQuestions",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = q.question,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            q.options.forEachIndexed { optIndex, optionText ->
                                val isSelected = selectedOptionIndex == optIndex
                                val isCorrectOption = optIndex == q.correctIndex
                                val buttonBg = when {
                                    isSelected && isCorrectOption -> Color(0xFF4CAF50).copy(alpha = 0.3f)
                                    isSelected -> Color(0xFFE1306C).copy(alpha = 0.3f)
                                    else -> Color.White.copy(alpha = 0.05f)
                                }
                                val buttonBorder = when {
                                    isSelected && isCorrectOption -> Color(0xFF4CAF50)
                                    isSelected -> Color(0xFFE1306C)
                                    else -> Color.White.copy(alpha = 0.15f)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                        .background(buttonBg)
                                        .border(1.dp, buttonBorder, RoundedCornerShape(25.dp))
                                        .clickable { handleOptionSelect(optIndex) }
                                        .padding(horizontal = 20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = optionText,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "⚡️",
                            fontSize = 64.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "98% Friendship Sync!",
                            color = Color(0xFFF9A15E),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You and ${friend.username} are in perfect alignment! Unlocking your custom Wrapped recap...",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        Text(
            text = "Answer honestly to unlock recap statistics",
            color = Color.White.copy(alpha = 0.4f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
    }
}

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)
