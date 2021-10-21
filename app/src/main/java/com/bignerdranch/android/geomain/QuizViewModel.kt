package com.bignerdranch.android.geomain

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    var currentIndex = 0
    //var isCheater = false
    private var trueAnswers = 0
    private var answersIndexList = mutableListOf<Int>()
    private var cheatingIndexList = mutableListOf<Int>()


    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev() {
        currentIndex = if (currentIndex % questionBank.size == 0)
            questionBank.size - 1
        else
            (currentIndex - 1) % questionBank.size
    }
    fun currentQuestionCheat() = cheatingIndexList.add(currentIndex)
    fun currentQuestionAnswered() = answersIndexList.add(currentIndex)
    fun currentQuestionIsAnswered() = answersIndexList.contains(currentIndex)
    fun currentQuestionIsCheat() = cheatingIndexList.contains(currentIndex)
    fun questionsEnd() = answersIndexList.count() == questionBank.count()
    fun trueAnswerAdd() = trueAnswers++
    fun testResult() = "Result: ${trueAnswers}/${questionBank.count()}. You cheat ${cheatingIndexList.count()} once."
    fun cheatIsEnd() = cheatingIndexList.count() > 2
}
