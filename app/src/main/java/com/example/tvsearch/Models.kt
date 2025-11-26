package com.example.tvsearch

data class SearchResult(
    val score: Double?,
    val show: Show
)

data class Show(
    val id: Int,
    val name: String,
    val rating: Rating?,
    val image: Image?,
    val summary: String?
)

data class Rating(
    val average: Double?
)

data class Image(
    val medium: String?,
    val original: String?
)

