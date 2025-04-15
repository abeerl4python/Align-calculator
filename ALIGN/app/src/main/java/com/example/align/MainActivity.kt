package com.example.align

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // Goal details
    private var goalTitle = "New MacBook Pro"
    private var goalAmount = 12000.0
    private var currentAmount = 4500.0
    private var deadlineDate: Date? = null
    private val calendar = Calendar.getInstance()

    // UI elements
    private lateinit var tvGoalTitle: TextView
    private lateinit var tvGoalAmount: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvDeadline: TextView
    private lateinit var tvCurrentAmount: TextView
    private lateinit var tvRemainingAmount: TextView
    private lateinit var btnAddSavings: Button
    private lateinit var btnInfo: ImageButton
    private lateinit var btnPlus: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var ivProgressCircle: ProgressBar
    private lateinit var linearProgressBar: ProgressBar

    // Mood tracking elements
    private lateinit var btnMotivated: Button
    private lateinit var btnHappy: Button
    private lateinit var btnTired: Button
    private lateinit var btnStressed: Button
    private lateinit var btnNeutral: Button
    private lateinit var etMoodNote: EditText
    private lateinit var btnSaveMood: Button
    private lateinit var tvMotivationText: TextView

    // Currently selected mood
    private var currentMood = "Motivated"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set initial deadline to next year
        calendar.add(Calendar.MONTH, 3)
        deadlineDate = calendar.time

        // Initialize all views
        findAllViews()

        // Initialize views with data
        initializeViews()

        // Set up all click listeners
        setupClickListeners()

        // Apply custom progress drawable to circle progress
        setupCircleProgressDrawable()
    }

    private fun findAllViews() {
        // Goal card views
        tvGoalTitle = findViewById(R.id.tvGoalTitle)
        tvGoalAmount = findViewById(R.id.tvGoalAmount)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvDeadline = findViewById(R.id.tvDeadline)
        tvCurrentAmount = findViewById(R.id.tvCurrentAmount)
        tvRemainingAmount = findViewById(R.id.tvRemainingAmount)
        btnAddSavings = findViewById(R.id.btnAddSavings)
        btnInfo = findViewById(R.id.btnInfo)
        btnPlus = findViewById(R.id.btnPlus)
        btnDelete = findViewById(R.id.btnDelete)
        ivProgressCircle = findViewById(R.id.ivProgressCircle)
        linearProgressBar = findViewById(R.id.linearProgressBar)

        // Mood tracking views
        btnMotivated = findViewById(R.id.btnMotivated)
        btnHappy = findViewById(R.id.btnHappy)
        btnTired = findViewById(R.id.btnTired)
        btnStressed = findViewById(R.id.btnStressed)
        btnNeutral = findViewById(R.id.btnNeutral)
        etMoodNote = findViewById(R.id.etMoodNote)
        btnSaveMood = findViewById(R.id.btnSaveMood)
        tvMotivationText = findViewById(R.id.tvMotivationText)
    }

    private fun setupCircleProgressDrawable() {
        // Apply the custom progress drawable for the circle progress bar
        // This ensures we have a pink progress over a light purple background
        ivProgressCircle.progressDrawable = ContextCompat.getDrawable(this, R.drawable.circular_progress)
    }

    private fun initializeViews() {
        // Set goal information
        tvGoalTitle.text = goalTitle
        tvGoalAmount.text = "Goal: AED ${goalAmount.toInt()}"

        // Calculate remaining amount
        val remainingAmount = goalAmount - currentAmount
        tvCurrentAmount.text = "AED ${currentAmount.toInt()}"
        tvRemainingAmount.text = "AED ${remainingAmount.toInt()}"

        // Calculate and display percentage
        val percentage = ((currentAmount / goalAmount) * 100).roundToInt()
        tvPercentage.text = "$percentage%"

        // Update progress bars
        linearProgressBar.progress = percentage
        ivProgressCircle.progress = percentage

        // Update deadline display
        updateDeadlineDisplay()

        // Set initial mood
        updateMoodSelection("Motivated")
    }

    private fun updateDeadlineDisplay() {
        deadlineDate?.let { deadline ->
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val formattedDate = sdf.format(deadline)

            // Calculate days left
            val today = Date()
            val diffInMillis = deadline.time - today.time
            val daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            tvDeadline.text = "Deadline: $formattedDate ($daysLeft days left)"
        }
    }

    private fun setupClickListeners() {
        // Add savings button
        btnAddSavings.setOnClickListener {
            showAddSavingsDialog()
        }

        // Info button
        btnInfo.setOnClickListener {
            showInfoDialog()
        }

        // Plus/Add goal button
        btnPlus.setOnClickListener {
            showAddGoalDialog()
        }

        // Delete button
        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Mood buttons
        btnMotivated.setOnClickListener { updateMoodSelection("Motivated") }
        btnHappy.setOnClickListener { updateMoodSelection("Happy") }
        btnTired.setOnClickListener { updateMoodSelection("Tired") }
        btnStressed.setOnClickListener { updateMoodSelection("Stressed") }
        btnNeutral.setOnClickListener { updateMoodSelection("Neutral") }

        // Save mood button
        btnSaveMood.setOnClickListener {
            saveMoodEntry()
        }
    }

    private fun updateMoodSelection(mood: String) {
        // Reset all buttons to default style
        btnMotivated.setBackgroundResource(R.drawable.btn_unselected)
        btnMotivated.setTextColor(resources.getColor(R.color.white))

        btnHappy.setBackgroundResource(R.drawable.btn_unselected)
        btnHappy.setTextColor(resources.getColor(R.color.white))

        btnTired.setBackgroundResource(R.drawable.btn_unselected)
        btnTired.setTextColor(resources.getColor(R.color.white))

        btnStressed.setBackgroundResource(R.drawable.btn_unselected)
        btnStressed.setTextColor(resources.getColor(R.color.white))

        btnNeutral.setBackgroundResource(R.drawable.btn_unselected)
        btnNeutral.setTextColor(resources.getColor(R.color.white))

        // Set selected button style
        when (mood) {
            "Motivated" -> {
                btnMotivated.setBackgroundResource(R.drawable.btn_gradient)
                btnMotivated.setTextColor(resources.getColor(R.color.white))
            }
            "Happy" -> {
                btnHappy.setBackgroundResource(R.drawable.btn_gradient)
                btnHappy.setTextColor(resources.getColor(R.color.white))
            }
            "Tired" -> {
                btnTired.setBackgroundResource(R.drawable.btn_gradient)
                btnTired.setTextColor(resources.getColor(R.color.white))
            }
            "Stressed" -> {
                btnStressed.setBackgroundResource(R.drawable.btn_gradient)
                btnStressed.setTextColor(resources.getColor(R.color.white))
            }
            "Neutral" -> {
                btnNeutral.setBackgroundResource(R.drawable.btn_gradient)
                btnNeutral.setTextColor(resources.getColor(R.color.white))
            }
        }

        currentMood = mood
    }

    private fun saveMoodEntry() {
        val note = etMoodNote.text.toString()

        // In a real app, save this to a database
        // For now, just show a confirmation toast
        Toast.makeText(
            this,
            "Mood saved: $currentMood${if (note.isNotEmpty()) " with note" else ""}",
            Toast.LENGTH_SHORT
        ).show()

        // Clear the note field
        etMoodNote.setText("")

        // Update daily motivation based on mood
        updateMotivationMessage(currentMood)
    }

    private fun updateMotivationMessage(mood: String) {
        val message = when (mood) {
            "Motivated" -> "That's the spirit! Your energy is contagious! Keep pushing toward your goals! ✨"
            "Happy" -> "Wonderful! A positive mindset makes everything easier. You're doing great! 😊"
            "Tired" -> "It's okay to rest. Take care of yourself today, and you'll be stronger tomorrow! 💤"
            "Stressed" -> "Take a deep breath. One step at a time. You've got this! 🧘"
            "Neutral" -> "Every day is a new opportunity. What small step can you take toward your goal today? 🌱"
            else -> "Keep that energy up! Your determination is getting you closer to your goal every day! ✨"
        }

        tvMotivationText.text = message
    }

    private fun showAddSavingsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_savings, null)
        val etAmount = dialogView.findViewById<EditText>(R.id.etSavingsAmount)

        AlertDialog.Builder(this)
            .setTitle("Add Savings")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val amountText = etAmount.text.toString()
                if (amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        addSavings(amount)
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addSavings(amount: Double) {
        // Update current amount
        currentAmount += amount

        // Ensure we don't exceed the goal
        if (currentAmount > goalAmount) {
            currentAmount = goalAmount
        }

        // Update UI
        val remainingAmount = goalAmount - currentAmount
        tvCurrentAmount.text = "AED ${currentAmount.toInt()}"
        tvRemainingAmount.text = "AED ${remainingAmount.toInt()}"

        // Update progress
        val percentage = ((currentAmount / goalAmount) * 100).roundToInt()
        tvPercentage.text = "$percentage%"
        linearProgressBar.progress = percentage
        ivProgressCircle.progress = percentage

        // Show a toast
        Toast.makeText(this, "Added AED ${amount.toInt()} to savings!", Toast.LENGTH_SHORT).show()

        // Check if goal is complete
        if (currentAmount >= goalAmount) {
            showGoalCompletedDialog()
        }
    }

    private fun showGoalCompletedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Congratulations! 🎉")
            .setMessage("You've reached your savings goal for $goalTitle!")
            .setPositiveButton("Set New Goal") { _, _ ->
                showAddGoalDialog()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle("About Align")
            .setMessage("Align helps you track your savings goals and daily mood to stay motivated and focused on your financial objectives. Track your progress, record how you feel, and stay aligned with your goals!")
            .setPositiveButton("Close", null)
            .show()
    }

    private fun showAddGoalDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_goal, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etGoalTitle)
        val etAmount = dialogView.findViewById<EditText>(R.id.etGoalAmount)
        val etDeadline = dialogView.findViewById<EditText>(R.id.etGoalDeadline)

        // Set default values
        etTitle.setText(goalTitle)
        etAmount.setText(goalAmount.toInt().toString())

        // Make the deadline field open date picker on click
        etDeadline.setOnClickListener {
            showDatePickerDialog(etDeadline)
        }

        AlertDialog.Builder(this)
            .setTitle("Set Savings Goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = etTitle.text.toString()
                val amountText = etAmount.text.toString()

                if (title.isNotEmpty() && amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        setNewGoal(title, amount)
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDatePickerDialog(etDeadline: EditText) {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            etDeadline.setText(sdf.format(calendar.time))
            deadlineDate = calendar.time
        }

        DatePickerDialog(
            this,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setNewGoal(title: String, amount: Double) {
        // Reset goal values
        goalTitle = title
        goalAmount = amount
        currentAmount = 0.0

        // Update UI
        tvGoalTitle.text = goalTitle
        tvGoalAmount.text = "Goal: AED ${goalAmount.toInt()}"
        tvCurrentAmount.text = "AED 0"
        tvRemainingAmount.text = "AED ${goalAmount.toInt()}"
        tvPercentage.text = "0%"
        linearProgressBar.progress = 0
        ivProgressCircle.progress = 0

        // Update deadline display
        updateDeadlineDisplay()

        Toast.makeText(this, "New goal set: $goalTitle", Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Goal")
            .setMessage("Are you sure you want to delete this savings goal?")
            .setPositiveButton("Delete") { _, _ ->
                // In a real app, this would delete from database
                // For now, just reset the UI
                setNewGoal("New Goal", 10000.0)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}