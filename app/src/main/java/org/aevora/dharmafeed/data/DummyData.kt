package org.aevora.dharmafeed.data

import android.content.Context
import androidx.navigation3.runtime.NavKey
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

object DummyData {
    var activeDay by mutableStateOf(1)

    var currentUser = User(
        id = "current_user",
        username = "rajan_seeker",
        avatarUrl = "spiritual://AVATAR?color=#FFD700&name=Rajan",
        name = "Rajan Chaudhary",
        bio = "A seeker on a spiritual journey through the Mahabharata epic. 🕉️🧘‍♂️",
        website = "github.com/rajan",
        postsCount = 0,
        followersCount = 108,
        followingCount = 8,
        isOnline = true,
        streakCount = 3
    )

    fun transformCharacter(char: MahabharataCharacter): User {
        val content = MahabharataDatabase.getContentForDay(activeDay)
        val hasStory = content.stories.any { it.characterId == char.id }
        return User(
            id = char.id,
            username = char.username,
            avatarUrl = "spiritual://AVATAR?color=${char.avatarColorHex.replace("#", "%23")}&name=${char.name}",
            name = char.name,
            bio = char.bio,
            website = "mahabharata.org/${char.id}",
            postsCount = 30,
            followersCount = 10000 + Math.abs(char.id.hashCode() % 90000),
            followingCount = 100 + Math.abs(char.id.hashCode() % 900),
            hasActiveStory = hasStory,
            isOnline = true,
            streakCount = 108
        )
    }

    val users: List<User>
        get() = listOf(currentUser) + MahabharataDatabase.characters.values.map { transformCharacter(it) }

    fun getUserById(id: String): User {
        if (id == "current_user") return currentUser
        val char = MahabharataDatabase.characters[id] ?: return currentUser
        return transformCharacter(char)
    }

    fun getLikesCountForPost(postId: String, characterId: String): Int {
        val baseLikes = when (characterId) {
            "krishna" -> 108000
            "arjuna" -> 18780
            "karna" -> 24700
            "yudhishthira" -> 100000
            "bhishma" -> 18000
            "duryodhana" -> 11000
            "draupadi" -> 10080
            "shakuni" -> 6000
            else -> 10800
        }
        val offset = Math.abs(postId.hashCode() % 100)
        return baseLikes + offset
    }

    fun transformPost(p: MahabharataPost, isBookmarked: Boolean = false, isLiked: Boolean = false, timeAgoText: String? = null): Post {
        return Post(
            id = p.id,
            user = getUserById(p.characterId),
            imageUrl = "spiritual://${p.artType}?hue=${p.hue}",
            caption = p.caption,
            likesCount = getLikesCountForPost(p.id, p.characterId),
            commentsCount = p.comments.size,
            timeAgo = timeAgoText ?: "Day $activeDay",
            isLiked = isLiked,
            isBookmarked = isBookmarked || bookmarkedPostIds.contains(p.id)
        )
    }

    val posts: List<Post>
        get() {
            val content = MahabharataDatabase.getContentForDay(activeDay)
            return content.posts.map { p -> transformPost(p) }
        }

    fun getPostById(id: String): Post {
        return posts.find { it.id == id } ?: posts[0]
    }

    fun getCommentsForPost(postId: String): List<Comment> {
        val content = MahabharataDatabase.getContentForDay(activeDay)
        val mPost = content.posts.find { it.id == postId } ?: return emptyList()
        return mPost.comments.mapIndexed { idx, c ->
            Comment(
                id = "${postId}_c_${idx}",
                user = getUserById(c.characterId),
                text = c.text,
                timeAgo = c.timeAgo,
                likesCount = 10 + idx * 5
            )
        }
    }

    val explorePosts: List<Post>
        get() = posts

    val reels: List<Reel>
        get() {
            // Rebrand existing reels to be Mahabharata discourse clips
            return listOf(
                Reel(
                    id = "reel_1",
                    user = getUserById("krishna"),
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    imageUrl = "spiritual://MANDALA?hue=50",
                    caption = "On the nature of Action and Duty: Perform your actions with detachment. #GitaWisdom #Dharma",
                    likesCount = 108000,
                    commentsCount = 2400,
                    audioName = "lord_krishna • Bhagavad Gita Live",
                    isLiked = true
                ),
                Reel(
                    id = "reel_2",
                    user = getUserById("arjuna"),
                    videoUrl = "https://www.w3schools.com/html/movie.mp4",
                    imageUrl = "spiritual://YANTRA?hue=210",
                    caption = "Ready for the battlefield of duty. My focus is only on the goal. #Focus #Gandiva",
                    likesCount = 85000,
                    commentsCount = 1200,
                    audioName = "arjuna_gandiva • Original Audio"
                )
            )
        }

    fun getThreadPartnerId(charId: String): String {
        return when (charId) {
            "krishna" -> "arjuna"
            "arjuna" -> "bhishma"
            "duryodhana" -> "karna"
            "shakuni" -> "duryodhana"
            else -> "current_user"
        }
    }

    val messages: List<Message>
        get() {
            val content = MahabharataDatabase.getContentForDay(activeDay)
            return content.dms.map { (charId, msgList) ->
                val lastMsg = msgList.last()
                val partnerId = getThreadPartnerId(charId)
                val sender = if (lastMsg.isFromMe) getUserById(partnerId) else getUserById(charId)
                Message(
                    id = lastMsg.id,
                    user = getUserById(charId),
                    text = "${sender.name}: ${lastMsg.text}",
                    timeAgo = lastMsg.timeAgo,
                    isUnread = false,
                    isFromMe = lastMsg.isFromMe
                )
            }
        }

    fun getConversationWith(charId: String): List<Message> {
        val content = MahabharataDatabase.getContentForDay(activeDay)
        val msgList = content.dms[charId] ?: emptyList()
        val partnerId = getThreadPartnerId(charId)
        return msgList.map { m ->
            val sender = if (m.isFromMe) getUserById(partnerId) else getUserById(charId)
            Message(
                id = m.id,
                user = sender,
                text = m.text,
                timeAgo = m.timeAgo,
                isUnread = false,
                isFromMe = m.isFromMe
            )
        }
    }

    val moments: List<Moment>
        get() {
            return listOf(
                Moment("mom_1", getUserById("krishna"), "spiritual://MANDALA?hue=50", "3h ago", "The sunset over Kurukshetra 🌅"),
                Moment("mom_2", getUserById("arjuna"), "spiritual://YANTRA?hue=180", "5h ago", "Archery practice before the dawn 🏹")
            )
        }

    fun getStoriesForUser(userId: String): List<StoryItem> {
        val content = MahabharataDatabase.getContentForDay(activeDay)
        val userStory = content.stories.find { it.characterId == userId } ?: return emptyList()
        return listOf(
            StoryItem(
                id = userStory.id,
                imageUrl = "spiritual://MANDALA?hue=${if (userId == "krishna") 50 else 200}",
                timeAgo = "2h",
                isPostShare = false,
                text = userStory.quote
            )
        )
    }

    // Load active day and profile details from SharedPreferences
    fun loadDay(context: Context) {
        val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        activeDay = prefs.getInt("active_day", 1)
        val name = prefs.getString("user_name", "Rajan Chaudhary") ?: "Rajan Chaudhary"
        val username = prefs.getString("user_username", "rajan_seeker") ?: "rajan_seeker"
        val bio = prefs.getString("user_bio", "A seeker on a spiritual journey through the Mahabharata epic. 🕉️🧘‍♂️") ?: "A seeker on a spiritual journey through the Mahabharata epic. 🕉️🧘‍♂️"
        val website = prefs.getString("user_website", "github.com/rajan") ?: "github.com/rajan"
        
        currentUser = currentUser.copy(
            name = name,
            username = username,
            bio = bio,
            website = website
        )
        loadBookmarks(context)
        
        // Load language preference
        val lang = prefs.getString("app_language", "en") ?: "en"
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    val bookmarkedPostIds = mutableStateListOf<String>()

    fun loadBookmarks(context: Context) {
        val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("bookmarked_posts", emptySet()) ?: emptySet()
        bookmarkedPostIds.clear()
        bookmarkedPostIds.addAll(set)
    }

    fun toggleBookmark(context: Context, postId: String) {
        if (bookmarkedPostIds.contains(postId)) {
            bookmarkedPostIds.remove(postId)
        } else {
            bookmarkedPostIds.add(postId)
        }
        val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("bookmarked_posts", bookmarkedPostIds.toSet()).apply()
    }

    // Save active day to SharedPreferences
    fun saveDay(context: Context, day: Int) {
        activeDay = day
        val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("active_day", day).apply()
    }

    // Save user profile details to SharedPreferences
    fun saveUserProfile(context: Context, name: String, username: String, bio: String, website: String) {
        currentUser = currentUser.copy(
            name = name,
            username = username,
            bio = bio,
            website = website
        )
        val prefs = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("user_name", name)
            .putString("user_username", username)
            .putString("user_bio", bio)
            .putString("user_website", website)
            .apply()
    }
}
