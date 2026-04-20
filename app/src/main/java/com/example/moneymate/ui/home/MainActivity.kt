package com.example.moneymate.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.moneymate.R
import com.example.moneymate.ui.category.CategoryListFragment
import com.example.moneymate.ui.stats.StatsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.Manifest
import com.example.moneymate.utils.NotificationHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        replaceFragment(HomeFragment())
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
//                R.id.nav_budget -> replaceFragment(BudgetFragment())
                R.id.nav_stats -> replaceFragment(StatsFragment())
                R.id.nav_category -> replaceFragment(CategoryListFragment())
//                R.id.nav_saving -> replaceFragment(SavingFragment())
//                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }
        createNotificationChannel()
        requestNotificationPermission()




    }
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "money_channel",
                "MoneyMate Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo từ MoneyMate"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}