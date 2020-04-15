package io.bluetrace.opentrace.healthmonitor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.bluetrace.opentrace.R

class SymptomFormFragment : Fragment(R.layout.fragment_symptom_form) {

    companion object {
        fun newInstance() = SymptomFormFragment()
    }

    private val viewModel: HealthMonitorViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: create questions dynamically
    }
}