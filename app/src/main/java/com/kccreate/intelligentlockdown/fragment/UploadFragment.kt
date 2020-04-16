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

class UploadFragment : Fragment(R.layout.fragment_upload) {

    private val uploadButton by bindView<AppCompatButton>(R.id.upload_button)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadButton.setOnClickListener {
            navigateToVerifyCallerScreen()
        }
    }

    private fun navigateToVerifyCallerScreen() {
        parentFragmentManager.beginTransaction()
            .add(R.id.content, EnterPinFragment())
            .addToBackStack("EnterPin")
            .commit()
    }
}
