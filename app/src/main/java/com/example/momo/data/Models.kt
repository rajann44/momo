package com.example.momo.data

data class User(
    val id: String,
    val username: String,
    val avatarUrl: String,
    val name: String,
    val bio: String = "",
    val website: String = "",
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val hasActiveStory: Boolean = false,
    val isOnline: Boolean = false,
    val streakCount: Int = 0
)

data class Post(
    val id: String,
    val user: User,
    val imageUrl: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val timeAgo: String,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

data class Comment(
    val id: String,
    val user: User,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Reel(
    val id: String,
    val user: User,
    val videoUrl: String, // Mocked or static
    val imageUrl: String, // Cover image
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val audioName: String,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

data class Message(
    val id: String,
    val user: User,
    val text: String,
    val timeAgo: String,
    val isUnread: Boolean = false,
    val isFromMe: Boolean = false
)

data class Moment(
    val id: String,
    val user: User,
    val imageUrl: String,
    val timeAgo: String,
    val caption: String
)

data class StoryItem(
    val id: String,
    val imageUrl: String,
    val timeAgo: String,
    val isPostShare: Boolean = false,
    val sharedPostUser: User? = null,
    val sharedPostImg: String = ""
)
