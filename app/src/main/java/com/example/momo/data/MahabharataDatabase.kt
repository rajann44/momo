package com.example.momo.data

data class MahabharataCharacter(
    val id: String,
    val username: String,
    val name: String,
    val avatarColorHex: String, // Dynamic avatars for offline mode
    val bio: String,
    val role: String
)

data class MahabharataComment(
    val characterId: String,
    val text: String,
    val timeAgo: String
)

data class MahabharataPost(
    val id: String,
    val characterId: String,
    val caption: String,
    val artType: String, // MANDALA, YANTRA, CHARIOT_WHEEL, SACRED_GEOMETRY
    val hue: Float, // Hue value for colorful HSL gradients
    val comments: List<MahabharataComment>
)

data class MahabharataStory(
    val id: String,
    val characterId: String,
    val quote: String,
    val isGitaVerse: Boolean = false
)

data class MahabharataMessage(
    val id: String,
    val characterId: String,
    val text: String,
    val isFromMe: Boolean,
    val timeAgo: String
)

data class GitaVerse(
    val verse: String,
    val translation: String,
    val commentary: String
)

data class MahabharataDayContent(
    val dayNumber: Int,
    val title: String,
    val gitaVerse: GitaVerse,
    val posts: List<MahabharataPost>,
    val stories: List<MahabharataStory>,
    val dms: Map<String, List<MahabharataMessage>>
)

object MahabharataDatabase {
    val characters = mapOf(
        "krishna" to MahabharataCharacter(
            id = "krishna",
            username = "lord_krishna",
            name = "Vasudeva Krishna",
            avatarColorHex = "#FFD700", // Divine Gold
            bio = "Guide, Philosopher, and Friend. Charioteer of Arjuna. Chanting the eternal song of Dharma.",
            role = "Divine Guide"
        ),
        "arjuna" to MahabharataCharacter(
            id = "arjuna",
            username = "arjuna_gandiva",
            name = "Arjuna Pandava",
            avatarColorHex = "#4169E1", // Royal Blue
            bio = "Wielder of the Gandiva bow. Seeker of truth. Pandava prince standing at the threshold of destiny.",
            role = "Warrior Prince"
        ),
        "yudhishthira" to MahabharataCharacter(
            id = "yudhishthira",
            username = "dharmaraja",
            name = "Yudhishthira",
            avatarColorHex = "#228B22", // Forest Green
            bio = "Eldest Pandava. Devotee of Dharma and truth. Striving for peace in an age of turmoil.",
            role = "Righteous King"
        ),
        "duryodhana" to MahabharataCharacter(
            id = "duryodhana",
            username = "kuru_emperor",
            name = "Duryodhana",
            avatarColorHex = "#B22222", // Crimson Red
            bio = "Crown Prince of Hastinapur. Determined to claim what is mine. Wielder of the mace.",
            role = "Ambitious King"
        ),
        "karna" to MahabharataCharacter(
            id = "karna",
            username = "suryaputra_karna",
            name = "Karna",
            avatarColorHex = "#FF4500", // Solar Orange
            bio = "Son of Surya, King of Anga. Unrivaled archer, giver of infinite charity, loyal friend of Duryodhana.",
            role = "Tragic Hero"
        ),
        "bhishma" to MahabharataCharacter(
            id = "bhishma",
            username = "pitamaha_bhishma",
            name = "Devavrata Bhishma",
            avatarColorHex = "#8B8B8B", // Steel Gray
            bio = "Grandfather of Kurus. Bound by the oath of lifelong celibacy and eternal loyalty to the throne of Hastinapur.",
            role = "Grand Patriarch"
        ),
        "draupadi" to MahabharataCharacter(
            id = "draupadi",
            username = "queen_draupadi",
            name = "Draupadi Panchali",
            avatarColorHex = "#800080", // Royal Purple
            bio = "Born of fire. Empress of Indraprastha. Demanding justice, truth, and the restoration of Kuru honor.",
            role = "Fire-born Queen"
        ),
        "shakuni" to MahabharataCharacter(
            id = "shakuni",
            username = "gandhara_dice",
            name = "Shakuni",
            avatarColorHex = "#4B0082", // Dark Indigo
            bio = "King of Gandhara. Master of the loaded dice. Restoring Gandhara's honor, one roll at a time.",
            role = "Cunning Uncle"
        )
    )

    fun getCharacter(id: String): MahabharataCharacter {
        return characters[id] ?: characters["krishna"]!!
    }

    val dailyContent = (1..30).map { day ->
        val title = when (day) {
            in 1..5 -> "The Gathering Storm: Dice & Deceit"
            in 6..12 -> "Silent Forest: The 12-Year Exile"
            in 13..18 -> "Gita Upadesha: Battlefield Wisdom"
            in 19..25 -> "Kurukshetra: Clashing Armies"
            else -> "The New Dawn: Path to Ascent"
        }

        val verse = when (day) {
            1 -> GitaVerse(
                verse = "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन।\nमा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि॥",
                translation = "You have a right to perform your prescribed duties, but you are not entitled to the fruits of your actions.",
                commentary = "Focus on the path of action, not the destination. Dharma is performed for its own sake."
            )
            13 -> GitaVerse(
                verse = "यदा यदा हि धर्मस्य ग्लानिर्भवति भारत।\nअभ्युत्थानमधर्मस्य तदात्मानं सृजाम्यहम्॥",
                translation = "Whenever there is a decline in righteousness and a rise in unrighteousness, O Bharata, I manifest Myself.",
                commentary = "Divine order restores balance when human greed and ignorance overflow."
            )
            14 -> GitaVerse(
                verse = "हतो वा प्राप्स्यसि स्वर्गं जित्वा वा भोक्ष्यसे महीम्।\nतस्मादुत्तिष्ठ कौन्तेय युद्धाय कृतनिश्चयः॥",
                translation = "If you are killed, you will reach heaven; if you conquer, you will enjoy the earth. Therefore, stand up, O son of Kunti, resolved to fight.",
                commentary = "Krishna urges Arjuna to shed anxiety. Action in alignment with duty is victorious in all outcomes."
            )
            else -> GitaVerse(
                verse = "नैनं छिन्दन्ति शस्त्राणि नैनं दहति पावकः।\nन चैनं क्लेदयन्त्यापो न शोषयति मारुतः॥",
                translation = "Weapons cannot cut the soul, nor can fire burn it; water cannot wet it, nor can the wind dry it.",
                commentary = "Realize your immortal spirit. The struggles of the material world are passing shadows."
            )
        }

        // Daily posts generator
        val posts = listOf(
            MahabharataPost(
                id = "post_${day}_1",
                characterId = if (day <= 10) "yudhishthira" else if (day <= 20) "arjuna" else "krishna",
                caption = when (day) {
                    in 1..5 -> "Righteousness (Dharma) is not always easy to choose, but it is the only path that guards our peace. ⚖️ #Dharma #Wisdom"
                    in 6..12 -> "The forest is quiet today. A simple leaf, a drop of water, a quiet mind. The exile has taught me more than any golden throne. 🌲🍂 #Exile #InnerPeace"
                    in 13..18 -> "Standing between two armies, I felt my strength fade. But with Govinda by my side, every doubt is resolving into absolute clarity. 🏹☀️ #Kurukshetra #Gita"
                    in 19..25 -> "Let the conch shells blow! We stand for justice, not for land. May truth be our ultimate shield. 🐚🛡️ #Justice #WarForRight"
                    else -> "The crown sits heavy, but the kingdom must heal. Let us sow the seeds of peace, compassion, and unity. 👑🌸 #Healing #Peace"
                },
                artType = if (day % 3 == 0) "MANDALA" else if (day % 3 == 1) "YANTRA" else "CHARIOT_WHEEL",
                hue = (day * 12f) % 360f,
                comments = listOf(
                    MahabharataComment(
                        characterId = if (day <= 10) "duryodhana" else "krishna",
                        text = if (day <= 10) "Dharma without power is useless, brother. Hastinapur belongs to the strong." else "Stand firm in your duties, Arjuna. Focus only on the action.",
                        timeAgo = "10m"
                    ),
                    MahabharataComment(
                        characterId = "draupadi",
                        text = "True peace can only come after complete justice is served.",
                        timeAgo = "5m"
                    )
                )
            ),
            MahabharataPost(
                id = "post_${day}_2",
                characterId = "krishna",
                caption = "The mind is restless and difficult to control, but it can be conquered through practice and detachment. 🧘‍♂️✨ #Mindfulness #KrishnaWisdom",
                artType = "SACRED_GEOMETRY",
                hue = (day * 45f) % 360f,
                comments = listOf(
                    MahabharataComment(characterId = "arjuna", text = "Guide my restless mind, Govinda. I surrender to your words.", "1h"),
                    MahabharataComment(characterId = "karna", text = "Detachment is easy for gods, but mortal bonds pull us down.", "45m")
                )
            ),
            MahabharataPost(
                id = "post_${day}_3",
                characterId = "karna",
                caption = "Fate may decide where we are born, but our character and dedication decide who we become. ☀️🏹 #SelfMade #Karna #Sun",
                artType = "YANTRA",
                hue = 30f, // Solar colors
                comments = listOf(
                    MahabharataComment(characterId = "duryodhana", text = "You are the truest king among us, Karna. Unmatched!", "2h"),
                    MahabharataComment(characterId = "shakuni", text = "Loyalty is a precious coin, use it wisely.", "1h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_4",
                characterId = "duryodhana",
                caption = "Only one will rule Hastinapur. I have built an empire of loyal warriors. Victory is mine! ⚔️🏰 #Hastinapur #Kauravas",
                artType = "CHARIOT_WHEEL",
                hue = 0f, // Red/War colors
                comments = listOf(
                    MahabharataComment(characterId = "yudhishthira", text = "Brothers should build together, Duryodhana, not destroy.", "3h"),
                    MahabharataComment(characterId = "bhishma", text = "My vow binds me to protect the throne, even in conflict.", "2h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_5",
                characterId = "draupadi",
                caption = "Honor is not a commodity to be gambled away. I demand justice from the Kuru elders! 🔥👑 #JusticeForDraupadi #Honor",
                artType = "MANDALA",
                hue = 280f, // Purple/Royal Fire
                comments = listOf(
                    MahabharataComment(characterId = "arjuna", text = "Your tears will not go unanswered, Panchali.", "4h"),
                    MahabharataComment(characterId = "bhishma", text = "Forgive me, daughter. The law is silent when power speaks.", "3h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_6",
                characterId = "bhishma",
                caption = "Bound by duty, locked by vows. Time moves on, but my commitment to Hastinapur remains unshakeable. 🛡️📜 #Pitamaha #Duty",
                artType = "SACRED_GEOMETRY",
                hue = 210f, // Cool Blue/Gray
                comments = listOf(
                    MahabharataComment(characterId = "krishna", text = "Vows are meant to serve life, Grandsire, not bind it to destruction.", "5h"),
                    MahabharataComment(characterId = "arjuna", text = "Your blessing is my greatest strength, Grandsire.", "4h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_7",
                characterId = "shakuni",
                caption = "The dice do not have eyes, but they obey my commands. A simple game can reshape empires. 🎲😈 #Tactics #DiceGame",
                artType = "YANTRA",
                hue = 150f, // Green/Deceit
                comments = listOf(
                    MahabharataComment(characterId = "duryodhana", text = "Uncle, your guidance is always unmatched!", "6h"),
                    MahabharataComment(characterId = "yudhishthira", text = "A game of dice should not divide family, Uncle.", "5h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_8",
                characterId = "yudhishthira",
                caption = "Truth is the ultimate foundation of the universe. Even in darkness, let us speak nothing but the truth. 🌸🕊️ #Truth #Dharmaraja",
                artType = "MANDALA",
                hue = 120f, // Green/Harmony
                comments = listOf(
                    MahabharataComment(characterId = "krishna", text = "True king of Dharma, your steadfastness is the anchor of the world.", "7h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_9",
                characterId = "arjuna",
                caption = "Training has no end. The focus must be absolute. I see only the eye of the bird. 🎯🏹 #Focus #Archery #Gandiva",
                artType = "CHARIOT_WHEEL",
                hue = 180f, // Turquoise
                comments = listOf(
                    MahabharataComment(characterId = "karna", text = "A fine focus, but Anga's arrows will meet yours on the field.", "8h")
                )
            ),
            MahabharataPost(
                id = "post_${day}_10",
                characterId = "krishna",
                caption = "Be a witness to your own life. Do your best, let go of the rest. Everything resides in the supreme consciousness. 🕉️🌅 #SpiritualJourney",
                artType = "MANDALA",
                hue = 50f, // Golden Spiritual
                comments = listOf(
                    MahabharataComment(characterId = "yudhishthira", text = "Grateful for your presence, Madhava.", "9h")
                )
            )
        )

        val stories = listOf(
            MahabharataStory(
                id = "story_${day}_1",
                characterId = "krishna",
                quote = "Focus on your actions. The divine takes care of the cosmic balance.",
                isGitaVerse = true
            ),
            MahabharataStory(
                id = "story_${day}_2",
                characterId = "arjuna",
                quote = "A warrior's true battle is fought inside his own mind."
            ),
            MahabharataStory(
                id = "story_${day}_3",
                characterId = "karna",
                quote = "Loyalty is the highest virtue of a true friend."
            )
        )

        val dms = mapOf(
            "krishna" to listOf(
                MahabharataMessage(
                    id = "msg_${day}_k1",
                    characterId = "arjuna",
                    text = when (day) {
                        in 1..3 -> "How can I fight Bhishma and Drona, who are worthy of my worship, O Madhusudana?"
                        in 4..10 -> "Grandfather Bhishma is slaying thousands. I cannot bear to strike him with my arrows."
                        in 11..15 -> "My heart breaks for Abhimanyu. He entered the Chakravyuha alone and undefended."
                        in 16..18 -> "The final battle with Karna is here, Govinda. My mind must not waver."
                        else -> "The war has ended, but the silence is heavier than the battlefield. What next, Krishna?"
                    },
                    isFromMe = true,
                    timeAgo = "15m"
                ),
                MahabharataMessage(
                    id = "msg_${day}_k2",
                    characterId = "krishna",
                    text = when (day) {
                        in 1..3 -> "The wise grieve neither for the living nor for the dead. Stand up, Arjuna, and perform your duty."
                        in 4..10 -> "To uphold Dharma, you must place your hesitation aside. Use Shikhandi as your shield."
                        in 11..15 -> "Grief is the path to anger, Arjuna. Channel it into your vow to defeat Jayadratha."
                        in 16..18 -> "Karna is a formidable warrior. Focus on your target, and leave the cosmic balance to the divine."
                        else -> "Establish righteousness and heal the land. A new era begins, and your duties are not yet complete."
                    },
                    isFromMe = false,
                    timeAgo = "10m"
                )
            ),
            "arjuna" to listOf(
                MahabharataMessage(
                    id = "msg_${day}_y1",
                    characterId = "yudhishthira",
                    text = when (day) {
                        in 1..3 -> "Grandfather Bhishma, I seek your permission and blessings before we begin this battle."
                        in 4..10 -> "How can we defeat you, Grandsire? Your invincible bow is consuming our entire army."
                        in 11..15 -> "With you on the bed of arrows, grandfather, the battlefield has lost its light. Drona is relentless."
                        in 16..18 -> "Karna is leading the Kaurava forces now. Contemplating this destruction fills me with despair."
                        else -> "Teach me the duties of a righteous king, Grandfather, before you leave this mortal world."
                    },
                    isFromMe = false,
                    timeAgo = "20m"
                ),
                MahabharataMessage(
                    id = "msg_${day}_y2",
                    characterId = "bhishma",
                    text = when (day) {
                        in 1..3 -> "My body is bound to Hastinapur, Yudhisthira, but my blessings are always with Dharma. You shall prevail."
                        in 4..10 -> "I will not raise weapons against a woman or anyone who was once a woman. Bring Shikhandi before me."
                        in 11..15 -> "Drona fights like a storm, but remember that even the greatest mountain must yield to righteousness."
                        in 16..18 -> "Stand firm in your righteousness, Yudhisthira. Victory belongs to those who stand on the path of truth."
                        else -> "Let truth be your crown. Treat your subjects as your own children, and let justice guide every action."
                    },
                    isFromMe = true,
                    timeAgo = "15m"
                )
            ),
            "duryodhana" to listOf(
                MahabharataMessage(
                    id = "msg_${day}_d1",
                    characterId = "duryodhana",
                    text = when (day) {
                        in 1..3 -> "Grandfather is leading, but I wish you were on the battlefield with your bow, Karna."
                        in 4..10 -> "Bhishma is too lenient with the Pandavas. They are not retreating, and our forces are wavering."
                        in 11..15 -> "Drona has created the Chakravyuha. This is our chance to capture Yudhishthira!"
                        in 16..18 -> "You are the commander now, Karna. My hope rests entirely on your arrows today."
                        else -> "Our brothers are gone, Karna. I stand alone by the lake. Was it all in vain?"
                    },
                    isFromMe = false,
                    timeAgo = "12m"
                ),
                MahabharataMessage(
                    id = "msg_${day}_d2",
                    characterId = "karna",
                    text = when (day) {
                        in 1..3 -> "My vow binds me while Bhishma commands, but my loyalty and my heart are always with you, my friend."
                        in 4..10 -> "Do not despair, Duryodhana. Once Bhishma falls, Surya's grace and my arrows will sweep the field."
                        in 11..15 -> "Abhimanyu has entered the formation. He is brave, but he cannot withstand the combined might of our heroes."
                        in 16..18 -> "My chariot wheel may sink, but my spirit remains unbroken. I will face Arjuna and claim victory for you."
                        else -> "True friendship is the only wealth I carried. Stand like a king, Duryodhana, until the very last breath."
                    },
                    isFromMe = true,
                    timeAgo = "8m"
                )
            ),
            "shakuni" to listOf(
                MahabharataMessage(
                    id = "msg_${day}_s1",
                    characterId = "shakuni",
                    text = when (day) {
                        in 1..3 -> "The Pandavas have Krishna, but we have the larger army and strategic positions, my nephew."
                        in 4..10 -> "If Bhishma falls, Karna can join the field. This plays perfectly into our hands."
                        in 11..15 -> "We have slain Abhimanyu. Arjuna has vowed to kill Jayadratha by sunset tomorrow. We must protect him at all costs."
                        in 16..18 -> "The battlefield shrinks. We must use every tactical maneuver we possess."
                        else -> "The game of dice has concluded, Duryodhana. We played our parts to the end."
                    },
                    isFromMe = false,
                    timeAgo = "18m"
                ),
                MahabharataMessage(
                    id = "msg_${day}_s2",
                    characterId = "duryodhana",
                    text = when (day) {
                        in 1..3 -> "Your counsel is my greatest shield, Uncle. We shall not let them reclaim Hastinapur."
                        in 4..10 -> "Indeed. Karna's arrival will change the entire course of this war."
                        in 11..15 -> "Jayadratha will be hidden behind a wall of a hundred thousand soldiers. Arjuna's vow will fail."
                        in 16..18 -> "We must fight with everything we have left. There is no turning back now."
                        else -> "You guided my ambition, Uncle. The crown is gone, but I will die a warrior on the field."
                    },
                    isFromMe = true,
                    timeAgo = "14m"
                )
            )
        )

        MahabharataDayContent(
            dayNumber = day,
            title = title,
            gitaVerse = verse,
            posts = posts,
            stories = stories,
            dms = dms
        )
    }

    fun getContentForDay(day: Int): MahabharataDayContent {
        val loopedDay = ((day - 1) % 30) + 1
        return dailyContent[loopedDay - 1]
    }
}
