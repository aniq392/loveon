// data/ApiResponse.kt
package xyz.fsg123.loveon.data

data class ApiResponse<T>(
    val result: Boolean,
    val message: String,
    val data: T? = null,
    val field: String? = null,
    val code: String? = null,
    val min_version: String? = null,
    val update_url: String? = null
)