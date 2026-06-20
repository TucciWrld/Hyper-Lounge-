package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    // User profile
    val userProfileFlow: Flow<UserProfile?> = appDao.getUserProfileFlow()
    suspend fun getUserProfile(): UserProfile? = appDao.getUserProfile()
    suspend fun insertUserProfile(profile: UserProfile) = appDao.insertUserProfile(profile)
    suspend fun updateUserProfile(profile: UserProfile) = appDao.updateUserProfile(profile)

    // Messages
    fun getMessagesForRoom(roomId: String): Flow<List<Message>> = appDao.getMessagesForRoomFlow(roomId)
    suspend fun insertMessage(message: Message) = appDao.insertMessage(message)
    suspend fun clearMessages(roomId: String) = appDao.clearMessagesForRoom(roomId)

    // Feed Posts
    val allFeedPosts: Flow<List<FeedPost>> = appDao.getAllFeedPosts()
    suspend fun insertFeedPost(post: FeedPost) = daoInsertFeedPost(post)
    suspend fun updateFeedPost(post: FeedPost) = appDao.updateFeedPost(post)

    private suspend fun daoInsertFeedPost(post: FeedPost) = appDao.insertFeedPost(post)

    // Stories
    val allStories: Flow<List<Story>> = appDao.getAllStories()
    suspend fun insertStory(story: Story) = appDao.insertStory(story)

    // Short Videos
    val allVideos: Flow<List<ShortVideo>> = appDao.getAllVideos()
    suspend fun insertVideo(video: ShortVideo) = appDao.insertVideo(video)
    suspend fun updateVideo(video: ShortVideo) = appDao.updateVideo(video)
}
