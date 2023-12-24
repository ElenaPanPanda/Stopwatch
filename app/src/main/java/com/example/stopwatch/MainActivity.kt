package com.example.stopwatch

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.example.stopwatch.databinding.ActivityMainBinding
import java.util.Date
import kotlin.concurrent.thread

private const val TIME_UPDATE = 1000L

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val handler = Handler(Looper.getMainLooper())
    private var currentTime = 0L
    private val colorsList = ColorsList()
    private var colorIndex = 0
    private var upperLimitSeconds = ""
    private var timerShouldStop: Boolean = false
    private var thread: Thread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            binding.textView.text = savedInstanceState.getString("timerCount")
        }


        binding.settingsButton.setOnClickListener {
            val contentView =
                LayoutInflater.from(this).inflate(R.layout.alert_dialog_settings, null, false)

            AlertDialog.Builder(this)
                .setTitle(R.string.set_upper_limit)
                .setView(contentView)
                .setPositiveButton(R.string.ok) { _, _ ->
                    val editText = contentView.findViewById<EditText>(R.id.upperLimitEditText)
                    upperLimitSeconds = editText.text.toString()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
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
        binding.settingsButton.isEnabled = false

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
        binding.settingsButton.isEnabled = true
        binding.textView.setTextColor(Color.BLACK)
    }

    private fun onTimerTick() {
        handler.post {
            binding.textView.text = formatMilliseconds(currentTime)
            currentTime += TIME_UPDATE

            binding.progressBar.visibility = View.VISIBLE

            val nextColor = colorsList[colorIndex]
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(nextColor)
            colorIndex = colorsList.getNextIndex(colorIndex)

            if (upperLimitSeconds != "") {
                if (currentTime > upperLimitSeconds.toInt() * TIME_UPDATE + TIME_UPDATE) {
                    binding.textView.setTextColor(Color.RED)
                }
            }

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