package com.example.momo.ui.welcome

import android.content.Context
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.momo.R
import com.example.momo.data.DummyData
import java.util.Locale

fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
    
    // Save language to SharedPrefs
    val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("app_language", languageCode).apply()
}

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var onboardingStep by rememberSaveable { mutableIntStateOf(0) }
    
    // Language state to force Compose recomposition
    val prefs = remember { context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE) }
    var selectedLanguageCode by remember { mutableStateOf(prefs.getString("app_language", "en") ?: "en") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070708)) // Deep pure spiritual black/dark slate
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        
        // Header (Always shows app name)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            val titleGradient = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFD700), // Divine Gold
                    Color(0xFFFF8C00), // Dark Orange
                    Color(0xFFFF4500)  // Saffron/Crimson
                )
            )

            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    brush = titleGradient,
                    fontSize = 54.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-1).sp
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // Subtle indicator dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { stepIndex ->
                    Box(
                        modifier = Modifier
                            .size(if (stepIndex == onboardingStep) 8.dp else 6.dp)
                            .background(
                                color = if (stepIndex == onboardingStep) Color(0xFFFFD700) else Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        // Stateful Content
        when (onboardingStep) {
            0 -> {
                // STEP 0: Language Selection
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Choose Your Language",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "अपनी भाषा चुनें | भाषां चिनुत",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    val languages = listOf(
                        Triple("en", "English", "Seeker's Path"),
                        Triple("hi", "हिन्दी", "धर्म का मार्ग"),
                        Triple("sa", "संस्कृतम्", "देववाणी मार्गः")
                    )

                    languages.forEach { (code, name, subtitle) ->
                        val isSelected = selectedLanguageCode == code
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedLanguageCode = code
                                    setLocale(context, code)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFFFD700).copy(alpha = 0.08f) else Color(0xFF121214)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFFFD700) else Color.White
                                    )
                                    Text(
                                        text = subtitle,
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Next Button
                Button(
                    onClick = { onboardingStep = 1 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),
                        contentColor = Color(0xFF070708)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            1 -> {
                // STEP 1: Introduce Mahabharata Spiritual Journey (Replaced old Welcome Intro)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = stringResource(id = R.string.welcome_subtitle),
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF121214)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.what_to_expect),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700),
                                letterSpacing = 2.sp
                            )

                            InfoRow(
                                icon = Icons.Default.MenuBook,
                                title = stringResource(id = R.string.expect_gita_title),
                                description = stringResource(id = R.string.expect_gita_desc)
                            )

                            InfoRow(
                                icon = Icons.Default.AutoAwesome,
                                title = stringResource(id = R.string.expect_posts_title),
                                description = stringResource(id = R.string.expect_posts_desc)
                            )

                            InfoRow(
                                icon = Icons.Default.Chat,
                                title = stringResource(id = R.string.expect_chats_title),
                                description = stringResource(id = R.string.expect_chats_desc)
                            )

                            InfoRow(
                                icon = Icons.Default.Bookmark,
                                title = stringResource(id = R.string.expect_saved_title),
                                description = stringResource(id = R.string.expect_saved_desc)
                            )
                        }
                    }
                }

                // Next Button
                Button(
                    onClick = { onboardingStep = 2 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),
                        contentColor = Color(0xFF070708)
                    ),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            2 -> {
                // STEP 2: Profile Customization
                var name by remember { mutableStateOf(DummyData.currentUser.name) }
                var username by remember { mutableStateOf(DummyData.currentUser.username) }
                var bio by remember { mutableStateOf(DummyData.currentUser.bio) }
                var website by remember { mutableStateOf(DummyData.currentUser.website) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Setup Seeker Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Customize your identity for this spiritual quest.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val textFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFD700),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedLabelColor = Color(0xFFFFD700),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Spiritual Bio") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = website,
                        onValueChange = { website = it },
                        label = { Text("Website") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Finish / Begin Journey Button
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            DummyData.saveUserProfile(context, name, username, bio, website)
                            onGetStartedClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            contentColor = Color(0xFF070708)
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_begin_journey),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(id = R.string.welcome_footer),
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.White.copy(alpha = 0.35f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFFFD700).copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                lineHeight = 16.sp
            )
        }
    }
}
