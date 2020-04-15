package com.kccreate.intelligentlockdown.healthmonitor

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.extensions.bindView

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