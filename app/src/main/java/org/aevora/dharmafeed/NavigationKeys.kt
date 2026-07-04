package org.aevora.dharmafeed

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Welcome : NavKey
@Serializable data object Main : NavKey
@Serializable data class UserProfile(val userId: String) : NavKey
@Serializable data class ChatDetail(val userId: String) : NavKey
@Serializable data class FriendshipRecap(val userId: String) : NavKey
@Serializable data object AIReelAssistant : NavKey
@Serializable data class PostDetail(val postId: String) : NavKey
@Serializable data class Comments(val postId: String) : NavKey
@Serializable data class StoryView(val userId: String) : NavKey
