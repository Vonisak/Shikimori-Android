package com.example.shikimoriandroid.domain.utils

class AnimeStringSwitcher {

    fun kindSwitch(kind: String): String {
        return when (kind) {
            "tv" -> "TV сериал"
            "movie" -> "Фильм"
            "ova" -> "OVA"
            "ona" -> "ONA"
            "music" -> "Клип"
            "special" -> "Спешл"
            else -> "?"
        }
    }

    fun ageRatingSwitch(rating: String): String {
        return when (rating) {
            "g" -> "G"
            "pg" -> "PG"
            "pg_13" -> "PG-13"
            "r" -> "R"
            "r_plus" -> "R+"
            "rx" -> "Rx"
            else -> "?"
        }
    }
}