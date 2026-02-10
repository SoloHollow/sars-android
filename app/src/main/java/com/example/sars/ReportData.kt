package com.example.sars

import java.util.UUID

data class AnimalReport(
    val id: UUID? = null,
    val reportedBy: UUID,
    val latitude: Double,
    val longitude: Double,
    val city: String?,
    val state: String?,
    val animalType: AnimalType,
    val isPack: Boolean,
    val countEstimate: Int,
    val healthStatus: HealthStatus,
    val extraInfo: String?,
    val status: ReportStatus = ReportStatus.OPEN,
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