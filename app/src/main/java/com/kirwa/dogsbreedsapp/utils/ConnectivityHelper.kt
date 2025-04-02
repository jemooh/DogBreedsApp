package com.kirwa.dogsbreedsapp.utils

import android.content.Context

class ConnectivityHelper(private val context: Context) {
    fun isConnected(): Boolean = Util.isConnected(context)
}