package com.kccreate.intelligentlockdown.healthmonitor

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.extensions.bindArgument
import com.kccreate.intelligentlockdown.extensions.bindView
import com.kccreate.intelligentlockdown.extensions.withArguments
import com.kccreate.intelligentlockdown.healthmonitor.model.Question

class QuestionFragment : Fragment(R.layout.fragment_question) {

    companion object {

        private const val ARG_QUESTION = "arg_question"

        fun newInstance(question: Question) =
            QuestionFragment().withArguments(ARG_QUESTION to question)
    }

    private val viewModel: HealthMonitorViewModel by activityViewModels()

    private val question by bindArgument<Question>(ARG_QUESTION)

    private val titleTextView by bindView<TextView>(R.id.title)
    private val yesButton by bindView<Button>(R.id.yes)
    private val noButton by bindView<Button>(R.id.no)
    private val scoreTextView by bindView<TextView>(R.id.score)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.score.observe(viewLifecycleOwner, Observer {
            scoreTextView.text = "$it points"
        })

        titleTextView.text = question.text

        yesButton.setOnClickListener { viewModel.answer(question.yesScore) }
        noButton.setOnClickListener { viewModel.answer(question.noScore) }
    }
}