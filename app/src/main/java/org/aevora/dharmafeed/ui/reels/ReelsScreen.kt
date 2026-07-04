package org.aevora.dharmafeed.ui.reels

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import org.aevora.dharmafeed.Comments
import org.aevora.dharmafeed.UserProfile
import org.aevora.dharmafeed.data.DummyData
import org.aevora.dharmafeed.data.Reel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReelsScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val reelsList = remember { DummyData.reels }
    val pagerState = rememberPagerState(pageCount = { reelsList.size })
    var reelsState = remember { mutableStateMapOf<String, Reel>().apply {
        reelsList.forEach { put(it.id, it) }
    }}

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val reel = reelsList[page]
            val activeReel = reelsState[reel.id] ?: reel
            
            val isCurrentPage = pagerState.currentPage == page
            ReelPageItem(
                reel = activeReel,
                isCurrentPage = isCurrentPage,
                onUserClick = { userId -> onItemClick(UserProfile(userId)) },
                onLikeClick = {
                    reelsState[reel.id] = activeReel.copy(
                        isLiked = !activeReel.isLiked,
                        likesCount = activeReel.likesCount + if (activeReel.isLiked) -1 else 1
                    )
                },
                onCommentClick = { onItemClick(Comments(reel.id)) }
            )
        }

        // Top bar overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* Launch camera */ }) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun ReelPageItem(
    reel: Reel,
    isCurrentPage: Boolean,
    onUserClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isBookmarked by remember { mutableStateOf(reel.isBookmarked) }
    var isCaptionExpanded by remember { mutableStateOf(false) }

    // Disk Rotation animation
    val diskRotation = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        diskRotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
            playWhenReady = isCurrentPage
        }
    }

    LaunchedEffect(reel.videoUrl) {
        val mediaItem = MediaItem.fromUri(reel.videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    LaunchedEffect(isCurrentPage) {
        if (isCurrentPage) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Video Player View
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.player = exoPlayer
            }
        )

        var isPlayerReady by remember { mutableStateOf(false) }
        DisposableEffect(exoPlayer) {
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    isPlayerReady = (playbackState == Player.STATE_READY)
                }
            }
            exoPlayer.addListener(listener)
            onDispose {
                exoPlayer.removeListener(listener)
            }
        }

        // Show placeholder image until video is ready/playing
        if (!isPlayerReady) {
            AsyncImage(
                model = reel.imageUrl,
                contentDescription = "Reel background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Top Overlay Gradient (similar to Spark Supperclub top-overlay)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
        )

        // Bottom Overlay Gradient (similar to Spark Supperclub bottom-overlay: height = 45%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Bottom Details and Right Actions Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp)
            ) {
                // User Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onUserClick(reel.user.id) }
                ) {
                    AsyncImage(
                        model = reel.user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = reel.user.username,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { /* Follow */ },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderDefaults.outlinedBorder.copy(width = 1.dp, brush = Brush.linearGradient(listOf(Color.White, Color.White))),
                        modifier = Modifier.height(28.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Text("Follow", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Caption
                Text(
                    text = reel.caption,
                    fontSize = 13.sp,
                    color = Color.White,
                    maxLines = if (isCaptionExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { isCaptionExpanded = !isCaptionExpanded }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Audio Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Music",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = reel.audioName,
                        fontSize = 11.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(140.dp)
                    )
                }
            }

            // Right actions column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Like Button
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (reel.isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "${reel.likesCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Comment Button
                IconButton(onClick = onCommentClick) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = "${reel.commentsCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Share Button
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Bookmark Button
                IconButton(onClick = { isBookmarked = !isBookmarked }) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Options Button
                IconButton(onClick = { /* Options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Rotating Audio Icon
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(diskRotation.value)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = reel.user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

// BorderDefaults mock helper
object BorderDefaults {
    val outlinedBorder = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
}
