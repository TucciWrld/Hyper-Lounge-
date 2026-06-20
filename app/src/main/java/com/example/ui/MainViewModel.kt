package com.example.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = AppRepository(db.appDao())

    // --- Active App Navigation Tab ---
    val currentTab = MutableStateFlow("SHORTS") // "SHORTS", "PHOTOS", "CHAT", "MAP", "PROFILE", "ADMIN"

    // --- Customizable Color Accent ---
    // User can customize color theme globally (GOLD, PINK, CYAN)
    val themeAccent = MutableStateFlow("GOLD")

    // --- Search & Filters ---
    val searchQuery = MutableStateFlow("")
    val mapFilterTag = MutableStateFlow("ALL") // "ALL", "CLUB", "VIP", "ROOFTOP", "LOUNGE"

    // --- Active Chat Room ---
    val activeChatRoom = MutableStateFlow("AI_CONCIERGE") // "AI_CONCIERGE" or "Lounge_Tokyo_Group"

    // --- Call Simulator State ---
    val callState = MutableStateFlow("IDLE") // "IDLE", "RINGING", "CONNECTED", "ENDED"
    val isVideoCall = MutableStateFlow(true)
    val callParticipantName = MutableStateFlow("Aurelia AI")
    val callParticipantAvatar = MutableStateFlow("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
    val callDurationSeconds = MutableStateFlow(0)

    // --- Selected Item Overlays ---
    val selectedStory = MutableStateFlow<Story?>(null)
    val activeLoungeDetail = MutableStateFlow<LoungeHotspot?>(null)

    // --- Room DB Exponents flows ---
    val userProfile: StateFlow<UserProfile?> = repository.userProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val feedPosts: StateFlow<List<FeedPost>> = repository.allFeedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stories: StateFlow<List<Story>> = repository.allStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val videos: StateFlow<List<ShortVideo>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Message stream for active chat room
    val activeChatMessages: StateFlow<List<Message>> = activeChatRoom
        .flatMapLatest { roomId -> repository.getMessagesForRoom(roomId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observe theme customizer from userProfile when loaded
        viewModelScope.launch {
            userProfile.collect { profile ->
                profile?.let {
                    themeAccent.value = it.themeAccent
                }
            }
        }
    }

    // --- Accent Customizer Handler ---
    fun updateThemeAccent(newAccent: String) {
        themeAccent.value = newAccent
        viewModelScope.launch(Dispatchers.IO) {
            val current = repository.getUserProfile() ?: return@launch
            repository.updateUserProfile(current.copy(themeAccent = newAccent))
        }
    }

    // --- Profile & Coins Actions ---
    fun purchaseCoins(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = repository.getUserProfile() ?: return@launch
            repository.updateUserProfile(current.copy(coins = current.coins + amount))
        }
    }

    // --- Video Likes & Gifts ---
    fun toggleLikeVideo(videoId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = videos.value
            val match = videoList.find { it.id == videoId } ?: return@launch
            val wasLiked = match.isLiked
            val mult = if (wasLiked) -1 else 1
            repository.updateVideo(
                match.copy(
                    isLiked = !wasLiked,
                    likes = match.likes + mult
                )
            )
        }
    }

    fun giftVideoCreator(videoId: Long, costCoins: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = repository.getUserProfile()
            if (currentProfile == null || currentProfile.coins < costCoins) return@launch

            // Subtract user coins
            repository.updateUserProfile(currentProfile.copy(coins = currentProfile.coins - costCoins))

            // Increase likes and trigger simulation alert
            val videoList = videos.value
            val match = videoList.find { it.id == videoId } ?: return@launch
            repository.updateVideo(
                match.copy(
                    likes = match.likes + (costCoins * 2),
                    shares = match.shares + 1
                )
            )
        }
    }

    // --- Feed Post Actions (Instagram-style) ---
    fun toggleLikeFeedPost(postId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = feedPosts.value
            val match = posts.find { it.id == postId } ?: return@launch
            val wasLiked = match.isLiked
            val mult = if (wasLiked) -1 else 1
            repository.updateFeedPost(
                match.copy(
                    isLiked = !wasLiked,
                    likesCount = match.likesCount + mult
                )
            )
        }
    }

    fun toggleSaveFeedPost(postId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val posts = feedPosts.value
            val match = posts.find { it.id == postId } ?: return@launch
            repository.updateFeedPost(match.copy(isSaved = !match.isSaved))
        }
    }

    fun submitNewPost(caption: String, imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getUserProfile()
            val newPost = FeedPost(
                creatorName = profile?.name ?: "Anonymous VIP",
                creatorAvatar = profile?.avatarUrl ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                caption = caption,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=400" },
                hashtags = "#VIPLife #HyperNewbie #Exclusive",
                likesCount = 0,
                commentsCount = 0,
                isLiked = false,
                isSaved = false,
                isVerified = profile?.isVerified ?: true
            )
            repository.insertFeedPost(newPost)
        }
    }

    fun submitNewStory(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getUserProfile()
            val newStory = Story(
                creatorName = profile?.name ?: "VIP Guest",
                creatorAvatar = profile?.avatarUrl ?: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150",
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=400" },
                timeAgo = "1m ago",
                isViewed = false
            )
            repository.insertStory(newStory)
        }
    }

    // --- Message Sending with Gemini AI ---
    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val roomId = activeChatRoom.value
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getUserProfile()
            val userMsg = Message(
                chatRoomId = roomId,
                senderName = profile?.name ?: "Me",
                text = text,
                isFromUser = true
            )
            repository.insertMessage(userMsg)

            if (roomId == "AI_CONCIERGE") {
                // Fetch context messages to let AI answer with high accuracy
                val history = activeChatMessages.value + userMsg
                // Query Gemini
                launch(Dispatchers.IO) {
                    val apiKey = BuildConfig.GEMINI_API_KEY
                    val aiReply = GeminiClient.getAiResponse(apiKey, history)
                    repository.insertMessage(
                        Message(
                            chatRoomId = "AI_CONCIERGE",
                            senderName = "Aurelia AI",
                            text = aiReply,
                            isFromUser = false
                        )
                    )
                }
            } else {
                // Group chat simulated automatic user reply after 1 sec
                delay(1500)
                val replies = listOf(
                    "Agreed! See you at the main floor in 10 minutes.",
                    "Save me a bottle of the vintage blend! 🍾",
                    "The neon glow is insane tonight. Tapping my map route now.",
                    "Wow! Loving your vibe Seraphina!",
                    "Is anyone ordering cocktails with gold-dust? Highly recommended!"
                )
                repository.insertMessage(
                    Message(
                        chatRoomId = roomId,
                        senderName = listOf("Kenji_V8", "Amara_Vibe", "Marcus_X", "Elena_Sky").random(),
                        text = replies.random(),
                        isFromUser = false
                    )
                )
            }
        }
    }

    fun clearMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearMessages(activeChatRoom.value)
        }
    }

    // --- P2P WebRTC / VoIP Voice & Videocalls Simulator ---
    fun triggerSimulatedCall(name: String, avatar: String, video: Boolean = true) {
        callParticipantName.value = name
        callParticipantAvatar.value = avatar
        isVideoCall.value = video
        callState.value = "RINGING"
        callDurationSeconds.value = 0
    }

    fun answerCall() {
        callState.value = "CONNECTED"
        viewModelScope.launch {
            while (callState.value == "CONNECTED") {
                delay(1000)
                callDurationSeconds.value += 1
            }
        }
    }

    fun endCall() {
        callState.value = "ENDED"
        viewModelScope.launch {
            delay(1500)
            callState.value = "IDLE"
        }
    }

    // --- Google Maps Nearby hotspots list ---
    // Perfect custom lounge coordinate points
    val loungeHotspots = listOf(
        LoungeHotspot("Onyx Chamber", "35.6634", "139.7310", "CLUB", "Luxury black counters, dark leather couches, cigar lounge.", "⭐️ 4.9 (240 members)", "https://images.unsplash.com/photo-1560624052-449f5ddf0c31?w=300"),
        LoungeHotspot("Sapphire Sky Garden", "35.6610", "139.7350", "ROOFTOP", "45th Floor helipad, glass pool, neon pink theme.", "⭐️ 4.8 (1.2k guests)", "https://images.unsplash.com/photo-1517457373958-b7bdd4587205?w=300"),
        LoungeHotspot("Amber Private Deck", "35.6655", "139.7280", "VIP", "Whiskey bar rooms, fingerprints lock, exclusive.", "⭐️ 5.0 (80 VIPs)", "https://images.unsplash.com/photo-1543007630-9710e4a00a20?w=300"),
        LoungeHotspot("Emerald Rose Salon", "35.6580", "139.7302", "LOUNGE", "Sensory soundscapes, chill DJs, organic cocktail lines.", "⭐️ 4.7 (430 visitors)", "https://images.unsplash.com/photo-1574096079513-d8259312b7a3?w=300")
    )

    // Admin Analytics
    val adminFlags = MutableStateFlow(listOf(
        AdminFlag(1, "Post #2: Stella_Dusk - Out of bounds coords", "RESOLVED", "System"),
        AdminFlag(2, "Story #3: Elena_Sky - Flagged for excessive neon glare", "PENDING", "Safety AI"),
        AdminFlag(3, "Room chat: Kenji_V8 - Fast message repetition", "WARNING SENT", "Moderator Seraphina")
    ))

    fun resolveFlag(flagId: Int) {
        adminFlags.value = adminFlags.value.map {
            if (it.id == flagId) it.copy(status = "RESOLVED") else it
        }
    }
}

// Map Hotspot Structure
data class LoungeHotspot(
    val name: String,
    val lat: String,
    val lng: String,
    val tag: String,
    val description: String,
    val ratingInfo: String,
    val coverUrl: String
)

// Admin Flags Structure
data class AdminFlag(
    val id: Int,
    val details: String,
    val status: String,
    val reporter: String
)
