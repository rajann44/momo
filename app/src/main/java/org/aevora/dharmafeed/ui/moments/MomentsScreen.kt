package org.aevora.dharmafeed.ui.moments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.aevora.dharmafeed.data.DummyData
import org.aevora.dharmafeed.data.Moment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentsScreen(
    modifier: Modifier = Modifier
) {
    var hasPostedMoment by remember { mutableStateOf(false) }
    val momentsState = remember { mutableStateListOf<Moment>().apply {
        addAll(DummyData.moments)
    }}
    var showMomentCaptureDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Moments",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!hasPostedMoment) {
                // Locked screen layout (BeReal style)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF3897F0).copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF3897F0),
                            modifier = Modifier.size(46.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "One photo from today 📸",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Share today's prompt to unlock your friends' genuine moments.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(30.dp))
                    
                    Button(
                        onClick = { showMomentCaptureDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3897F0)),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.height(50.dp).padding(horizontal = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Capture Today's Moment ✨", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Peeking blurred row
                    Text(
                        text = "3 friends have posted today:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        momentsState.take(3).forEach { moment ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                            ) {
                                AsyncImage(
                                    model = moment.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().blur(18.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = moment.user.avatarUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp).clip(CircleShape).border(1.dp, Color.White, CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Unlocked full feed of moments
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF3897F0).copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Prompt: One photo from today 📸",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3897F0)
                            )
                            Text(
                                text = "Unlocked for today! Here are today's moments:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    items(momentsState) { moment ->
                        MomentCard(moment = moment)
                    }
                }
            }
        }

        // Mock Capture Dialog
        if (showMomentCaptureDialog) {
            AlertDialog(
                onDismissRequest = { showMomentCaptureDialog = false },
                title = {
                    Text(
                        text = "Capture Daily Moment 📸",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Choose a photo to capture for today's prompt:\n\"One photo from today\"",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val options = listOf(
                            "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600" to "Valley sunset vibes 🌅",
                            "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=600" to "Sun rays through trees 🌲",
                            "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600" to "Coding workspace setup 💻"
                        )

                        options.forEach { (imgUrl, captionText) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        momentsState.add(
                                            0,
                                            Moment(
                                                id = "mom_user_${System.currentTimeMillis()}",
                                                user = DummyData.currentUser,
                                                imageUrl = imgUrl,
                                                timeAgo = "Just now",
                                                caption = captionText
                                            )
                                        )
                                        hasPostedMoment = true
                                        showMomentCaptureDialog = false
                                    }
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = imgUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = captionText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showMomentCaptureDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun MomentCard(moment: Moment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // User Info header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = moment.user.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moment.user.username,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = moment.timeAgo,
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Image Card
        AsyncImage(
            model = moment.imageUrl,
            contentDescription = "Moment captured",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Caption & Reactions peeking
        Text(
            text = moment.caption,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 18.sp
        )
    }
}
