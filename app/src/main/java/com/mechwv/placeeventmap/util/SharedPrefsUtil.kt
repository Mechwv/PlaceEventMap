package com.mechwv.placeeventmap.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsUtil {
    companion object {
        fun writeTokenIntoPreferences(context: Context, jwtToken: String){
            val sharedPreferences = context.getSharedPreferences("PlaceEventMapPrefs", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString("jwtToken", jwtToken).apply()
        }

        fun readTokenFromPreferences(context: Context) {
            val sharedPreferences = context.getSharedPreferences("PlaceEventMapPrefs", Context.MODE_PRIVATE)
            val jwtToken: String? = sharedPreferences.getString("jwtToken", "defaultValue")
            println("JWT_TOKEN_READ $jwtToken")
        }
    }
}