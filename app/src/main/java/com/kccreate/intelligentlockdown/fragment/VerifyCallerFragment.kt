package com.kccreate.intelligentlockdown.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kccreate.intelligentlockdown.Preference
import kotlinx.android.synthetic.main.fragment_upload_verifycaller.*
import com.kccreate.intelligentlockdown.R

class VerifyCallerFragment : Fragment(R.layout.fragment_upload_verifycaller) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handShakePin = Preference.getHandShakePin(view.context)
        verifyCallerFragmentVerificationCode.text = handShakePin
        verifyCallerFragmentActionButton.setOnClickListener {
            val myParentFragment: UploadPageFragment = (parentFragment as UploadPageFragment)
            myParentFragment.navigateToUploadPin()
        }
    }
}
