package com.kccreate.intelligentlockdown.healthmonitor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kccreate.intelligentlockdown.R
import io.bluetrace.opentrace.extensions.observe
import io.bluetrace.opentrace.healthmonitor.HealthMonitorViewModel
import io.bluetrace.opentrace.healthmonitor.HowDoYouFeelFragment
import io.bluetrace.opentrace.healthmonitor.OkGreatFragment
import io.bluetrace.opentrace.healthmonitor.QuestionFragment
import io.bluetrace.opentrace.healthmonitor.model.Question

class HealthMonitorActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, HealthMonitorActivity::class.java)
    }

    private val viewModel: HealthMonitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_healthmonitor)

        observe(viewModel.finished, ::finished)

        replaceFragment(HowDoYouFeelFragment.newInstance())
    }

    fun onFeelingGoodClicked() {
        replaceFragment(OkGreatFragment.newInstance())
    }

    fun onNotFeelingGoodClicked() {
//        observe(viewModel.currentQuestion, ::showCurrentQuestion)
        replaceFragment(ContactFormFragment.newInstance())
    }

    private fun finished(finished: Boolean) {
        if (finished) {
            val score = viewModel.score.value ?: 0

            val help = when (score) {
                in 0..10 -> "Lockdown Procedures"
                in 10..25 -> "Severity 1 tips"
                in 25..100 -> "Severity 2 tips"
                else -> "Contact Form"
            }

            AlertDialog.Builder(this)
                .setTitle("Self-tracking result")
                .setMessage("Your score is: $score.\nYour suggested help is: $help")
                .create()
                .show()
        }
    }

    private fun showCurrentQuestion(question: Question) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, QuestionFragment.newInstance(question))
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}

