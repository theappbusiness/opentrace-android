package com.kccreate.intelligentlockdown.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.extensions.bindView

class UploadSuccessFragment : Fragment(R.layout.fragment_upload_success) {

    private val successButton by bindView<AppCompatButton>(R.id.upload_success_button)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        successButton.setOnClickListener {
            navigateToUploadScreen()
        }
    }

    private fun navigateToUploadScreen() {
        parentFragmentManager.popBackStackImmediate()
        parentFragmentManager.beginTransaction()
            .replace(R.id.content, UploadFragment())
            .commit()
    }
}
