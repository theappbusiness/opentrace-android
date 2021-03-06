package com.kccreate.intelligentlockdown.healthmonitor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class TrackerQuestions(
    val thresholdSeverity1: Int,
    val thresholdSeverity2: Int,
    val thresholdContact: Int,
    val questions: List<Question>
)

data class Question(
    val text: String,
    val yesScore: Int = 0,
    val noScore: Int = 0,
    val answer: Answer = Answer.Unknown
)

enum class Answer { Yes, No, Unknown }