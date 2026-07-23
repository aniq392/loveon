

// data/MemberData.kt
package xyz.fsg123.loveon.data

data class MemberData(
    val mb_id: String,
    val mb_nick: String,
    val mb_name: String,
    val mb_email: String,
    val mb_level: Int,          // 2: 일반, 1: 블랙리스트
    val mb_profile_img: String? = null,
    val mb_app_id: String? = null,
    val mb_app_version: String? = null,
    val is_blacklisted: Boolean = false
)