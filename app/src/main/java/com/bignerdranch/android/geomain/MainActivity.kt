package com.bignerdranch.android.geomain

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button

    private lateinit var questionTextView: TextView
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton

    private lateinit var soundBox: SoundBox

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundBox = SoundBox(assets)


        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)

        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        quizViewModel.currentIndex = currentIndex
        blockCheatButton()

        cheatButton.setOnClickListener {view ->
        // Начало CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                //val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            blockButton(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            blockButton(true)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            soundBox.play(soundBox.sounds[0])
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        updateQuestion()

    }

    //вызывается при закрытии дочерней activity
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
//            quizViewModel.isCheater =
//                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false

            if (data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) == true) quizViewModel.currentQuestionCheat()
        }
        blockCheatButton()
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        Log.d(TAG, "Updating question text", Exception())

        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        blockButton(quizViewModel.currentQuestionIsAnswered())
    }

    private fun blockCheatButton() {
        if (quizViewModel.cheatIsEnd()) cheatButton.isEnabled = false
    }

    private fun blockButton(block: Boolean) {
        trueButton.isEnabled = !block
        falseButton.isEnabled = !block
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer


        val messageResId = when {
            quizViewModel.currentQuestionIsCheat() -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.trueAnswerAdd()
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }


        quizViewModel.currentQuestionAnswered()
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (quizViewModel.questionsEnd())
            Toast.makeText(this, quizViewModel.testResult(), Toast.LENGTH_SHORT)
                .show()
    }

}