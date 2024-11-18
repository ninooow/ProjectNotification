package com.example.ninogantenk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (intent.action) {
            "ACTION_BUTTON_1" -> {
                val countButton1 = sharedPreferences.getInt("button_1_count", 0) + 1
                editor.putInt("button_1_count", countButton1)
                Toast.makeText(context, "Like Count: $countButton1", Toast.LENGTH_SHORT).show()
            }
            "ACTION_BUTTON_2" -> {
                val countButton2 = sharedPreferences.getInt("button_2_count", 0) + 1
                editor.putInt("button_2_count", countButton2)
                Toast.makeText(context, "Dislike Count: $countButton2", Toast.LENGTH_SHORT).show()
            }
        }
        editor.apply()
        // Kirim broadcast untuk memperbarui UI di MainActivity
        context.sendBroadcast(Intent("UPDATE_COUNT"))
    }
}