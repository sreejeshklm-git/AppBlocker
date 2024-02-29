package com.example.appblockr.model

data class UsesStatsDataModel(
    var email: String? = null,
    var dataArrayList: ArrayList<StatsModel>? = null
) {
    operator fun compareTo(app: UsesStatsDataModel): Int {
        return email!!.compareTo(app.email!!)
    }
}