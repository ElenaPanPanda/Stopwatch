package com.example.stopwatch

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.stopwatch.databinding.ActivityMainBinding
import java.util.Date
import kotlin.concurrent.thread
import kotlin.random.Random

private const val TIME_UPDATE = 1000L

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private var currentTime = 0L
    private val colorsList = ColorsList()
    private var colorIndex = 0
    private var timerShouldStop: Boolean = false
    private var thread: Thread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            binding.textView.text = savedInstanceState.getString("timerCount")
        }
    }

    override fun onStart() {
        super.onStart()

        binding.startButton.setOnClickListener {
            if (thread == null) {
                startTimer()
            }
        }

        binding.resetButton.setOnClickListener {
            stopTimer()
        }
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun startTimer() {
        timerShouldStop = false

        thread = thread {
            do {
                onTimerTick()
                Thread.sleep(TIME_UPDATE)
            } while (!timerShouldStop)
        }
    }

    private fun stopTimer() {
        timerShouldStop = true
        binding.textView.setText(R.string.startTime)
        currentTime = 0L
        thread = null
        binding.progressBar.visibility = View.GONE
    }

    private fun onTimerTick() {
        handler.post {
            binding.textView.text = formatMilliseconds(currentTime)
            currentTime += TIME_UPDATE

            binding.progressBar.visibility = View.VISIBLE

            val nextColor = colorsList[colorIndex]
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(nextColor)
            colorIndex = colorsList.getNextIndex(colorIndex)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("timerCount", binding.textView.text.toString())
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatMilliseconds(milliseconds: Long): String {
        //val millis = milliseconds % 1000
        val format = SimpleDateFormat("mm:ss")
        return format.format(Date(milliseconds))
        //+ ":$millis".padEnd(3, '0').take(3)
    }
}