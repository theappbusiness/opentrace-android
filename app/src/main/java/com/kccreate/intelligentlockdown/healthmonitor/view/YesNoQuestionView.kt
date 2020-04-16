package com.kccreate.intelligentlockdown.healthmonitor.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.healthmonitor.model.Answer
import com.kccreate.intelligentlockdown.healthmonitor.model.Question

class YesNoQuestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_yesno_question, this, true)

        orientation = VERTICAL
    }

    private val questionTextView: TextView by lazy { findViewById<TextView>(R.id.text) }
    private val yesOption: AppCompatRadioButton by lazy { findViewById<AppCompatRadioButton>(R.id.yes) }
    private val noOption: AppCompatRadioButton by lazy { findViewById<AppCompatRadioButton>(R.id.no) }

    private lateinit var question: Question

    fun withQuestion(question: Question) {
        this.question = question
        questionTextView.text = question.text
    }

    fun getAnswer() = when {
        yesOption.isChecked -> question.copy(answer = Answer.Yes)
        noOption.isChecked -> question.copy(answer = Answer.No)
        else -> question.copy(answer = Answer.Unknown)
    }
}