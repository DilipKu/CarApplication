package com.example.mycarapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var speedValue: TextView
    private lateinit var fuelValue: TextView
    private lateinit var fuelProgress: ProgressBar
    private lateinit var batteryValue: TextView
    private lateinit var batteryProgress: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val mockUpdateRunnable = object : Runnable {
        override fun run() {
            updateMockData()
            handler.postDelayed(this, 1000)
        }
    }

    private var currentSpeed = 0
    private var currentFuel = 75
    private var currentBattery = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("TEST_APP", "MainActivity Opened")

        Toast.makeText(this, "MainActivity Opened", Toast.LENGTH_LONG).show()
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "MainActivity Opened", Toast.LENGTH_LONG).show()

        speedValue = findViewById(R.id.speedValue)
        fuelValue = findViewById(R.id.fuelValue)
        fuelProgress = findViewById(R.id.fuelProgress)
        batteryValue = findViewById(R.id.batteryValue)
        batteryProgress = findViewById(R.id.batteryProgress)

        handler.post(mockUpdateRunnable)
    }

    private fun updateMockData() {
        // Randomly adjust speed
        currentSpeed += Random.nextInt(-5, 6)
        currentSpeed = currentSpeed.coerceIn(0, 180)

        // Slowly decrease fuel and battery
        if (Random.nextFloat() > 0.95f) currentFuel -= 1
        if (Random.nextFloat() > 0.98f) currentBattery -= 1
        
        currentFuel = currentFuel.coerceIn(0, 100)
        currentBattery = currentBattery.coerceIn(0, 100)

        // Update UI
        speedValue.text = getString(R.string.speed_format, currentSpeed)
        
        fuelValue.text = getString(R.string.percent_format, currentFuel)
        fuelProgress.progress = currentFuel

        batteryValue.text = getString(R.string.percent_format, currentBattery)
        batteryProgress.progress = currentBattery
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(mockUpdateRunnable)
    }
}
