package com.example.mycarapplication

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var speedValue: TextView
    private lateinit var speedometer: ProgressBar
    private lateinit var fuelValue: TextView
    private lateinit var fuelProgress: ProgressBar
    private lateinit var batteryValue: TextView
    private lateinit var batteryProgress: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private var speedAnimator: ValueAnimator? = null

    private val mockUpdateRunnable = object : Runnable {
        override fun run() {
            updateMockData()
            handler.postDelayed(this, 1500) // Slightly longer interval to allow animation to shine
        }
    }

    private var currentSpeed = 0
    private var targetSpeed = 0
    private var currentFuel = 75
    private var currentBattery = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        speedValue = findViewById(R.id.speedValue)
        speedometer = findViewById(R.id.speedometer)
        fuelValue = findViewById(R.id.fuelValue)
        fuelProgress = findViewById(R.id.fuelProgress)
        batteryValue = findViewById(R.id.batteryValue)
        batteryProgress = findViewById(R.id.batteryProgress)

        handler.post(mockUpdateRunnable)
    }

    private fun updateMockData() {
        // Randomly adjust target speed
        targetSpeed += Random.nextInt(-15, 20)
        targetSpeed = targetSpeed.coerceIn(0, 180)

        // Animate from currentSpeed to targetSpeed
        animateSpeed(currentSpeed, targetSpeed)
        currentSpeed = targetSpeed

        // Slowly decrease fuel and battery
        if (Random.nextFloat() > 0.95f) currentFuel -= 1
        if (Random.nextFloat() > 0.98f) currentBattery -= 1
        
        currentFuel = currentFuel.coerceIn(0, 100)
        currentBattery = currentBattery.coerceIn(0, 100)

        // Update UI for fuel and battery
        fuelValue.text = getString(R.string.percent_format, currentFuel)
        fuelProgress.progress = currentFuel

        batteryValue.text = getString(R.string.percent_format, currentBattery)
        batteryProgress.progress = currentBattery
    }

    private fun animateSpeed(from: Int, to: Int) {
        speedAnimator?.cancel()
        speedAnimator = ValueAnimator.ofInt(from, to).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Int
                speedValue.text = getString(R.string.speed_format, animatedValue)
                speedometer.progress = animatedValue
            }
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(mockUpdateRunnable)
        speedAnimator?.cancel()
    }
}
