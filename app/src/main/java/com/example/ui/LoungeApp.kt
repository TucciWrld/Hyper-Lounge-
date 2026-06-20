package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.FeedPost
import com.example.data.Message
import com.example.data.ShortVideo
import com.example.data.Story
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoungeApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Key states synced from ViewModel
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeAccentName by viewModel.themeAccent.collectAsStateWithLifecycle()
    val activeProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Accent Color reference
    val primaryAccentColor = when (activeAccentName) {
        "PINK" -> Color(0xFFFF2B85)
        "CYAN" -> Color(0xFF00E5FF)
        else -> Color(0xFFE5C060) // GOLD
    }

    // Interactive age-gate splash screen
    var isAgeVerified by remember { mutableStateOf(false) }
    var showBiometricScan by remember { mutableStateOf(false) }

    // Story viewer Overlay
    val activeStory by viewModel.selectedStory.collectAsStateWithLifecycle()

    // Call Simulator Overlay
    val callState by viewModel.callState.collectAsStateWithLifecycle()
    val isVideoCall by viewModel.isVideoCall.collectAsStateWithLifecycle()
    val callParticipant by viewModel.callParticipantName.collectAsStateWithLifecycle()
    val callParticipantAvatar by viewModel.callParticipantAvatar.collectAsStateWithLifecycle()
    val callDurationSec by viewModel.callDurationSeconds.collectAsStateWithLifecycle()

    // Notification banner simulator state
    var notificationMessage by remember { mutableStateOf<String?>(null) }

    // Helper to trigger simulated banner
    fun triggerNotificationBanner(text: String) {
        coroutineScope.launch {
            notificationMessage = text
            delay(3500)
            notificationMessage = null
        }
    }

    // Splash Entrance Screen Gate (Age verification gate)
    if (!isAgeVerified) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF060609)),
            contentAlignment = Alignment.Center
        ) {
            // Background mesh or decorative circles
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = primaryAccentColor.copy(alpha = 0.12f),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.5f, size.height * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFFF2B85).copy(alpha = 0.08f),
                    radius = size.width * 0.4f,
                    center = Offset(size.width * 0.1f, size.height * 0.8f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gold / Pink Logo asset or placeholder icon
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Color(0xFF13131D), CircleShape)
                        .border(1.5.dp, primaryAccentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalBar,
                        contentDescription = "Club Logo",
                        tint = primaryAccentColor,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "HYPER LOUNGE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 4.sp,
                    color = Color.White
                )

                Text(
                    text = "18+ EXCLUSIVE CLUB",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 6.sp,
                    color = primaryAccentColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "A modern, premium adult digital lounge combining social streams, secure channels, dynamic AI matchmaking, and localized nightlife locators.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Luxury Glassmorphic Verification Gate Cards
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF12121A).copy(alpha = 0.82f), RoundedCornerShape(20.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.NoAdultContent,
                            contentDescription = "Adult Icon",
                            tint = Color(0xFFFF2B85),
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Identity & Age Verification Required",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "To access secure live video features and direct adult chatting, please verify your age status.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        if (!showBiometricScan) {
                            Button(
                                onClick = { showBiometricScan = true },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryAccentColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_age_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Enter Lounge (Confirm 18+)",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            // Biometrics simulate scanner
                            var progress by remember { mutableStateOf(0f) }
                            var isComplete by remember { mutableStateOf(false) }

                            LaunchedEffect(Unit) {
                                while (progress < 1.0f) {
                                    delay(40)
                                    progress += 0.05f
                                }
                                progress = 1.0f
                                isComplete = true
                                delay(600)
                                isAgeVerified = true
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = "Scanner",
                                    tint = if (isComplete) Color.Green else primaryAccentColor,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .alpha(if (progress % 0.4f > 0.2f) 0.5f else 1f)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (isComplete) "Access Granted!" else "Scanning Face ID & Fingerprint...",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isComplete) Color.Green else primaryAccentColor
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LinearProgressIndicator(
                                    progress = progress,
                                    color = primaryAccentColor,
                                    trackColor = Color.White.copy(alpha = 0.1f),
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(4.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Secure end-to-end encryption active • Verified Lounge Members",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    } else {
        // Main Application Scaffold Layout
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0A0E))
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.08f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(0.dp)
                        )
                        .padding(top = 40.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "HYPER LOUNGE",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "18+",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .background(primaryAccentColor, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Top right state actions: Coins counter & notifications bell
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Coin economy display
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF161624), RoundedCornerShape(12.dp))
                                    .border(
                                        1.dp,
                                        primaryAccentColor.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        viewModel.currentTab.value = "PROFILE"
                                        triggerNotificationBanner("Opened Wallet. Select a pack to increase Coins!")
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = "Coins",
                                        tint = AccentGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${activeProfile?.coins ?: 0}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Buy",
                                        tint = primaryAccentColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            IconButton(
                                onClick = {
                                    triggerNotificationBanner("No new moderation alerts in your VIP region.")
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF161624), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notif",
                                    tint = primaryAccentColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                // Customized Premium Floating Navigation Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .background(
                            Color(0xFF0F0F16).copy(alpha = 0.88f),
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = primaryAccentColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val items = listOf(
                            Triple("SHORTS", Icons.Default.Audiotrack, "Videos"),
                            Triple("PHOTOS", Icons.Default.PhotoLibrary, "Explore"),
                            Triple("CHAT", Icons.Default.Chat, "Chat"),
                            Triple("MAP", Icons.Default.Map, "Finder"),
                            Triple("PROFILE", Icons.Default.AccountCircle, "Profile"),
                            Triple("ADMIN", Icons.Default.AdminPanelSettings, "Admin")
                        )

                        items.forEach { (tabId, icon, label) ->
                            val isSelected = currentTab == tabId
                            val accentTarget = if (isSelected) primaryAccentColor else Color.Gray

                            Column(
                                modifier = Modifier
                                    .clickable(
                                        onClick = { viewModel.currentTab.value = tabId },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    )
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = accentTarget,
                                    modifier = Modifier
                                        .size(if (isSelected) 24.dp else 22.dp)
                                        .testTag("nav_btn_${tabId.lowercase()}")
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = accentTarget,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            },
            containerColor = Color(0xFF07070B)
        ) { innerPadding ->

            // Main Contents Area container Box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Multi-screen Router
                when (currentTab) {
                    "SHORTS" -> ShortsFeedScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                    "PHOTOS" -> PhotoFeedScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                    "CHAT" -> ChatDashboardScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                    "MAP" -> NeonMapScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                    "PROFILE" -> ProfileEditorScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                    "ADMIN" -> AdminDashboardScreen(viewModel, primaryAccentColor, ::triggerNotificationBanner)
                }

                // Interactive Push notification alert banner floating at the top of everything
                AnimatedVisibility(
                    visible = notificationMessage != null,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .background(Color(0xFF13131F), RoundedCornerShape(16.dp))
                            .border(1.5.dp, primaryAccentColor, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Celebration,
                                contentDescription = "Alert",
                                tint = primaryAccentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "HYPER LOUNGE ALERT",
                                    fontSize = 11.sp,
                                    color = primaryAccentColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = notificationMessage ?: "",
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // 2. Stories Overlay Display (disappearing progress indicator)
                activeStory?.let { story ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.96f))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        viewModel.selectedStory.value = null
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        var storyProgress by remember { mutableStateOf(0f) }

                        LaunchedEffect(story.id) {
                            storyProgress = 0f
                            while (storyProgress < 1.0f) {
                                delay(60)
                                storyProgress += 0.02f
                            }
                            viewModel.selectedStory.value = null
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Story bar timers
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = storyProgress,
                                    color = primaryAccentColor,
                                    trackColor = Color.White.copy(alpha = 0.2f),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp)
                                        .clip(CircleShape)
                                )
                            }

                            // Story header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = story.creatorAvatar,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .border(1.dp, primaryAccentColor, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = story.creatorName,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }

                                IconButton(onClick = { viewModel.selectedStory.value = null }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Story main picture
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.DarkGray)
                            ) {
                                AsyncImage(
                                    model = story.imageUrl,
                                    contentDescription = "Story Content",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action reaction box
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = "",
                                    onValueChange = {},
                                    placeholder = { Text("Send quick VIP reaction...", color = Color.Gray, fontSize = 12.sp) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = {
                                    triggerNotificationBanner("Reaction sent to ${story.creatorName}! ❤️")
                                    viewModel.selectedStory.value = null
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = "Send",
                                        tint = primaryAccentColor
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Simulated peer-to-peer WebRTC live calling screens
                if (callState != "IDLE") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF030305).copy(alpha = 0.98f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))

                            // Avatar with vibrating borders mapping WebRTC indicator soundwaves
                            Box(contentAlignment = Alignment.Center) {
                                val pulseScale by rememberInfiniteTransition().animateFloat(
                                    initialValue = 1.0f,
                                    targetValue = 1.25f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1000, easing = LinearEasing),
                                        repeatMode = RepeatMode.Reverse
                                    )
                                )

                                Box(
                                    modifier = Modifier
                                        .size(150.dp * pulseScale)
                                        .alpha(0.3f)
                                        .background(primaryAccentColor.copy(alpha = 0.3f), CircleShape)
                                )

                                AsyncImage(
                                    model = callParticipantAvatar,
                                    contentDescription = "Caller",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, primaryAccentColor, CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = callParticipant,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Call Status Label
                            Text(
                                text = when (callState) {
                                    "RINGING" -> "Hyper VIP Encrypted Call Ringing..."
                                    "CONNECTED" -> {
                                        val minutes = callDurationSec / 60
                                        val seconds = callDurationSec % 60
                                        "Connected • Live WebRTC: %02d:%02d".format(minutes, seconds)
                                    }
                                    "ENDED" -> "Call Disconnected"
                                    else -> ""
                                },
                                color = primaryAccentColor,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )

                            // Simulated interactive local WebRTC stream window if video call
                            if (isVideoCall && callState == "CONNECTED") {
                                Spacer(modifier = Modifier.height(20.dp))
                                Box(
                                    modifier = Modifier
                                        .size(180.dp, 120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                        .background(Color.DarkGray)
                                ) {
                                    // Simulated VIP local webcam
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_launcher_background),
                                        contentDescription = "My Camera",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        colorFilter = ColorFilter.tint(primaryAccentColor.copy(alpha = 0.5f))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text("Self (VIP Guest)", color = Color.White, fontSize = 9.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1.5f))

                            // Call Control keys row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 60.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (callState == "RINGING") {
                                    IconButton(
                                        onClick = { viewModel.endCall() },
                                        modifier = Modifier
                                            .size(64.dp)
                                            .background(Color.Red, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CallEnd,
                                            contentDescription = "Decline",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.answerCall() },
                                        modifier = Modifier
                                            .size(64.dp)
                                            .background(Color.Green, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "Accept",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                } else if (callState == "CONNECTED") {
                                    // In Call keys
                                    IconButton(
                                        onClick = { triggerNotificationBanner("Mic Muted!") },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MicOff,
                                            contentDescription = "Mute",
                                            tint = Color.White
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.endCall() },
                                        modifier = Modifier
                                            .size(64.dp)
                                            .background(Color.Red, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CallEnd,
                                            contentDescription = "Disconnect",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { triggerNotificationBanner("Camera toggled!") },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VideocamOff,
                                            contentDescription = "Camera Toggle",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 1: Shorts Feed Screen (TikTok style)
// ==========================================
@Composable
fun ShortsFeedScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val videos by viewModel.videos.collectAsStateWithLifecycle()

    if (videos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = accentColor)
        }
    } else {
        // Vertical infinite feed
        // We use a clean LazyColumn with snapping controls or simple vertical progression
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(videos) { video ->
                ShortVideoPlayerCard(video, viewModel, accentColor, bannerNotifier)
            }
        }
    }
}

@Composable
fun LazyItemScope.ShortVideoPlayerCard(
    video: ShortVideo,
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    var showGiftDock by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillParentMaxWidth()
            .fillParentMaxHeight()
            .background(Color.Black)
    ) {
        // 1. Core Visual backdrop (high fidelity visual context simulation)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Neon drawing panel or static visual overlays mapping video
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background deep dynamic gradient matching video category
                val colors = when (video.category) {
                    "TRENDING" -> listOf(Color(0xFF0F001A), Color(0xFF0A0010))
                    "COCKTAILS" -> listOf(Color(0xFF1B1400), Color(0xFF0C0900))
                    else -> listOf(Color(0xFF00161A), Color(0xFF000A0C))
                }
                drawRect(
                    brush = Brush.verticalGradient(colors)
                )

                // Simulated light beams pulsing
                drawCircle(
                    color = accentColor.copy(alpha = 0.08f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.5f, size.height * 0.4f)
                )
            }

            // Beautiful placeholder central illustration mapping video context
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = when(video.category) {
                        "TRENDING" -> Icons.Default.OnlinePrediction
                        "COCKTAILS" -> Icons.Default.LocalBar
                        else -> Icons.Default.Explore
                    },
                    contentDescription = null,
                    tint = accentColor.copy(alpha = 0.4f),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "[ LIVE HD SIMULATION ]",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = accentColor.copy(alpha = 0.8f),
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Double Tap to Send Sparks ❤️",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        // 2. Playback indicators
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.Green, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("1080p Ultra HD", color = Color.White, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
            }
        }

        // 3. Right Action Column panel
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Creator item
            Box(contentAlignment = Alignment.BottomCenter) {
                AsyncImage(
                    model = video.creatorAvatar,
                    contentDescription = "Creator",
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, accentColor, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .offset(y = 6.dp)
                        .size(16.dp)
                        .background(accentColor, CircleShape)
                        .clickable { bannerNotifier("Following ${video.creatorName}!") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.Black, modifier = Modifier.size(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Like key
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { viewModel.toggleLikeVideo(video.id) },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (video.isLiked) Color.Red else Color.White
                    )
                }
                Text(
                    text = "${video.likes}",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comments key
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { bannerNotifier("Interactive Comments opened for #${video.creatorName}") },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "Comments",
                        tint = Color.White
                    )
                }
                Text(
                    text = "${video.comments}",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Gift Creator economy button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { showGiftDock = !showGiftDock },
                    modifier = Modifier
                        .size(44.dp)
                        .background(accentColor.copy(alpha = 0.25f), CircleShape)
                        .border(1.dp, accentColor, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Gifts",
                        tint = accentColor
                    )
                }
                Text(
                    text = "GIFT",
                    fontSize = 10.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // 4. Overlaid bottom video information pane
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.85f)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "@${video.creatorName}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                if (video.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = video.description,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Animated horizontal music ticker
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = video.musicName,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 5. Spited Creator Gift dock Popup overlay
        AnimatedVisibility(
            visible = showGiftDock,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF13131F).copy(alpha = 0.95f), RoundedCornerShape(20.dp))
                    .border(1.5.dp, accentColor, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SEND EXCLUSIVE HIGH-ROLLER GIFT",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(onClick = { showGiftDock = false }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val gifts = listOf(
                        Triple("Champagne Shower", 300, Icons.Default.LocalBar),
                        Triple("Gold Crown VIP", 150, Icons.Default.EmojiEvents),
                        Triple("Sapphire Sparkler", 50, Icons.Default.AutoAwesome)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        gifts.forEach { (name, cost, icon) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                    .clickable {
                                        val curProfile = viewModel.userProfile.value
                                        if (curProfile == null || curProfile.coins < cost) {
                                            bannerNotifier("Insufficient Coins balance! Tap Wallet to purchase.")
                                        } else {
                                            viewModel.giftVideoCreator(video.id, cost)
                                            bannerNotifier("Sent $name to @${video.creatorName}! 🎁✨")
                                            showGiftDock = false
                                        }
                                    }
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = icon, contentDescription = name, tint = accentColor, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(name, fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text("$cost coins", fontSize = 10.sp, color = AccentGold, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 2: Instagram/Story Photo Feed Screen
// ==========================================
@Composable
fun PhotoFeedScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val posts by viewModel.feedPosts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()

    var activeSubTab by remember { mutableStateOf("STREAM") } // "STREAM", "EXPLORE", "UPLOAD"

    // Custom Upload parameters
    var customCaption by remember { mutableStateOf("") }
    var customImageUrl by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Toggle tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            listOf("STREAM", "EXPLORE", "UPLOAD").forEach { tab ->
                val active = activeSubTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (active) accentColor else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { activeSubTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (active) Color.Black else Color.White
                    )
                }
            }
        }

        when (activeSubTab) {
            "STREAM" -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Instagram Stories row at the top
                    item {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                text = "ACTIVE LOUNGE STORIES",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
                                fontFamily = FontFamily.Monospace
                            )
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(stories) { story_item ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.clickable {
                                            viewModel.selectedStory.value = story_item
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(62.dp)
                                                .background(Color.DarkGray, CircleShape)
                                                .border(
                                                    2.dp,
                                                    Brush.verticalGradient(listOf(accentColor, Color(0xFFFF2B85))),
                                                    CircleShape
                                                )
                                                .padding(3.dp)
                                        ) {
                                            AsyncImage(
                                                model = story_item.creatorAvatar,
                                                contentDescription = "Avatar",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(story_item.creatorName, color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Feed entries
                    items(posts) { post ->
                        FeedPostCard(post, viewModel, accentColor, bannerNotifier)
                    }
                }
            }

            "EXPLORE" -> {
                // Dual Column AI Discover Page
                val exploreCards = listOf(
                    Pair("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=300", "Tokyo Sapphire Skies Roof"),
                    Pair("https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=300", "Golden VIP Bottle Lounges"),
                    Pair("https://images.unsplash.com/photo-1543007630-9710e4a00a20?w=300", "Noir Cocktail Speakeasies"),
                    Pair("https://images.unsplash.com/photo-1560624052-449f5ddf0c31?w=300", "Secret Velvet VIP Chambers")
                )

                Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                    Text(
                        text = "AI-POWERED RECOMMENDATIONS",
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "Elite destinations analyzed based on trending hashtags in Tokyo district.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(exploreCards.subList(0,2), exploreCards.subList(2,4)).forEach { list ->
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                list.forEach { (img, label) ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.DarkGray)
                                            .clickable { bannerNotifier("Matched! Point your map locator to $label") }
                                    ) {
                                        AsyncImage(
                                            model = img,
                                            contentDescription = label,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                                    )
                                                )
                                        )
                                        Text(
                                            text = label,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "UPLOAD" -> {
                // Post Story or Feed Form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "BROADCAST TO THE LOUNGE",
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    OutlinedTextField(
                        value = customCaption,
                        onValueChange = { customCaption = it },
                        label = { Text("Caption or hashtags") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = customImageUrl,
                        onValueChange = { customImageUrl = it },
                        label = { Text("Image URL (leave blank for template)") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/item.jpg") },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                viewModel.submitNewStory(customImageUrl)
                                bannerNotifier("Story posted successfully! 24h timer active.")
                                customCaption = ""
                                customImageUrl = ""
                                activeSubTab = "STREAM"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                        ) {
                            Text("Post as VIP Story", color = Color.White)
                        }

                        Button(
                            onClick = {
                                viewModel.submitNewPost(customCaption, customImageUrl)
                                bannerNotifier("Feed Post uploaded and shared in Tokyo channels!")
                                customCaption = ""
                                customImageUrl = ""
                                activeSubTab = "STREAM"
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Upload to Feed", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedPostCard(
    post: FeedPost,
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = post.creatorAvatar,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.dp, accentColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = post.creatorName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (post.isVerified) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = accentColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text("Tokyo VIP Club • 3m ago", fontSize = 10.sp, color = Color.Gray)
                    }
                }

                IconButton(onClick = { viewModel.triggerSimulatedCall(post.creatorName, post.creatorAvatar) }) {
                    Icon(imageVector = Icons.Default.Videocam, contentDescription = "Cam", tint = accentColor)
                }
            }

            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Post content",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Bottom Actions Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = { viewModel.toggleLikeFeedPost(post.id) }) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) Color.Red else Color.White
                        )
                    }

                    IconButton(onClick = { bannerNotifier("Comment section loaded for post #${post.id}") }) {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = "Comm", tint = Color.White)
                    }

                    IconButton(onClick = { bannerNotifier("Shared secure VIP link to chat!") }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }

                IconButton(onClick = { viewModel.toggleSaveFeedPost(post.id) }) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (post.isSaved) accentColor else Color.White
                    )
                }
            }

            // Caption details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "${post.likesCount} high-roller likes",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.caption,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )

                Text(
                    text = post.hashtags,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// ==========================================
// SCREEN 3: WhatsApp-style Encrypted messaging
// ==========================================
@Composable
fun ChatDashboardScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val activeMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()
    val activeRoom by viewModel.activeChatRoom.collectAsStateWithLifecycle()

    var typedText by remember { mutableStateOf("") }
    var subTab by remember { mutableStateOf("AI_CONCIERGE") } // "AI_CONCIERGE", "TOKYO_GROUP"

    LaunchedEffect(subTab) {
        viewModel.activeChatRoom.value = subTab
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Horizontal Chat channels selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (subTab == "AI_CONCIERGE") accentColor.copy(alpha = 0.25f) else Color(0xFF13131F),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (subTab == "AI_CONCIERGE") accentColor else Color.White.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { subTab = "AI_CONCIERGE" }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.SmartToy, contentDescription = "AI", tint = accentColor)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Aurelia AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Online concierge", color = Color.Gray, fontSize = 9.sp)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (subTab == "Lounge_Tokyo_Group") accentColor.copy(alpha = 0.25f) else Color(0xFF13131F),
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (subTab == "Lounge_Tokyo_Group") accentColor else Color.White.copy(alpha = 0.05f),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { subTab = "Lounge_Tokyo_Group" }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Groups, contentDescription = "Group", tint = accentColor)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Tokyo VIP Club", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Group channel", color = Color.Gray, fontSize = 9.sp)
                    }
                }
            }
        }

        // Channels header displaying calling controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.Green, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (activeRoom == "AI_CONCIERGE") "SECURE CHAT WITH AURELIA" else "TOKYO REGIONAL LOUNGE ZONE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = {
                            viewModel.triggerSimulatedCall(
                                name = if (activeRoom == "AI_CONCIERGE") "Aurelia AI Concierge" else "Kenji V8",
                                avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
                                video = false
                            )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = "VoiceCall", tint = accentColor, modifier = Modifier.size(18.dp))
                    }

                    IconButton(
                        onClick = {
                            viewModel.triggerSimulatedCall(
                                name = if (activeRoom == "AI_CONCIERGE") "Aurelia AI Concierge" else "Elena Sky",
                                avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
                                video = true
                            )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = "VideoCall", tint = accentColor, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Messages list Column
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = false
        ) {
            items(activeMessages) { msg ->
                ChatBubble(msg, accentColor)
            }
        }

        // Input bottom bar (with Voice recording simulation keys)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF09090D))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // simulated microphone key for voice notes
                IconButton(
                    onClick = {
                        bannerNotifier("Hold button to record WebRTC audio note! Dynamic waveform active.")
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFF13131F), CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = "Voice note", tint = accentColor)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = typedText,
                    onValueChange = { typedText = it },
                    placeholder = { Text("Encrypted message...", color = Color.Gray, fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                    ),
                    maxLines = 2,
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (typedText.isNotBlank()) {
                                viewModel.sendMessage(typedText)
                                typedText = ""
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send key
                IconButton(
                    onClick = {
                        if (typedText.isNotBlank()) {
                            viewModel.sendMessage(typedText)
                            typedText = ""
                        }
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(accentColor, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: Message, accentColor: Color) {
    val isMe = msg.isFromUser
    val alignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isMe) accentColor else Color(0xFF13131F)
    val textColor = if (isMe) Color.Black else Color.White

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            Text(
                text = msg.senderName,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .background(
                        color = bubbleColor,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 0.dp,
                            bottomEnd = if (isMe) 0.dp else 16.dp
                        )
                    )
                    .border(
                        1.dp,
                        if (isMe) Color.Transparent else Color.White.copy(alpha = 0.05f),
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 0.dp,
                            bottomEnd = if (isMe) 0.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = msg.text,
                    color = textColor,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ==========================================
// SCREEN 4: Neon Lounge finder Map (Google Maps style)
// ==========================================
@Composable
fun NeonMapScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val searchWord by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterTag by viewModel.mapFilterTag.collectAsStateWithLifecycle()
    val activeLounge by viewModel.activeLoungeDetail.collectAsStateWithLifecycle()

    var showDirectionPath by remember { mutableStateOf(false) }

    // Synchronize direct routing lines triggers
    LaunchedEffect(activeLounge) {
        showDirectionPath = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top map search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchWord,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text("Search club by theme (e.g. onyx)", fontSize = 11.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = accentColor) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Geofence / Check-in simulator button
            IconButton(
                onClick = {
                    bannerNotifier("Geofence radar triggered! Arrival confirmed at Sapphire Heli-Lounge.")
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                    .border(1.dp, accentColor, RoundedCornerShape(12.dp))
            ) {
                Icon(imageVector = Icons.Default.GpsFixed, contentDescription = "GPS", tint = accentColor)
            }
        }

        // Sub tags toggles: Club, Rooftop, Lounge, VIP
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "CLUB", "ROOFTOP", "VIP", "LOUNGE").forEach { tag ->
                val selected = filterTag == tag
                Box(
                    modifier = Modifier
                        .background(
                            if (selected) accentColor else Color(0xFF13131F),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { viewModel.mapFilterTag.value = tag }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Color.Black else Color.White
                    )
                }
            }
        }

        // Interactive Drawing Canvas simulating Google maps with Radar-Sweep grids
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
        ) {
            val radarAngle by rememberInfiniteTransition().animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF030305))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                // Tap simulation triggers markers selections
                                val match = viewModel.loungeHotspots.random()
                                viewModel.activeLoungeDetail.value = match
                                bannerNotifier("Radar clicked at coords. Identified lounge ${match.name}!")
                            }
                        )
                    }
            ) {
                // Canvas size constraints
                val w = size.width
                val h = size.height
                val centerVal = Offset(w / 2f, h / 2f)

                // Draw map grid lines
                val gridStep = 60.dp.toPx()
                for (x in 0..(w / gridStep).toInt()) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.04f),
                        start = Offset(x * gridStep, 0f),
                        end = Offset(x * gridStep, h)
                    )
                }
                for (y in 0..(h / gridStep).toInt()) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.04f),
                        start = Offset(0f, y * gridStep),
                        end = Offset(w, y * gridStep)
                    )
                }

                // Radar concentric lines
                drawCircle(
                    color = accentColor.copy(alpha = 0.1f),
                    radius = w * 0.2f,
                    center = centerVal,
                    style = strokeStyleHelper()
                )
                drawCircle(
                    color = accentColor.copy(alpha = 0.06f),
                    radius = w * 0.4f,
                    center = centerVal,
                    style = strokeStyleHelper()
                )

                // Radial active sweep vector line
                val sweepX = centerVal.x + (w * 0.5f) * sin(Math.toRadians(radarAngle.toDouble())).toFloat()
                val sweepY = centerVal.y - (w * 0.5f) * sin(Math.toRadians((radarAngle + 90f).toDouble())).toFloat()
                drawLine(
                    color = accentColor.copy(alpha = 0.25f),
                    start = centerVal,
                    end = Offset(sweepX, sweepY),
                    strokeWidth = 3.dp.toPx()
                )

                // Draw active matching hotspots on the map!
                // High performance pulsing hotspot circles
                val hotspots = viewModel.loungeHotspots
                val colors = listOf(accentColor, Color(0xFFFF2B85), Color(0xFF00E5FF))

                hotspots.forEachIndexed { idx, spot ->
                    // Filter match
                    if (filterTag == "ALL" || spot.tag == filterTag) {
                        val pinColor = colors[idx % colors.size]
                        val pxOffset = when(idx) {
                            0 -> Offset(w * 0.25f, h * 0.3f)
                            1 -> Offset(w * 0.75f, h * 0.25f)
                            2 -> Offset(w * 0.35f, h * 0.7f)
                            else -> Offset(w * 0.65f, h * 0.62f)
                        }

                        // Pulsing outer ring
                        drawCircle(
                            color = pinColor.copy(alpha = 0.15f),
                            radius = 24.dp.toPx(),
                            center = pxOffset
                        )
                        // Inner solid pinpoint
                        drawCircle(
                            color = pinColor,
                            radius = 6.dp.toPx(),
                            center = pxOffset
                        )

                        // If user tapped and GET DIRECTIONS is active, draw neon path lines
                        if (showDirectionPath && activeLounge?.name == spot.name) {
                            drawLine(
                                color = pinColor,
                                start = centerVal,
                                end = pxOffset,
                                strokeWidth = 3.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                            )
                        }
                    }
                }
            }

            // Central locator pointer "YOU ARE HERE"
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(12.dp)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            )

            // Dynamic lounge detail card slide up pop up
            androidx.compose.animation.AnimatedVisibility(
                visible = activeLounge != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            ) {
                activeLounge?.let { spot ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF13131F).copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(spot.tag, color = accentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(spot.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }

                                IconButton(onClick = { viewModel.activeLoungeDetail.value = null }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(spot.description, color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(spot.ratingInfo, color = AccentGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        showDirectionPath = true
                                        bannerNotifier("Route calculated: GPS Navigation Active to ${spot.name}!")
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                                    modifier = Modifier.weight(1.5f)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Navigation, contentDescription = "Nav", tint = Color.Black, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("GET ROUTE", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }

                                Button(
                                    onClick = {
                                        viewModel.currentTab.value = "CHAT"
                                        bannerNotifier("Checking in! Live coordinates shared inside community channels.")
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("CHECK IN", color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun strokeStyleHelper() = androidx.compose.ui.graphics.drawscope.Stroke(
    width = 1.dp.value,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
)

// ==========================================
// SCREEN 5: Profile & Style Customizer Screen
// ==========================================
@Composable
fun ProfileEditorScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val activeProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val matchingAccent by viewModel.themeAccent.collectAsStateWithLifecycle()

    var editedBio by remember { mutableStateOf("") }

    LaunchedEffect(activeProfile) {
        activeProfile?.let {
            if (editedBio.isEmpty()) editedBio = it.bio
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // User profile header summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF13131F), RoundedCornerShape(20.dp))
                .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = activeProfile?.avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .border(2.dp, accentColor, CircleShape)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activeProfile?.name ?: "VIP User",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (activeProfile?.isVerified == true) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Badge",
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(editedBio, color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))

                Spacer(modifier = Modifier.height(12.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${activeProfile?.followerCount}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        Text("Followers", color = Color.Gray, fontSize = 10.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${activeProfile?.followingCount}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        Text("Following", color = Color.Gray, fontSize = 10.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("VIP GOLD", fontWeight = FontWeight.Bold, color = accentColor, fontSize = 15.sp)
                        Text("Member rank", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }

        // Feature: Customizable Accent selectors
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "CUSTOMIZE ACCENT THEME COLOR",
                fontWeight = FontWeight.Bold,
                color = accentColor,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val patterns = listOf(
                    Pair("GOLD", Color(0xFFE5C060)),
                    Pair("PINK", Color(0xFFFF2B85)),
                    Pair("CYAN", Color(0xFF00E5FF))
                )

                patterns.forEach { (name, color) ->
                    val active = matchingAccent == name
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                            .border(
                                width = if (active) 2.dp else 1.dp,
                                color = if (active) color else Color.White.copy(alpha = 0.05f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                viewModel.updateThemeAccent(name)
                                bannerNotifier("Accent color customized to $name theme! ✨")
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Coin economy wallet store
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "WALLET & CREATOR TOKENS",
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Buy virtual coins to gift elite content creators, unlock exclusive channels, and boost live feed streams.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                val coinPacks = listOf(
                    Pair("100 Coins", 5),
                    Pair("500 Coins", 20),
                    Pair("2000 Coins", 75)
                )

                coinPacks.forEach { (pack, priceUSD) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                            .clickable {
                                val added = when (priceUSD) {
                                    5 -> 100
                                    20 -> 500
                                    else -> 2000
                                }
                                viewModel.purchaseCoins(added)
                                bannerNotifier("Successfully added $added coins in your wallet! 🪙")
                            }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "Coin", tint = AccentGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(pack, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text("$$priceUSD.00 USD", color = accentColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Save modifications button
        Button(
            onClick = {
                viewModel.currentTab.value = "PHOTOS"
                bannerNotifier("Profile bio updated!")
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save & Explore Lounge", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

// ==========================================
// SCREEN 6: Admin Dashboard Mod Panel Screen
// ==========================================
@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    accentColor: Color,
    bannerNotifier: (String) -> Unit
) {
    val flags by viewModel.adminFlags.collectAsStateWithLifecycle()

    var broadcastText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "ADMIN MODERATOR DASHBOARD",
            fontWeight = FontWeight.Bold,
            color = accentColor,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace
        )

        // General analytical blocks
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Total Members", color = Color.Gray, fontSize = 9.sp)
                    Text("24,840", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text("+14.2% today", color = Color.Green, fontSize = 9.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Active Streams", color = Color.Gray, fontSize = 9.sp)
                    Text("182", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text("12 VIP sessions", color = accentColor, fontSize = 9.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF13131F), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text("Coin Economy", color = Color.Gray, fontSize = 9.sp)
                    Text("1.8M", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text("$45,820 pool", color = AccentGold, fontSize = 9.sp)
                }
            }
        }

        // Live flags queue
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "PENDING SYSTEM MODERATION ALERTS",
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                flags.forEach { flag ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(flag.details, fontSize = 11.sp, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Reporter: ${flag.reporter}", fontSize = 9.sp, color = Color.Gray)
                        }

                        if (flag.status == "PENDING") {
                            Button(
                                onClick = {
                                    viewModel.resolveFlag(flag.id)
                                    bannerNotifier("Alert resolved successfully. Warning action taken!")
                                },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp)
                            ) {
                                Text("RESOLVE", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(flag.status, fontSize = 10.sp, color = Color.Green, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Broadcast System push notification campaigns
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13131F)),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "LAUNCH GLOBAL NOTIFICATION BANNER",
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                OutlinedTextField(
                    value = broadcastText,
                    onValueChange = { broadcastText = it },
                    label = { Text("Broadcast headline text") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Button(
                    onClick = {
                        if (broadcastText.isNotBlank()) {
                            bannerNotifier(broadcastText)
                            broadcastText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Push Broadcast Campaign", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
