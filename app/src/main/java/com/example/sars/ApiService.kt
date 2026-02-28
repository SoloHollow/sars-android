package com.example.sars

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Simple HTTP client to fetch reports from the backend.
 * Uses only built-in Java/Android APIs (no Retrofit needed).
 *
 * ⚠️  Change BASE_URL to your actual backend URL.
 */
object ApiService {

    // ── CHANGE THIS to your real backend base URL ─────────────────────────
    private const val BASE_URL = "https://your-backend-api.com"

    /**
     * GET /reports  →  List<AnimalReport>
     * Runs on IO dispatcher; safe to call from a coroutine.
     */
    suspend fun fetchReports(): Result<List<AnimalReport>> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/reports")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val body = connection.inputStream.bufferedReader().readText()
                connection.disconnect()
                Result.success(parseReports(body))
            } else {
                connection.disconnect()
                Result.failure(Exception("Server returned $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * POST /reports  →  saves a new report, returns the saved report with id.
     */
    suspend fun submitReport(report: AnimalReport): Result<AnimalReport> =
        withContext(Dispatchers.IO) {
            try {
                val url = URL("$BASE_URL/reports")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Accept", "application/json")
                connection.doOutput = true
                connection.connectTimeout = 10_000
                connection.readTimeout = 10_000

                val body = reportToJson(report)
                connection.outputStream.bufferedWriter().use { it.write(body) }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK ||
                    responseCode == HttpURLConnection.HTTP_CREATED
                ) {
                    val responseBody = connection.inputStream.bufferedReader().readText()
                    connection.disconnect()
                    // Parse the returned object and return it
                    val arr = parseReports("[$responseBody]")
                    Result.success(arr.firstOrNull() ?: report)
                } else {
                    connection.disconnect()
                    Result.failure(Exception("Server returned $responseCode"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // ── JSON parsing ──────────────────────────────────────────────────────

    private fun parseReports(json: String): List<AnimalReport> {
        val array = JSONArray(json)
        val list = mutableListOf<AnimalReport>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            runCatching {
                list.add(
                    AnimalReport(
                        id = obj.optString("id", null.toString())
                            ?.let { runCatching { java.util.UUID.fromString(it) }.getOrNull() },
                        reportedBy = java.util.UUID.fromString(
                            obj.optString("reportedBy", java.util.UUID.randomUUID().toString())
                        ),
                        latitude = obj.getDouble("latitude"),
                        longitude = obj.getDouble("longitude"),
                        city = obj.optString("city").ifEmpty { null },
                        state = obj.optString("state").ifEmpty { null },
                        animalType = AnimalType.valueOf(
                            obj.optString("animalType", "DOG").uppercase()
                        ),
                        isPack = obj.optBoolean("isPack", false),
                        countEstimate = obj.optInt("countEstimate", 1),
                        healthStatus = HealthStatus.valueOf(
                            obj.optString("healthStatus", "NORMAL").uppercase()
                        ),
                        extraInfo = obj.optString("extraInfo").ifEmpty { null },
                        status = ReportStatus.valueOf(
                            obj.optString("status", "OPEN").uppercase()
                        ),
                        createdAt = obj.optString("createdAt").ifEmpty { null }
                    )
                )
            } // silently skip malformed entries
        }
        return list
    }

    private fun reportToJson(report: AnimalReport): String {
        return JSONObject().apply {
            put("reportedBy", report.reportedBy.toString())
            put("latitude", report.latitude)
            put("longitude", report.longitude)
            put("city", report.city ?: JSONObject.NULL)
            put("state", report.state ?: JSONObject.NULL)
            put("animalType", report.animalType.name)
            put("isPack", report.isPack)
            put("countEstimate", report.countEstimate)
            put("healthStatus", report.healthStatus.name)
            put("extraInfo", report.extraInfo ?: JSONObject.NULL)
        }.toString()
    }
}
