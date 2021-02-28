package com.example.android.navigation

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



data class Question(
        val questionId: Int,
        val answer: Boolean,
        var attempted: Boolean = false,
        var userAnswer: Boolean = false
)
class GameViewModel : ViewModel() {
    private var questionIndex = 0
    private lateinit var questionBank: MutableList<Question>

    var questionAmount = 0

    var userScore = 0

    private val _scoreS = MutableLiveData<Int>()
    val scoreString: LiveData<Int>
        get() = _scoreS

    private val _question = MutableLiveData<String>()
    val questionString: LiveData<String>
        get() = _question

    private val _attempted = MutableLiveData<Boolean>()
    val attempted: LiveData<Boolean>
        get() = _attempted

    private val _isCorrect = MutableLiveData<Boolean>()
    val isCorrect: LiveData<Boolean>
        get() = _isCorrect

    private val _actualAnswer = MutableLiveData<Boolean>()
    val actualAnswer: LiveData<Boolean>
        get() = _actualAnswer

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGamefinished: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        _question.value = ""
        newGame()
    }


    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun questionsAttempted(): Int {
        return questionBank.count { it.attempted }
    }

    private fun newGame() {
        resetQuestionBank()
        questionAmount = questionBank.count()
        questionIndex = 0
        _eventGameFinish.value = false
        updateQuestion()

    }

    private fun updateQuestion() {
        _question.value = questionBank[questionIndex].questionId.toString()
        _attempted.value = questionBank[questionIndex].attempted

        _isCorrect.value = questionBank[questionIndex].userAnswer
        _actualAnswer.value = questionBank[questionIndex].answer

    }

    private fun resetQuestionBank() {
        questionBank = mutableListOf(
                Question(R.string.question_1, false),
                Question(R.string.question_2, true),
                Question(R.string.question_3, true),
                Question(R.string.question_4, false),
                Question(R.string.question_5, false),
                Question(R.string.question_6, true),
                Question(R.string.question_7, false),
                Question(R.string.question_8, true),
                Question(R.string.question_9, false),
                Question(R.string.question_10, false),
                Question(R.string.question_11, false),
                Question(R.string.question_12, true),
                Question(R.string.question_13, false),
                Question(R.string.question_14, true),
                Question(R.string.question_15, false),
                Question(R.string.question_16, false),
                Question(R.string.question_17, true),
                Question(R.string.question_18, false),
                Question(R.string.question_19, false),
                Question(R.string.question_20, true)
        )

        questionBank.shuffle()
    }

    fun nextQuestion() {
        questionIndex++;
        if (questionIndex >= questionBank.size) {
            questionIndex = 0;
        }
        updateQuestion();
    }

    fun prevQuestion() {
        questionIndex--;
        if (questionIndex < 0) {
            questionIndex = questionBank.size - 1;
        }
        updateQuestion();
    }

    fun registerAnswer(result: Boolean) {
        questionBank[questionIndex].attempted = true
        val questionAnswer = questionBank[questionIndex].answer
        questionBank[questionIndex].userAnswer = questionAnswer == result
        updateQuestion()

        if (questionBank[questionIndex].userAnswer) {
            userScore += 1
        }
    }

    fun isAllQuestionsAttempted(): Boolean {
        return questionsAttempted() == questionBank.size
    }

    override fun onCleared() {
        super.onCleared()

    }

}