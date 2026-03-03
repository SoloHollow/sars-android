package com.example.sars

import com.google.gson.annotations.SerializedName

data class AnimalReport(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("reportedBy")
    val reportedBy: String? = null,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("animalType")
    val animalType: AnimalType,
    @SerializedName("isPack")
    val isPack: Boolean,
    @SerializedName("countEstimate")
    val countEstimate: Int,
    @SerializedName("healthStatus")
    val healthStatus: HealthStatus,
    @SerializedName("extraInfo")
    val extraInfo: String?,
    @SerializedName("status")
    val status: ReportStatus = ReportStatus.OPEN,
    @SerializedName("createdAt")
    val createdAt: String? = null
)

enum class AnimalType {
    DOG, CAT, OTHER
}

enum class HealthStatus {
    NORMAL, INJURED, RABIES_SUSPECT, DISEASED
}

enum class ReportStatus {
    OPEN, IN_PROGRESS, RESOLVED
}