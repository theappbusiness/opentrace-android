package io.bluetrace.opentrace.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import io.bluetrace.opentrace.R
import io.bluetrace.opentrace.logging.CentralLog

class HowItWorksFragment : OnboardingFragmentInterface() {

    private var TAG: String = "HowItWorksFragment"

    override fun getButtonText(): String = "Continue"

    override fun becomesVisible() {}

    override fun onButtonClick(view: View) {
        authenticate()
    }

    private fun authenticate() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    CentralLog.i(TAG, "Sign in success")
                } else {
                    //TODO Error handling needed
                    CentralLog.d(TAG, "Sign in failure")
                }
                navigateToNextPage()
            }
    }

    private fun navigateToNextPage() {
        val onboardActivity = requireActivity() as OnboardingActivity
        onboardActivity.navigateToNextPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_howitworks, container, false)

    override fun getProgressValue(): Int = 25

    override fun onUpdatePhoneNumber(num: String) {}

    override fun onError(error: String) {}

}
