package io.bluetrace.opentrace.healthmonitor

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import io.bluetrace.opentrace.R
import io.bluetrace.opentrace.extensions.bindView

class OkGreatFragment : Fragment(R.layout.fragment_ok_great) {

    companion object {
        fun newInstance() = OkGreatFragment()
    }

    private val okButton by bindView<AppCompatButton>(R.id.ok)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        okButton.setOnClickListener {
            requireActivity().finish()
        }
    }
}