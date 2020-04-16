package com.kccreate.intelligentlockdown.healthmonitor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.extensions.observe
import com.kccreate.intelligentlockdown.healthmonitor.SymptomsResult.*
import com.kccreate.intelligentlockdown.healthmonitor.model.Question

class HealthMonitorActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, HealthMonitorActivity::class.java)
    }

    private val viewModel: HealthMonitorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_healthmonitor)

        observe(viewModel.symptomsResult, ::onSymptomsResult)

        replaceFragment(HowDoYouFeelFragment.newInstance())
    }

    private fun onSymptomsResult(symptomsResult: SymptomsResult) {
        when(symptomsResult){
            LockDown -> replaceFragment(OkGreatFragment.newInstance()) //FIXME Maybe?
            Severity1 -> replaceFragment(TipsSeverity1Fragment.newInstance())
            Severity2 -> replaceFragment(TipsSeverity2Fragment.newInstance())
            ContactForm -> replaceFragment(ContactFormFragment.newInstance())
        }
    }

    fun onFeelingGoodClicked() {
        replaceFragment(OkGreatFragment.newInstance())
    }

    fun onNotFeelingGoodClicked() {
        replaceFragment(SymptomFormFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}

