package io.bluetrace.opentrace.healthmonitor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.bluetrace.opentrace.R
import io.bluetrace.opentrace.healthmonitor.model.TrackerQuestions
import java.io.BufferedReader

class HealthMonitorViewModel(private val app: Application) : AndroidViewModel(app) {

    private val trackerQuestions = readJson()

    private val iterator = trackerQuestions.questions.iterator()

    val currentQuestion = MutableLiveData(iterator.next())

    val score = MutableLiveData(0)

    val finished = MutableLiveData(false)

    fun answer(points: Int) {
        score.value = score.value?.plus(points)

        if (iterator.hasNext()) {
            currentQuestion.value = iterator.next()
        } else {
            finished.value = true
        }
    }

    private fun readJson(): TrackerQuestions {
        val stream = app.resources.openRawResource(R.raw.questions)
        val json = stream.bufferedReader().use(BufferedReader::readText)
        val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
        return gson.fromJson(json, TrackerQuestions::class.java)
    }
}