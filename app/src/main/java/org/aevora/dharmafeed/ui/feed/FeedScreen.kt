package org.aevora.dharmafeed.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.ui.draw.blur
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import org.aevora.dharmafeed.AIReelAssistant
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.res.stringResource
import org.aevora.dharmafeed.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import org.aevora.dharmafeed.Comments
import org.aevora.dharmafeed.PostDetail
import org.aevora.dharmafeed.UserProfile
import androidx.compose.ui.platform.LocalContext
import org.aevora.dharmafeed.StoryView
import org.aevora.dharmafeed.data.DummyData
import org.aevora.dharmafeed.data.Post
import org.aevora.dharmafeed.data.User
import org.aevora.dharmafeed.data.MahabharataDatabase
import org.aevora.dharmafeed.ui.components.SpiritualArt
import org.aevora.dharmafeed.ui.components.SpiritualAvatar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeDay = DummyData.activeDay
    val postsState = remember { mutableStateMapOf<String, Post>() }

    LaunchedEffect(activeDay) {
        postsState.clear()
        DummyData.posts.forEach { postsState[it.id] = it }
    }



    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Custom Top Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 26.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 1.sp
                )
            },
            actions = {
                IconButton(onClick = { /* Notifications */ }) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Notifications")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {


            // Stories Header
            item {
                StoriesRow(users = DummyData.users.filter { it.hasActiveStory }, onStoryClick = { user ->
                    if (user.hasActiveStory) {
                        onItemClick(StoryView(user.id))
                    } else {
                        onItemClick(UserProfile(user.id))
                    }
                })
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)
            }

            // Gita Verse of the Day Banner
            item {
                val dailyContent = MahabharataDatabase.getContentForDay(DummyData.activeDay)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = "DAY ${DummyData.activeDay}: ${dailyContent.title}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary, // Primary color for high contrast
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dailyContent.gitaVerse.verse,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = dailyContent.gitaVerse.translation,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Guidance: ${dailyContent.gitaVerse.commentary}",
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.secondary // Secondary color for high contrast
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)
            }

            // Posts list
            items(postsState.values.toList(), key = { it.id }) { post ->
                PostItem(
                    post = post,
                    onUserClick = { userId -> onItemClick(UserProfile(userId)) },
                    onLikeClick = {
                        val currentPost = postsState[post.id] ?: post
                        postsState[post.id] = currentPost.copy(
                            isLiked = !currentPost.isLiked,
                            likesCount = currentPost.likesCount + if (currentPost.isLiked) -1 else 1
                        )
                    },
                    onCommentClick = { postId -> onItemClick(Comments(postId)) },
                    onPostClick = { postId -> onItemClick(PostDetail(postId)) }
                )
            }
        }
    }


}

@Composable
fun StoriesRow(
    users: List<User>,
    onStoryClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.width(4.dp)) }
        items(users) { user ->
            StoryCircle(user = user, onClick = { onStoryClick(user) })
        }
        item { Spacer(modifier = Modifier.width(4.dp)) }
    }
}

@Composable
fun StoryCircle(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val instagramGradient = Brush.sweepGradient(
        colors = listOf(
            Color(0xFF833AB4), // Purple
            Color(0xFFF77737), // Orange
            Color(0xFFE1306C), // Pink
            Color(0xFFC13584), // Magenta
            Color(0xFFFD1D1D), // Red
            Color(0xFF833AB4)  // Close loop
        )
    )

    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .padding(3.dp),
            contentAlignment = Alignment.Center
        ) {
            // Story Ring
            if (user.hasActiveStory) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, instagramGradient, CircleShape)
                )
            }

            SpiritualAvatar(
                avatarUrl = user.avatarUrl,
                modifier = Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = if (user.id == "current_user") "Your Story" else user.username,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(72.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PostItem(
    post: Post,
    onUserClick: (String) -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: (String) -> Unit,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBookmarked = DummyData.bookmarkedPostIds.contains(post.id)
    var isCaptionExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpiritualAvatar(
                avatarUrl = post.user.avatarUrl,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onUserClick(post.user.id) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = post.user.username,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable { onUserClick(post.user.id) }
                )
                Text(
                    text = if (post.user.id == "current_user") "Seeking Wisdom" else post.user.bio.substringBefore("."),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
            IconButton(onClick = { /* Options */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Post Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .clickable { onPostClick(post.id) },
            contentAlignment = Alignment.Center
        ) {
            if (post.imageUrl.startsWith("spiritual://")) {
                val uri = post.imageUrl
                val parts = uri.substring("spiritual://".length).split("?hue=")
                val artType = parts.getOrNull(0) ?: "MANDALA"
                val hue = parts.getOrNull(1)?.toFloatOrNull() ?: 0f
                SpiritualArt(artType = artType, hue = hue, modifier = Modifier.fillMaxSize())
            } else {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Post Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Text overlay on post image container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = post.caption,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp
                )
            }
        }

        // Actions Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp)
                )
            }
            IconButton(onClick = { onCommentClick(post.id) }) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { /* Share */ }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { DummyData.toggleBookmark(context, post.id) }) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Outlined.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Likes, Caption, Comments Metadata
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = "${post.likesCount} likes",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            // Expandable Caption
            val captionText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${post.user.username} ")
                }
                append(post.caption)
            }
            
            Text(
                text = captionText,
                fontSize = 13.sp,
                maxLines = if (isCaptionExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isCaptionExpanded = !isCaptionExpanded
                    }
            )
            
            if (post.commentsCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "View all ${post.commentsCount} comments",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.clickable { onCommentClick(post.id) }
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.timeAgo,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
