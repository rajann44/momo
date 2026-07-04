package com.example.momo.ui.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.momo.ui.components.SpiritualAvatar
import com.example.momo.data.DummyData
import com.example.momo.data.Message
import androidx.navigation3.runtime.NavKey
import com.example.momo.FriendshipRecap
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    userId: String,
    onBackClick: () -> Unit,
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val friend = remember(userId) { DummyData.getUserById(userId) }
    val listState = rememberLazyListState()
    var isWatchTogetherActive by remember { mutableStateOf(false) }
    
    // Local list of messages to enable interactive sending
    val chatMessages = remember(userId, DummyData.activeDay) {
        mutableStateListOf<Message>().apply {
            addAll(DummyData.getConversationWith(userId))
        }
    }
    
    var newMessageText by remember { mutableStateOf("") }
    
    // Auto scroll to bottom when a new message is added
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val threadTitle = when (userId) {
            "krishna" -> "Krishna & Arjuna"
            "arjuna" -> "Yudhishthira & Bhishma"
            "duryodhana" -> "Duryodhana & Karna"
            "shakuni" -> "Shakuni & Duryodhana"
            else -> "${friend.name} & Seeker"
        }
        val threadSubtitle = "Sacred Correspondence • Day ${DummyData.activeDay}"

        // Chat Header Top Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* View profile */ }
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        SpiritualAvatar(
                            avatarUrl = friend.avatarUrl,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        if (friend.isOnline) {
                            Box(
                                modifier = Modifier
                                    .size(11.dp)
                                    .background(Color(0xFF4CAF50), CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = threadTitle,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = threadSubtitle,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            actions = {},
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
        )
        
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)

        if (isWatchTogetherActive) {
            // Watch Together split screen co-viewing layout
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Synced Reels Player (Top 55% height)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.55f)
                        .background(Color.Black)
                ) {
                    val videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4"
                    val context = LocalContext.current
                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            repeatMode = Player.REPEAT_MODE_ALL
                            playWhenReady = true
                        }
                    }

                    LaunchedEffect(videoUrl) {
                        val mediaItem = MediaItem.fromUri(videoUrl)
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            exoPlayer.release()
                        }
                    }

                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false
                                resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Live camera picture-in-picture circle mockup
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF3897F0), CircleShape)
                            .align(Alignment.TopEnd)
                    ) {
                        SpiritualAvatar(
                            avatarUrl = friend.avatarUrl,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Co-viewing synced status text overlay
                    Text(
                        text = "Watching Together (Synced) 🔴",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.TopStart)
                    )

                    // Reels metadata overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Big Buck Bunny 🐰",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Synced session with ${friend.name}",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Flying emojis reaction rendering area
                    val reactionEmojis = remember { mutableStateListOf<Pair<String, Float>>() }
                    reactionEmojis.forEach { (emoji, xOffsetFraction) ->
                        var yOffsetFraction by remember { mutableStateOf(1f) }
                        LaunchedEffect(Unit) {
                            val duration = 1500
                            val startTime = System.currentTimeMillis()
                            while (System.currentTimeMillis() - startTime < duration) {
                                val progress = (System.currentTimeMillis() - startTime).toFloat() / duration
                                yOffsetFraction = 1f - progress
                                delay(16)
                            }
                            reactionEmojis.remove(Pair(emoji, xOffsetFraction))
                        }
                        Text(
                            text = emoji,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(
                                    start = (xOffsetFraction * 260).dp,
                                    bottom = (yOffsetFraction * 180).dp
                                )
                        )
                    }

                    // Floating reaction buttons bar
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("❤️", "😂", "🔥", "😮").forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clickable {
                                        reactionEmojis.add(
                                            Pair(emoji, (0.2f + Math.random().toFloat() * 0.6f))
                                        )
                                    }
                                    .padding(4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)

                // DM Chat Area in split-screen (Bottom 45% height)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.45f)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(6.dp)) }
                        items(chatMessages, key = { it.id }) { message ->
                            val isCurrentUser = message.user.id == DummyData.currentUser.id
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isCurrentUser) Color(0xFFFF8C00)
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = message.text,
                                        fontSize = 13.sp,
                                        color = if (isCurrentUser) Color.White else MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(6.dp)) }
                    }
                }
            }
        } else {
            // Normal DM Thread (takes full height weight)
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                items(chatMessages, key = { it.id }) { message ->
                    val isRightSide = message.isFromMe
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isRightSide) Arrangement.End else Arrangement.Start
                    ) {
                        if (!isRightSide) {
                            SpiritualAvatar(
                                avatarUrl = message.user.avatarUrl,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        Column(
                            horizontalAlignment = if (isRightSide) Alignment.End else Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isRightSide) 16.dp else 4.dp,
                                            bottomEnd = if (isRightSide) 4.dp else 16.dp
                                        )
                                    )
                                    .background(
                                        if (isRightSide) Color(0xFFFF8C00) // Premium saffron bubble
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f) // Grey bubble
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = message.text,
                                    fontSize = 14.sp,
                                    color = if (isRightSide) Color.White else MaterialTheme.colorScheme.onBackground,
                                    lineHeight = 19.sp
                                )
                            }
                            
                            Text(
                                text = message.timeAgo,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                            )
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)

        // Bottom Message Input Row replaced with historical archive notice
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "This is a historical archive of their sacred correspondence.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
