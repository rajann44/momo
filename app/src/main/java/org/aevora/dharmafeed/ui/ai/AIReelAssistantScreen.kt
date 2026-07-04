package org.aevora.dharmafeed.ui.ai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

enum class AssistantStep {
    SELECT_ASSETS,
    SELECT_TEMPLATE,
    GENERATING,
    PREVIEW
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AIReelAssistantScreen(
    onBackClick: () -> Unit,
    onPublishSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(AssistantStep.SELECT_ASSETS) }
    val selectedPhotos = remember { mutableStateListOf<String>() }
    var selectedTemplate by remember { mutableStateOf("Vintage Film") }
    var selectedAudio by remember { mutableStateOf("Lofi Dreams • Chill Vibes") }

    val dummyGallery = listOf(
        "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1472214222541-d510753a4707?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=300&h=300&fit=crop",
        "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=300&h=300&fit=crop"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "AI Reel Assistant ✨",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if (currentStep == AssistantStep.SELECT_ASSETS) {
                        onBackClick()
                    } else {
                        // Go back a step
                        currentStep = when(currentStep) {
                            AssistantStep.SELECT_TEMPLATE -> AssistantStep.SELECT_ASSETS
                            AssistantStep.PREVIEW -> AssistantStep.SELECT_TEMPLATE
                            else -> AssistantStep.SELECT_ASSETS
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), thickness = 0.5.dp)

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) with
                fadeOut(animationSpec = androidx.compose.animation.core.tween(300))
            },
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) { step ->
            when (step) {
                AssistantStep.SELECT_ASSETS -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text(
                            text = "Select photos to generate your Reel:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(dummyGallery) { photoUrl ->
                                val isSelected = selectedPhotos.contains(photoUrl)
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            if (isSelected) {
                                                selectedPhotos.remove(photoUrl)
                                            } else {
                                                selectedPhotos.add(photoUrl)
                                            }
                                        }
                                ) {
                                    AsyncImage(
                                        model = photoUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.4f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(Color(0xFFFF8C00), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { currentStep = AssistantStep.SELECT_TEMPLATE },
                            enabled = selectedPhotos.isNotEmpty(),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF8C00),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Next: Choose Template (${selectedPhotos.size} selected)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                AssistantStep.SELECT_TEMPLATE -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text(
                            text = "Choose style transfer template:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val templates = listOf("Vintage Film", "Neon Cyberpunk", "Cinematic Lofi")
                        templates.forEach { template ->
                            val isSelected = selectedTemplate == template
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedTemplate = template }
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = template + if (isSelected) " ✨" else "",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Choose trending audio:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val audios = listOf("Lofi Dreams • Chill Vibes", "Festival Sounds • Live Edit", "tech_insider • Original Audio")
                        audios.forEach { audio ->
                            val isSelected = selectedAudio == audio
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedAudio = audio }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = audio,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFFFF8C00) else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { currentStep = AssistantStep.GENERATING },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Generate AI Reel ✨", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                AssistantStep.GENERATING -> {
                    GeneratingScreen {
                        currentStep = AssistantStep.PREVIEW
                    }
                }
                AssistantStep.PREVIEW -> {
                    PreviewScreen(
                        templateName = selectedTemplate,
                        audioName = selectedAudio,
                        onPublish = onPublishSuccess
                    )
                }
            }
        }
    }
}

@Composable
fun GeneratingScreen(onGenerationDone: () -> Unit) {
    var loadingText by remember { mutableStateOf("Analyzing photo compositions...") }

    LaunchedEffect(Unit) {
        delay(1200)
        loadingText = "Syncing transitions to audio beats..."
        delay(1200)
        loadingText = "Applying style transfer..."
        delay(1200)
        loadingText = "Adding auto-captions and hashtags..."
        delay(1000)
        onGenerationDone()
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF96167),
            Color(0xFFF9A15E),
            Color(0xFFE94B8A)
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp),
                color = Color(0xFFFF8C00),
                strokeWidth = 4.dp
            )
            Text(
                text = "✨",
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "momo AI is working",
            style = androidx.compose.ui.text.TextStyle(
                brush = gradient,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = loadingText,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun PreviewScreen(
    templateName: String,
    audioName: String,
    onPublish: () -> Unit
) {
    val context = LocalContext.current
    // Use MDN flower video as the generated preview
    val videoUrl = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"
    
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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Preview generated AI Reel:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        // Video Preview Card
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
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

            // Preview Overlay Tags
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Template: $templateName ✨",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Just captured these moments! Generated with momo AI assistant ✨ #momoAI #lofi #vibes",
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = audioName,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onPublish,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Publish to Feed", fontWeight = FontWeight.Bold)
        }
    }
}
