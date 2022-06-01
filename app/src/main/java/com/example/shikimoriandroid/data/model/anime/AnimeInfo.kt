package com.example.shikimoriandroid.data.model.anime

import com.google.gson.annotations.SerializedName

data class AnimeInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nameEng: String,
    @SerializedName("russian") val nameRus: String,
    @SerializedName("episodes") val episodes: String,
    @SerializedName("image") val poster: AnimeImage,
    @SerializedName("description") val description: String,
    @SerializedName("score") val score: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("status") val status: String,
    @SerializedName("episodes_aired") val episodesAired: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("rating") val ageRating: String,
    @SerializedName("genres") val genres: List<AnimeGenre>,
    @SerializedName("user_rate") val userRate: UserRate?,
    @SerializedName("rates_scores_stats") val ratesStats: List<Stats>,
    @SerializedName("rates_statuses_stats") val statusesStats: List<Stats>
)