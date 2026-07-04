package org.aevora.dharmafeed

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.aevora.dharmafeed.ui.main.MainScreen
import org.aevora.dharmafeed.ui.profile.ProfileScreen
import org.aevora.dharmafeed.ui.detail.PostDetailScreen
import org.aevora.dharmafeed.ui.comments.CommentsScreen
import org.aevora.dharmafeed.ui.welcome.WelcomeScreen
import org.aevora.dharmafeed.ui.messages.ChatDetailScreen
import org.aevora.dharmafeed.ui.messages.FriendshipRecapScreen
import org.aevora.dharmafeed.ui.ai.AIReelAssistantScreen
import org.aevora.dharmafeed.ui.story.StoryScreen
import org.aevora.dharmafeed.FriendshipRecap
import org.aevora.dharmafeed.AIReelAssistant
import org.aevora.dharmafeed.StoryView

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
