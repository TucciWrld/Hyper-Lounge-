package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User Profile
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    // Messages
    @Query("SELECT * FROM messages WHERE chatRoomId = :roomId ORDER BY timestamp ASC")
    fun getMessagesForRoomFlow(roomId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("DELETE FROM messages WHERE chatRoomId = :roomId")
    suspend fun clearMessagesForRoom(roomId: String)

    // Feed Posts
    @Query("SELECT * FROM feed_posts ORDER BY timestamp DESC")
    fun getAllFeedPosts(): Flow<List<FeedPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedPost(post: FeedPost)

    @Update
    suspend fun updateFeedPost(post: FeedPost)

    // Stories
    @Query("SELECT * FROM stories ORDER BY id DESC")
    fun getAllStories(): Flow<List<Story>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story)

    // Short Videos
    @Query("SELECT * FROM short_videos ORDER BY id DESC")
    fun getAllVideos(): Flow<List<ShortVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: ShortVideo)

    @Update
    suspend fun updateVideo(video: ShortVideo)
}
