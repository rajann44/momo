package com.example.momo.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.momo.ui.components.SpiritualAvatar
import com.example.momo.ui.components.SpiritualArt
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.momo.PostDetail
import com.example.momo.ChatDetail
import com.example.momo.data.DummyData
import com.example.momo.data.User
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.res.stringResource
import com.example.momo.R
import com.example.momo.data.MahabharataDatabase
import com.example.momo.data.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    onBackClick: () -> Unit,
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    isPersonalProfile: Boolean = false
) {
    var refreshTrigger by remember { mutableStateOf(0) }
    var showSettingsMenu by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val user = remember(userId, refreshTrigger) { DummyData.getUserById(userId) }
    var isEditing by remember { mutableStateOf(false) }
    val userPosts = remember(userId) {
        val filtered = DummyData.posts.filter { it.user.id == userId }
        if (filtered.isEmpty()) {
            listOf(
                DummyData.posts[0].copy(id = "up_1", imageUrl = "https://images.unsplash.com/photo-1502082553048-f009c37129b9?w=600"),
                DummyData.posts[1].copy(id = "up_2", imageUrl = "https://images.unsplash.com/photo-1546182990-dffeafbe841d?w=600"),
                DummyData.posts[2].copy(id = "up_3", imageUrl = "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=600")
            )
        } else {
            filtered
        }
    }

    val bookmarkedPosts = remember(DummyData.bookmarkedPostIds.size, DummyData.activeDay) {
        val allDatabasePosts = MahabharataDatabase.dailyContent.flatMap { it.posts }.map { p ->
            DummyData.transformPost(p, isBookmarked = true, timeAgoText = "Day ${p.id.split("_")[1]}")
        }
        allDatabasePosts.filter { DummyData.bookmarkedPostIds.contains(it.id) }.distinctBy { it.id }
    }

    var isFollowingState by remember { mutableStateOf(user.isFollowing) }
    var activeTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                if (!isPersonalProfile) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            },
            actions = {
                if (isPersonalProfile) {
                    Box {
                        IconButton(onClick = { showSettingsMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = { showSettingsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.select_language)) },
                                onClick = {
                                    showLanguageDialog = true
                                    showSettingsMenu = false
                                }
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
        )

        // Profile content inside LazyVerticalGrid to enable full scroll with grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header stats & details (Spans all columns)
            item(span = { GridItemSpan(3) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (user.id == "current_user") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.dev_panel_title),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${stringResource(id = R.string.current_day)}: ${DummyData.activeDay}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    val context = LocalContext.current
                                    IconButton(
                                        onClick = {
                                            if (DummyData.activeDay > 1) {
                                                DummyData.saveDay(context, DummyData.activeDay - 1)
                                                refreshTrigger++
                                            }
                                        },
                                        enabled = DummyData.activeDay > 1
                                    ) {
                                        Text("<", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                    IconButton(
                                        onClick = {
                                            if (DummyData.activeDay < 30) {
                                                DummyData.saveDay(context, DummyData.activeDay + 1)
                                                refreshTrigger++
                                            }
                                        },
                                        enabled = DummyData.activeDay < 30
                                    ) {
                                        Text(">", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }

                    // Profile picture and stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpiritualAvatar(
                            avatarUrl = user.avatarUrl,
                            modifier = Modifier
                                .size(86.dp)
                                .clip(CircleShape)
                        )
                        
                        Spacer(modifier = Modifier.width(32.dp))
                        
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val postCountText = if (isPersonalProfile) "${bookmarkedPosts.size}" else "${userPosts.size}"
                            val postsLabel = if (isPersonalProfile) "Saved" else "Posts"
                            ProfileStat(number = postCountText, label = postsLabel)
                            ProfileStat(number = "${user.followersCount}", label = "Followers")
                            ProfileStat(number = "${user.followingCount}", label = "Following")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio Details
                    val displayName = if (user.name.isBlank()) "Unnamed Seeker" else user.name
                    val displayBio = if (user.bio.isBlank()) "No spiritual bio added yet. Tap 'Edit Profile' to add your bio." else user.bio
                    val isBioEmpty = user.bio.isBlank()
                    val isNameEmpty = user.name.isBlank()

                    Text(
                        text = displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isNameEmpty) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = displayBio,
                        fontSize = 13.sp,
                        color = if (isBioEmpty) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        style = androidx.compose.ui.text.TextStyle(fontStyle = if (isBioEmpty) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal)
                    )
                    // Website display removed

                    Spacer(modifier = Modifier.height(18.dp))

                    // Actions Row
                    if (isPersonalProfile) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ProfileActionButton(
                                text = stringResource(id = R.string.edit_profile),
                                onClick = { isEditing = true },
                                modifier = Modifier.weight(1f)
                            )
                            ProfileActionButton(
                                text = stringResource(id = R.string.share_profile),
                                onClick = { /* Share */ },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Follow Button
                            if (isFollowingState) {
                                ProfileActionButton(
                                    text = stringResource(id = R.string.following),
                                    onClick = { isFollowingState = false },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Button(
                                    onClick = { isFollowingState = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3897F0)), // Instagram Blue
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text(stringResource(id = R.string.btn_follow), color = Color.White, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            ProfileActionButton(
                                text = stringResource(id = R.string.btn_message),
                                onClick = { onItemClick(ChatDetail(user.id)) },
                                modifier = Modifier.weight(1f)
                            )

                            // Add user outline button
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                        RoundedCornerShape(50.dp)
                                    )
                                    .clickable { /* Expand recommendations */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Suggestions",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Highlights Section
                    HighlightsSection()

                    // Tab bar row or Saved Teachings subheader
                    if (!isPersonalProfile) {
                        ProfileTabs(
                            selectedIndex = activeTab,
                            onTabSelected = { activeTab = it }
                        )
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.saved_teachings_header),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // Grid list items or Empty/Zero state
            if (isPersonalProfile && bookmarkedPosts.isEmpty()) {
                item(span = { GridItemSpan(3) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(id = R.string.no_saved_teachings),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(id = R.string.no_saved_teachings_desc),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                val displayedPosts = if (isPersonalProfile) bookmarkedPosts else (if (activeTab == 0 || activeTab == 1) userPosts else userPosts.reversed())
                items(displayedPosts, key = { "${activeTab}_${it.id}" }) { post ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(1.dp)
                            .clickable {
                                onItemClick(PostDetail(post.id))
                            },
                        contentAlignment = Alignment.BottomStart
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
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }

    if (isEditing) {
        var editName by remember { mutableStateOf(user.name) }
        var editUsername by remember { mutableStateOf(user.username) }
        var editBio by remember { mutableStateOf(user.bio) }
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { isEditing = false },
            title = { Text(text = stringResource(id = R.string.edit_profile), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text(stringResource(id = R.string.lbl_name)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text(stringResource(id = R.string.field_username)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text(stringResource(id = R.string.field_bio)) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        DummyData.saveUserProfile(
                            context = context,
                            name = editName,
                            username = editUsername,
                            bio = editBio,
                            website = ""
                        )
                        refreshTrigger++
                        isEditing = false
                    }
                ) {
                    Text(stringResource(id = R.string.btn_save))
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditing = false }) {
                    Text(stringResource(id = R.string.btn_cancel))
                }
            }
        )
    }

    if (showLanguageDialog) {
        val context = LocalContext.current
        val prefs = remember { context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE) }
        var currentLang by remember { mutableStateOf(prefs.getString("app_language", "en") ?: "en") }

        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(id = R.string.select_language), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val languages = listOf(
                        "en" to "English",
                        "hi" to "हिन्दी",
                        "sa" to "संस्कृतम्"
                    )
                    languages.forEach { (code, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentLang = code
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentLang == code,
                                onClick = { currentLang = code }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = name, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val locale = java.util.Locale(currentLang)
                        java.util.Locale.setDefault(locale)
                        val resources = context.resources
                        val configuration = resources.configuration
                        configuration.setLocale(locale)
                        resources.updateConfiguration(configuration, resources.displayMetrics)

                        // Save selection
                        prefs.edit().putString("app_language", currentLang).apply()
                        
                        showLanguageDialog = false
                        refreshTrigger++
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileStat(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = number,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ProfileActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                RoundedCornerShape(50.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun HighlightsSection(
    modifier: Modifier = Modifier
) {
    val highlightTitles = listOf("Wisdom", "Dharma", "Battle", "Exile", "Gita")
    val highlightImages = listOf(
        "spiritual://MANDALA?hue=50",
        "spiritual://YANTRA?hue=120",
        "spiritual://CHARIOT_WHEEL?hue=0",
        "spiritual://SACRED_GEOMETRY?hue=150",
        "spiritual://MANDALA?hue=280"
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(highlightTitles.size) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f), CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrl = highlightImages[index]
                    if (imageUrl.startsWith("spiritual://")) {
                        val uri = imageUrl
                        val parts = uri.substring("spiritual://".length).split("?hue=")
                        val artType = parts.getOrNull(0) ?: "MANDALA"
                        val hue = parts.getOrNull(1)?.toFloatOrNull() ?: 0f
                        SpiritualArt(artType = artType, hue = hue, modifier = Modifier.fillMaxSize().clip(CircleShape))
                    } else {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = highlightTitles[index],
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = highlightTitles[index],
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ProfileTabs(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                RoundedCornerShape(50.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tabIcons = listOf(
            Icons.Default.GridOn,
            Icons.Default.PlayArrow,
            Icons.Outlined.BookmarkBorder
        )
        val tabDescriptions = listOf("Grid", "Reels", "Saved")
        
        tabIcons.forEachIndexed { index, icon ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                        RoundedCornerShape(50.dp)
                    )
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tabDescriptions[index],
                    tint = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
