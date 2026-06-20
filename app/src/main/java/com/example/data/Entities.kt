package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val coins: Int = 500,
    val themeAccent: String = "GOLD", // "GOLD", "PINK", "CYAN"
    val isVerified: Boolean = true,
    val followerCount: Int = 1840,
    val followingCount: Int = 342
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatRoomId: String, // "AI_CONCIERGE" or other usernames
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromUser: Boolean,
    val isAudio: Boolean = false,
    val audioDuration: String = ""
)

@Entity(tableName = "feed_posts")
data class FeedPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorName: String,
    val creatorAvatar: String,
    val caption: String,
    val imageUrl: String,
    val hashtags: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isVerified: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "stories")
data class Story(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorName: String,
    val creatorAvatar: String,
    val imageUrl: String,
    val timeAgo: String,
    val isViewed: Boolean = false
)

@Entity(tableName = "short_videos")
data class ShortVideo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creatorName: String,
    val creatorAvatar: String,
    val description: String,
    val musicName: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val isLiked: Boolean = false,
    val isVerified: Boolean = true,
    val isLive: Boolean = false,
    val category: String = "TRENDING"
)
