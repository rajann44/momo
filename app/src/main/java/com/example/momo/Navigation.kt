package com.example.momo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.momo.ui.main.MainScreen
import com.example.momo.ui.profile.ProfileScreen
import com.example.momo.ui.detail.PostDetailScreen
import com.example.momo.ui.comments.CommentsScreen
import com.example.momo.ui.welcome.WelcomeScreen
import com.example.momo.ui.messages.ChatDetailScreen
import com.example.momo.ui.messages.FriendshipRecapScreen
import com.example.momo.ui.ai.AIReelAssistantScreen
import com.example.momo.ui.story.StoryScreen
import com.example.momo.FriendshipRecap
import com.example.momo.AIReelAssistant
import com.example.momo.StoryView

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Welcome)

  NavDisplay(
    backStack = backStack,
    onBack = {
      if (backStack.size > 1) {
        backStack.removeLastOrNull()
      }
    },
    entryProvider =
      entryProvider {
        entry<Welcome> {
          WelcomeScreen(
            onGetStartedClick = {
              backStack.removeLastOrNull()
              backStack.add(Main)
            }
          )
        }
        entry<Main> {
          MainScreen(onItemClick = { navKey -> backStack.add(navKey) })
        }
        entry<UserProfile> { key ->
          ProfileScreen(
            userId = key.userId,
            onBackClick = { backStack.removeLastOrNull() },
            onItemClick = { navKey -> backStack.add(navKey) }
          )
        }
        entry<ChatDetail> { key ->
          ChatDetailScreen(
            userId = key.userId,
            onBackClick = { backStack.removeLastOrNull() },
            onItemClick = { navKey -> backStack.add(navKey) }
          )
        }
        entry<FriendshipRecap> { key ->
          FriendshipRecapScreen(
            userId = key.userId,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<AIReelAssistant> {
          AIReelAssistantScreen(
            onBackClick = { backStack.removeLastOrNull() },
            onPublishSuccess = { backStack.removeLastOrNull() }
          )
        }
        entry<PostDetail> { key ->
          PostDetailScreen(
            postId = key.postId,
            onBackClick = { backStack.removeLastOrNull() },
            onItemClick = { navKey -> backStack.add(navKey) }
          )
        }
        entry<Comments> { key ->
          CommentsScreen(
            postId = key.postId,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
        entry<StoryView> { key ->
          StoryScreen(
            userId = key.userId,
            onBackClick = { backStack.removeLastOrNull() }
          )
        }
      },
  )
}
