package com.kirwa.dogsbreedsapp.data.local.datasource

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi

class SharedPreferences
constructor(private val settings: SharedPreferences) {
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String?): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus =
            activity.shouldShowRequestPermissionRationale(permission!!)
        return prevShouldShowStatus != currShouldShowStatus
    }

    private fun getRatinaleDisplayStatus(
        context: Context,
        permission: String?
    ): Boolean {
        val genPrefs = context.getSharedPreferences(
            "GENERIC_PREFERENCES",
            Context.MODE_PRIVATE
        )
        return genPrefs.getBoolean(permission, false)
    }
}
