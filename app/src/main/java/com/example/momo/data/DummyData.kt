package com.example.momo.data

object DummyData {
    val currentUser = User(
        id = "current_user",
        username = "rajan_momo",
        avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&h=200&fit=crop",
        name = "Rajan Chaudhary",
        bio = "Building mobile applications with Jetpack Compose 🚀\nExploring the intersection of AI and UX design.",
        website = "github.com/rajan",
        postsCount = 9,
        followersCount = 1420,
        followingCount = 482,
        isOnline = true,
        streakCount = 0
    )

    val users = listOf(
        currentUser,
        User(
            id = "user_1",
            username = "tech_insider",
            avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200&h=200&fit=crop",
            name = "Alex Rivera",
            bio = "Tech reviews, setups, and future concepts.",
            website = "youtube.com/techinsider",
            postsCount = 142,
            followersCount = 85200,
            followingCount = 120,
            hasActiveStory = true,
            isOnline = true,
            streakCount = 12
        ),
        User(
            id = "user_2",
            username = "art_and_soul",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop",
            name = "Maya Lin",
            bio = "Oil painter, ceramicist, and dog lover. Studio visits open!",
            website = "mayalinart.com",
            postsCount = 88,
            followersCount = 12400,
            followingCount = 590,
            hasActiveStory = true,
            isOnline = false,
            streakCount = 0
        ),
        User(
            id = "user_3",
            username = "globetrotter",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop",
            name = "David K.",
            bio = "50+ countries and counting. Traveling light, living large.",
            website = "davidglobetrotter.org",
            postsCount = 312,
            followersCount = 45900,
            followingCount = 830,
            hasActiveStory = true,
            isOnline = true,
            streakCount = 5
        ),
        User(
            id = "user_4",
            username = "nature_lens",
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop",
            name = "Sarah Green",
            bio = "Landscape and wildlife photography.",
            website = "sarahgreenphoto.com",
            postsCount = 74,
            followersCount = 6320,
            followingCount = 320,
            hasActiveStory = false,
            isOnline = false,
            streakCount = 0
        ),
        User(
            id = "user_5",
            username = "fitness_guru",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop",
            name = "Marcus Bolt",
            bio = "Personal trainer & nutritionist. Let's hit that new PR!",
            website = "boltfit.com",
            postsCount = 205,
            followersCount = 98100,
            followingCount = 401,
            hasActiveStory = true,
            isOnline = true,
            streakCount = 8
        )
    )

    fun getUserById(id: String): User {
        return users.find { it.id == id } ?: currentUser
    }

    val posts = listOf(
        Post(
            id = "post_1",
            user = getUserById("user_1"),
            imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=800",
            caption = "My workspace setup for 2026. Clean desk, clean mind. Thoughts on the dual monitors? 💻🤖 #setup #developer #desksetup",
            likesCount = 1842,
            commentsCount = 47,
            timeAgo = "2 hours ago",
            isLiked = true
        ),
        Post(
            id = "post_2",
            user = getUserById("user_3"),
            imageUrl = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=800",
            caption = "Driving through the red rocks of Arizona. This road trip is everything I needed! 🌵🚗☀️ #travel #roadtrip #arizona #wanderlust",
            likesCount = 3241,
            commentsCount = 89,
            timeAgo = "5 hours ago",
            isBookmarked = true
        ),
        Post(
            id = "post_3",
            user = getUserById("user_2"),
            imageUrl = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?w=800",
            caption = "Finished this commission today! Acrylic on canvas. Grateful for everyone supporting my journey. 🎨✨ #art #painting #canvasart",
            likesCount = 948,
            commentsCount = 23,
            timeAgo = "1 day ago"
        ),
        Post(
            id = "post_4",
            user = getUserById("user_4"),
            imageUrl = "https://images.unsplash.com/photo-1472214222541-d510753a4707?w=800",
            caption = "A quiet morning in the valley. Golden hour hits different when there's dew on the grass. 🌅🌲 #nature #photography #landscape",
            likesCount = 532,
            commentsCount = 14,
            timeAgo = "2 days ago"
        ),
        Post(
            id = "post_5",
            user = getUserById("user_5"),
            imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=800",
            caption = "Consistency is the only secret. No shortcuts, just show up every day. Tag your gym partner! 💪🔥 #fitness #motivation #workout",
            likesCount = 2891,
            commentsCount = 104,
            timeAgo = "3 days ago"
        )
    )

    fun getPostById(id: String): Post {
        return posts.find { it.id == id } ?: posts[0]
    }

    val comments = mapOf(
        "post_1" to listOf(
            Comment("c1_1", getUserById("user_2"), "Beautiful setup! Where did you get that desk mat?", "2h", 12),
            Comment("c1_2", getUserById("user_5"), "Dual monitors are a must for productivity!", "1h", 4),
            Comment("c1_3", getUserById("user_4"), "Clean. Minimal. I love it.", "30m", 1)
        ),
        "post_2" to listOf(
            Comment("c2_1", getUserById("user_1"), "Arizona is stunning. Have you visited the Grand Canyon yet?", "4h", 32),
            Comment("c2_2", getUserById("user_4"), "This landscape shot is incredible! Safe travels David!", "3h", 15),
            Comment("c2_3", getUserById("user_2"), "The lighting here is just magical.", "2h", 8)
        )
    )

    fun getCommentsForPost(postId: String): List<Comment> {
        return comments[postId] ?: listOf(
            Comment("c_def_1", getUserById("user_1"), "Wow, amazing post!", "5h", 2),
            Comment("c_def_2", getUserById("user_2"), "Super cool!", "4h", 0)
        )
    }

    val explorePosts = posts + listOf(
        Post("exp_1", getUserById("user_1"), "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d?w=600", "Breathe in the wild", 400, 10, "1w"),
        Post("exp_2", getUserById("user_2"), "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600", "Nature's mist", 521, 14, "1w"),
        Post("exp_3", getUserById("user_3"), "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=600", "Mountain peak views", 1202, 53, "6d"),
        Post("exp_4", getUserById("user_4"), "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=600", "Forest rays", 892, 28, "5d"),
        Post("exp_5", getUserById("user_5"), "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600", "Valley curves", 2392, 112, "4d"),
        Post("exp_6", getUserById("user_1"), "https://images.unsplash.com/photo-1528459801416-a9e53bbf4e17?w=600", "Abstract shapes", 305, 5, "3d")
    )

    val reels = listOf(
        Reel(
            id = "reel_1",
            user = getUserById("user_1"),
            videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
            imageUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
            caption = "Unboxing the newest tech gadgets for developers in 2026! Stay tuned for the full video review. #tech #unboxing #gadget",
            likesCount = 42100,
            commentsCount = 182,
            audioName = "tech_insider • Original Audio",
            isLiked = true
        ),
        Reel(
            id = "reel_2",
            user = getUserById("user_3"),
            videoUrl = "https://www.w3schools.com/html/movie.mp4",
            imageUrl = "https://images.unsplash.com/photo-1506157786151-b8491531f063?w=800",
            caption = "Last night's music festival in Berlin. The energy was absolutely electric! 🇩🇪⚡️ #berlin #concert #edm #festival",
            likesCount = 89300,
            commentsCount = 943,
            audioName = "Festival Sounds • Live Edit"
        ),
        Reel(
            id = "reel_3",
            user = getUserById("user_2"),
            videoUrl = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
            imageUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=800",
            caption = "Quick time-lapse of my painting process. 6 hours of work compressed into 15 seconds! 🎨🖌️ #arttimelapse #painting #creative",
            likesCount = 15200,
            commentsCount = 94,
            audioName = "Lofi Dreams • Chill Vibes",
            isBookmarked = true
        )
    )

    val messages = listOf(
        Message("m1", getUserById("user_1"), "Hey! Did you see the new Compose compiler update?", "3:24 PM", isUnread = true),
        Message("m2", getUserById("user_2"), "The painting is ready for pickup whenever you are free!", "2:15 PM"),
        Message("m3", getUserById("user_3"), "I just landed in Tokyo! Let's catch up tomorrow.", "Yesterday"),
        Message("m4", getUserById("user_5"), "Are you hitting the gym tonight? Let's do some leg workouts.", "Friday"),
        Message("m5", getUserById("user_4"), "Sent you the raw landscape images for feedback.", "June 28")
    )

    val moments = listOf(
        Moment("mom_1", getUserById("user_1"), "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600", "3h ago", "My workspace puppy 🐶"),
        Moment("mom_2", getUserById("user_3"), "https://images.unsplash.com/photo-1501504905252-473c47e087f8?w=600", "4h ago", "Enjoying the sunset coffee ☕️🌅"),
        Moment("mom_3", getUserById("user_5"), "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=600", "5h ago", "Leg day done. PR hit! 💪")
    )

    private val userStories = mapOf(
        "user_1" to listOf(
            StoryItem(
                id = "story_u1_1",
                imageUrl = "https://images.unsplash.com/photo-1547082299-de196ea013d6?w=800",
                timeAgo = "3h"
            ),
            StoryItem(
                id = "story_u1_2",
                imageUrl = "",
                timeAgo = "20h",
                isPostShare = true,
                sharedPostUser = User(
                    id = "user_2_sub",
                    username = "vastu.shaz",
                    avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
                    name = "Maya Lin"
                ),
                sharedPostImg = "https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?w=600"
            )
        ),
        "user_2" to listOf(
            StoryItem(
                id = "story_u2_1",
                imageUrl = "",
                timeAgo = "12h",
                isPostShare = true,
                sharedPostUser = User(
                    id = "user_3_sub",
                    username = "globetrotter",
                    avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
                    name = "David K."
                ),
                sharedPostImg = "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=600"
            )
        ),
        "user_3" to listOf(
            StoryItem(
                id = "story_u3_1",
                imageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800",
                timeAgo = "5h"
            ),
            StoryItem(
                id = "story_u3_2",
                imageUrl = "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=800",
                timeAgo = "1h"
            )
        ),
        "user_5" to listOf(
            StoryItem(
                id = "story_u5_1",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=800",
                timeAgo = "8h"
            )
        )
    )

    fun getStoriesForUser(userId: String): List<StoryItem> {
        return userStories[userId] ?: emptyList()
    }
}
