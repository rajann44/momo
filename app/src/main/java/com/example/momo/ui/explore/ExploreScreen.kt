package com.example.momo.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.example.momo.PostDetail
import com.example.momo.data.DummyData
import com.example.momo.data.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val explorePosts = remember { DummyData.explorePosts }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // Search Bar Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(
                        text = "Search",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        // Staggered Grid Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // We chunk our list of posts to create a repeating pattern:
            // 1. One 2x2 image on left, and two 1x1 images stacked on right
            // 2. Row of three 1x1 images
            // 3. One 2x2 image on right, and two 1x1 images stacked on left
            // 4. Row of three 1x1 images
            
            val chunks = explorePosts.chunked(9)
            
            items(chunks.size) { chunkIndex ->
                val chunk = chunks[chunkIndex]
                ExploreGridPattern(
                    posts = chunk,
                    onPostClick = { postId -> onItemClick(PostDetail(postId)) }
                )
            }
        }
    }
}

@Composable
fun ExploreGridPattern(
    posts: List<Post>,
    onPostClick: (String) -> Unit
) {
    // 9 items per full pattern, or whatever is remaining
    val size = posts.size

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section 1: Left 2x2 + Right stack of two 1x1 (Uses 3 items)
        if (size >= 3) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left 2x2 large item
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .aspectRatio(1f)
                        .padding(0.5.dp)
                        .clickable { onPostClick(posts[0].id) }
                ) {
                    AsyncImage(
                        model = posts[0].imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Right stacked 1x1 items
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.5f)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[1].id) }
                    ) {
                        AsyncImage(
                            model = posts[1].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[2].id) }
                    ) {
                        AsyncImage(
                            model = posts[2].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        } else if (size > 0) {
            // Fallback for remaining items
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 0 until size) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[i].id) }
                    ) {
                        AsyncImage(
                            model = posts[i].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                for (i in size until 3) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Section 2: Three 1x1 items (Uses items 3, 4, 5)
        if (size >= 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 3..5) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[i].id) }
                    ) {
                        AsyncImage(
                            model = posts[i].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        } else if (size > 3) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 3 until size) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[i].id) }
                    ) {
                        AsyncImage(
                            model = posts[i].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                for (i in size until 6) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Section 3: Left stack of two 1x1 + Right 2x2 (Uses items 6, 7, 8)
        if (size >= 9) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Left stacked 1x1 items
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.5f)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[6].id) }
                    ) {
                        AsyncImage(
                            model = posts[6].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[7].id) }
                    ) {
                        AsyncImage(
                            model = posts[7].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Right 2x2 large item
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .aspectRatio(1f)
                        .padding(0.5.dp)
                        .clickable { onPostClick(posts[8].id) }
                ) {
                    AsyncImage(
                        model = posts[8].imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else if (size > 6) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 6 until size) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(0.5.dp)
                            .clickable { onPostClick(posts[i].id) }
                    ) {
                        AsyncImage(
                            model = posts[i].imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                for (i in size until 9) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
