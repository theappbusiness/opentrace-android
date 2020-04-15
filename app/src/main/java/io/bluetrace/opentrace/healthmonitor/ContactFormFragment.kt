package io.bluetrace.opentrace.healthmonitor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.bluetrace.opentrace.R
import io.bluetrace.opentrace.extensions.bindView

class ContactFormFragment : Fragment(R.layout.fragment_contact_form) {

    companion object {
        fun newInstance() = ContactFormFragment()
    }

    private val viewModel: HealthMonitorViewModel by activityViewModels()

    private val submitButton by bindView<AppCompatButton>(R.id.submit)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitButton.setOnClickListener {
            Toast.makeText(requireContext(), "Thank you", Toast.LENGTH_LONG).show()
        }
    }
}