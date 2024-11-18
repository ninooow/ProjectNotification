package com.example.ninogantenk

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.ninogantenk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    // Membuat BroadcastReceiver untuk memperbarui UI saat notifikasi ditekan
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateCounts()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Registrasi receiver
        val intentFilter = IntentFilter().apply {
            addAction("UPDATE_COUNT")
        }
        registerReceiver(receiver, intentFilter)

        binding.btnNotif.setOnClickListener {
            showNotification()
        }

        // Perbarui tampilan saat pertama kali dibuka
        updateCounts()
    }

    private fun showNotification() {
        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // Intent untuk tombol like
        val intentAction1 = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_BUTTON_1"
        }
        val pendingIntent1 = PendingIntent.getBroadcast(
            this, 0, intentAction1, flag or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Intent untuk tombol dislike
        val intentAction2 = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_BUTTON_2"
        }
        val pendingIntent2 = PendingIntent.getBroadcast(
            this, 1, intentAction2, flag or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notifImage = BitmapFactory.decodeResource(resources, R.drawable.nino)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notip)
            .setContentTitle("Apakah benar?")
            .setContentText("Nino ganteng bangetkah?")
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(notifImage))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, "Like", pendingIntent1)
            .addAction(1, "Dislike", pendingIntent2)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId, "Benar atau Tidak", NotificationManager.IMPORTANCE_DEFAULT
            )
            notifManager.createNotificationChannel(notifChannel)
        }

        notifManager.notify(notifId, builder.build())
    }

    override fun onResume() {
        super.onResume()
        updateCounts()
    }

    // Metode untuk memperbarui jumlah like/dislike di UI
    private fun updateCounts() {
        val sharedPreferences = getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)
        val countButton1 = sharedPreferences.getInt("button_1_count", 0)
        val countButton2 = sharedPreferences.getInt("button_2_count", 0)
        binding.sumLike.text = "$countButton1"
        binding.sumDislike.text = "$countButton2"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
