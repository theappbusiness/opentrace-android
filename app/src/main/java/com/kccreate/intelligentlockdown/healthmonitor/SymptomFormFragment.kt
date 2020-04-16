package com.kccreate.intelligentlockdown.healthmonitor

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.extensions.bindView
import com.kccreate.intelligentlockdown.healthmonitor.model.Answer
import com.kccreate.intelligentlockdown.healthmonitor.view.YesNoQuestionView

class SymptomFormFragment : Fragment(R.layout.fragment_symptom_form) {

    companion object {
        fun newInstance() = SymptomFormFragment()
    }

    private val viewModel: HealthMonitorViewModel by activityViewModels()

    private val questionsContainer by bindView<LinearLayout>(R.id.questions_container)
    private val submitButton by bindView<AppCompatButton>(R.id.submit)

    private val questions = mutableListOf<YesNoQuestionView>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.questions.forEach { question ->
            val questionView = YesNoQuestionView(requireContext()).apply {
                withQuestion(question)
            }
            questions.add(questionView)
            questionsContainer.addView(questionView)
        }

        submitButton.setOnClickListener {
            val answers =
                questions.map { it.getResult() }.filterNot { it.answer == Answer.Unknown }.toList()

            if (answers.size != questions.size) {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.txt_all_fields_mandatory))
                    .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create().show()
            } else {
                viewModel.submitAnswers(answers)
            }
        }
    }
}