package com.example.momo.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.momo.data.DummyData
import com.example.momo.ui.explore.ExploreScreen
import com.example.momo.ui.feed.FeedScreen
import com.example.momo.ui.moments.MomentsScreen
import com.example.momo.ui.messages.MessagesScreen
import com.example.momo.ui.profile.ProfileScreen
import com.example.momo.ui.reels.ReelsScreen

@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = if (selectedTab == 3) Color.Black.copy(alpha = 0.85f) else MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                tonalElevation = 8.dp
            ) {
                val itemColor = if (selectedTab == 3) Color.White else MaterialTheme.colorScheme.onBackground

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = itemColor,
                        unselectedIconColor = itemColor.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Explore"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = itemColor,
                        unselectedIconColor = itemColor.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.PhotoCamera else Icons.Outlined.PhotoCamera,
                            contentDescription = "Moments"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = itemColor,
                        unselectedIconColor = itemColor.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                            contentDescription = "Reels"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = itemColor,
                        unselectedIconColor = itemColor.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 4) Icons.Filled.Send else Icons.Outlined.Send,
                            contentDescription = "Messages"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = itemColor,
                        unselectedIconColor = itemColor.copy(alpha = 0.6f),
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 5,
                    onClick = { selectedTab = 5 },
                    icon = {
                        AsyncImage(
                            model = DummyData.currentUser.avatarUrl,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (selectedTab == 5) 1.5.dp else 0.dp,
                                    color = itemColor,
                                    shape = CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> FeedScreen(onItemClick = onItemClick)
                1 -> ExploreScreen(onItemClick = onItemClick)
                2 -> MomentsScreen()
                3 -> ReelsScreen(onItemClick = onItemClick)
                4 -> MessagesScreen(onItemClick = onItemClick)
                5 -> ProfileScreen(
                    userId = DummyData.currentUser.id,
                    onBackClick = {},
                    onItemClick = onItemClick,
                    isPersonalProfile = true
                )
            }
        }
    }
}
