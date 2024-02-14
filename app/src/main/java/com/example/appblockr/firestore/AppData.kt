package com.example.appblockr.firestore

data class AppData(
    val appName: String?,
    val bundle_id: String?,
    val email: String?,
    val duration: String?,
    val clicksCount: String?,
    val isAppLocked: Boolean
) {
    constructor() : this("", "", "", "", "", false)
}
