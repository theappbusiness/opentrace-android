package io.bluetrace.opentrace.healthmonitor

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import io.bluetrace.opentrace.R
import io.bluetrace.opentrace.extensions.bindView

class HowDoYouFeelFragment : Fragment(R.layout.fragment_how_do_you_feel) {

    companion object {
        fun newInstance() = HowDoYouFeelFragment()
    }

    private val feelingGoodButton by bindView<AppCompatButton>(R.id.feeling_good)
    private val notFeelingGoodButton by bindView<AppCompatButton>(R.id.not_feeling_good)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feelingGoodButton.setOnClickListener {
            (requireActivity() as HealthMonitorActivity).onFeelingGoodClicked()
        }

        notFeelingGoodButton.setOnClickListener {
            (requireActivity() as HealthMonitorActivity).onNotFeelingGoodClicked()
        }
    }
}