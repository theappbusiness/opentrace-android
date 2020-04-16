package com.kccreate.intelligentlockdown.healthmonitor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.healthmonitor.model.Answer.*
import com.kccreate.intelligentlockdown.healthmonitor.model.Question
import com.kccreate.intelligentlockdown.healthmonitor.model.TrackerQuestions
import java.io.BufferedReader

enum class SymptomsResult { Severity1, Severity2, ContactForm }

class HealthMonitorViewModel(private val app: Application) : AndroidViewModel(app) {


    private val trackerQuestions = readJson()

    val questions: List<Question>
        get() = trackerQuestions.questions

    private var answers: List<Question> = emptyList()

    val symptomsResult = MutableLiveData<SymptomsResult>()

    private fun readJson(): TrackerQuestions {
        val stream = app.resources.openRawResource(R.raw.questions)
        val json = stream.bufferedReader().use(BufferedReader::readText)
        val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.fromJson(json, TrackerQuestions::class.java)
    }

    fun submitAnswers(answers: List<Question>) {
        this.answers = answers
        val score = calculateScore()

        symptomsResult.value = when {
            score >= trackerQuestions.thresholdContact -> SymptomsResult.ContactForm
            score >= trackerQuestions.thresholdSeverity2 -> SymptomsResult.Severity2
            else -> SymptomsResult.Severity1
        }
    }

    private fun calculateScore() = this.answers.fold(0, { total, question ->
        total + when (question.answer) {
            Yes -> question.yesScore
            No -> question.noScore
            Unknown -> 0
        }
    })
}