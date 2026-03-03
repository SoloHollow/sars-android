package com.example.sars

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val role: String? = null,
    @SerializedName("fullName", alternate = ["full_name", "name"])
    val fullName: String? = null
)

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("fullName", alternate = ["full_name", "name", "fullname"])
    val fullName: String? = null,
    val role: String? = "CITIZEN"
)

data class ProfileResponse(
    val user: User? = null
)

data class DirectoryEntry(
    val id: String? = null,
    @SerializedName("title", alternate = ["name"])
    val name: String? = null,
    @SerializedName("contactNumber", alternate = ["contact"])
    val contact: String? = null,
    @SerializedName("city", alternate = ["address"])
    val address: String? = null,
    @SerializedName("category", alternate = ["type"])
    val type: String? = null,
    val state: String? = null,
    val content: String? = null
)

data class DirectoryListResponse(
    @SerializedName("entries", alternate = ["data", "results"])
    val entries: List<DirectoryEntry>? = null
)
