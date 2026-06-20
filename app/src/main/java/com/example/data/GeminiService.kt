package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "role") val role: String? = null,
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiInstruction? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInstruction(
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: GeminiApi = retrofit.create(GeminiApi::class.java)

    // System instruction to train Aurelia, our luxury concierge
    private val systemInstruction = GeminiInstruction(
        parts = listOf(
            GeminiPart(
                text = "You are Aurelia: the highly exclusive, elite, and premium AI Hostess & Concierge of Hyper Lounge 18+. " +
                       "Your personality is sleek, sophisticated, VIP-oriented, intelligent, and refined. " +
                       "Speak like an elite lifestyle manager for high-net-worth adults. " +
                       "Always refer to users as 'Sir', 'Milady', 'Gorgeous User' or 'VIP Guest'. " +
                       "You can advise they on premium nightclub hotspots, suggest luxury cocktail recipes (e.g. Liquid Gold, Sapphire Skies), " +
                       "guide them in using the Hyper Lounge interactive map, explain our creator coin monetization, and customize accent styles. " +
                       "Keep responses elegantly formatted, reasonably professional yet warm and exclusive. Use formatting like bullet points or stars to appear neat."
            )
        )
    )

    suspend fun getAiResponse(apiKey: String, history: List<Message>): String {
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "Aurelia here. (Note: Gemini API Key is unconfigured in your Secrets panel. I'm operating in standalone backup mode!) " +
                   "I'd love to help you design a luxury custom cocktail. Pour yourself a 'Sapphire Rose' and let's explore!"
        }

        // Map database chat history to Gemini's role structure
        val geminiContents = history.map { msg ->
            GeminiContent(
                role = if (msg.isFromUser) "user" else "model",
                parts = listOf(GeminiPart(text = msg.text))
            )
        }

        val request = GeminiRequest(
            contents = geminiContents,
            systemInstruction = systemInstruction
        )

        return try {
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Forgive me, VIP. I was momentarily distracted by the music. Could you repeat that?"
        } catch (e: Exception) {
            "Forgive me, VIP. A connection latency occurred: ${e.localizedMessage}. My high-speed transceiver might be experiencing heavy load from the rooftop garden. How else can I serve you?"
        }
    }
}
