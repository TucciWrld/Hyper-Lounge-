package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        Message::class,
        FeedPost::class,
        Story::class,
        ShortVideo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hyper_lounge_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDb(database.appDao())
                }
            }
        }

        suspend fun populateDb(dao: AppDao) {
            // 1. Initial User Profile (VIP Gold Tier)
            dao.insertUserProfile(
                UserProfile(
                    id = 1,
                    name = "Seraphina Luxe",
                    bio = "Elite Concierge & VIP Lounge Moderator | Redifining Nightlife Elegance ✨",
                    avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                    coins = 1500,
                    themeAccent = "GOLD",
                    isVerified = true,
                    followerCount = 42500,
                    followingCount = 180
                )
            )

            // 2. Prepopulate Stories
            val stories = listOf(
                Story(1, "Amara_Vibe", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400", "2h ago"),
                Story(2, "Marcus_X", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=400", "4h ago"),
                Story(3, "Elena_Sky", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", "https://images.unsplash.com/photo-1481162854517-d9e353af153d?w=400", "8h ago"),
                Story(4, "VIP_Access", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", "https://images.unsplash.com/photo-1574169208507-84376144848b?w=400", "12h ago")
            )
            stories.forEach { dao.insertStory(it) }

            // 3. Prepopulate Feed Posts (Instagram-style)
            val posts = listOf(
                FeedPost(
                    id = 1,
                    creatorName = "Onyx_lounge_la",
                    creatorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
                    caption = "Unveiling the private chamber at Onyx Lounge. High-end single malts, custom cigar lounges, and absolute privacy for elite tier members. DM for private card membership. 🥃✨",
                    imageUrl = "https://images.unsplash.com/photo-1560624052-449f5ddf0c31?auto=format&fit=crop&w=600&q=80",
                    hashtags = "#VIP #LuxuryNightlife #OnyxChamber #PrivateClub",
                    likesCount = 2840,
                    commentsCount = 124,
                    isLiked = true,
                    isVerified = true
                ),
                FeedPost(
                    id = 2,
                    creatorName = "Stella_Dusk",
                    creatorAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150",
                    caption = "Sunset velvet skies from the 45th floor heli-lounge. The customizable neon colors fit my mood perfectly today. Cyber Pink or Gold? What accent flavor do you rock?",
                    imageUrl = "https://images.unsplash.com/photo-1517457373958-b7bdd4587205?auto=format&fit=crop&w=600&q=80",
                    hashtags = "#CyberPink #VIPRoof #SunsetParty #HyperLounge",
                    likesCount = 1435,
                    commentsCount = 89,
                    isLiked = false,
                    isVerified = true
                )
            )
            posts.forEach { dao.insertFeedPost(it) }

            // 4. Prepopulate Short Videos (TikTok-style metadata)
            val videos = listOf(
                ShortVideo(
                    id = 1,
                    creatorName = "dj_violet_flux",
                    creatorAvatar = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150",
                    description = "Live set remix testing in Hyper VIP lounge studio. Dropping this Friday in Tokyo 🎧✨ #synthwave #deephouse #djproducer",
                    musicName = "Violet Flux - Midnight Reverie (Original Mix)",
                    likes = 45900,
                    comments = 1280,
                    shares = 4500,
                    isLiked = true,
                    isVerified = true,
                    category = "TRENDING"
                ),
                ShortVideo(
                    id = 2,
                    creatorName = "VIP_bartender_alex",
                    creatorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
                    description = "Crafting the Liquid Gold cocktail: custom bourbon, single malt extract, gold dust flakes, hand-carved ice sphere. Pure luxury. Drink responsibly. 🥂🥇 #cocktailporn #mixology #goldlabel",
                    musicName = "Alex Mix - Jazz Noir Sunset",
                    likes = 12400,
                    comments = 320,
                    shares = 890,
                    isLiked = false,
                    isVerified = true,
                    category = "COCKTAILS"
                ),
                ShortVideo(
                    id = 3,
                    creatorName = "chloe_escapade",
                    creatorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                    description = "Checking into the Pink Sapphire Lounge! Spot on the live geofence map, pull on your VIP cards. #loungefinder #hotspots #neonmap",
                    musicName = "Escape Beats - Neon Nights",
                    likes = 8520,
                    comments = 142,
                    shares = 241,
                    isLiked = false,
                    isVerified = false,
                    category = "NEARBY"
                )
            )
            videos.forEach { dao.insertVideo(it) }

            // 5. Prepopulate initial welcome messages inside AI concierge
            dao.insertMessage(
                Message(
                    chatRoomId = "AI_CONCIERGE",
                    senderName = "Aurelia AI",
                    text = "Welcome to Hyper Lounge 18+, Sir/Madame. I am Aurelia, your elite digital concierge. I can recommend nearby luxury clubs, brew signature cocktail recipes, explain our Creator Token economy, and assist in customizable styling. How may I elevate your night?",
                    isFromUser = false
                )
            )
            dao.insertMessage(
                Message(
                    chatRoomId = "Lounge_Tokyo_Group",
                    senderName = "System",
                    text = "Welcome to VIP Tokyo Group. Encrypted communication active.",
                    isFromUser = false
                )
            )
            dao.insertMessage(
                Message(
                    chatRoomId = "Lounge_Tokyo_Group",
                    senderName = "Kenji_V8",
                    text = "Anyone at the Roppongi Sky Lounge tonight? The skyline is breathtaking.",
                    isFromUser = false
                )
            )
        }
    }
}
