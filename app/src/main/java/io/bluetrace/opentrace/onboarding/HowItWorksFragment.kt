package io.bluetrace.opentrace.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.bluetrace.opentrace.R

class HowItWorksFragment : OnboardingFragmentInterface() {

    override fun getButtonText(): String = "Continue"

    override fun becomesVisible() {}

    override fun onButtonClick(view: View) {
        val onboardActivity = context as OnboardingActivity
        onboardActivity.navigateToNextPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_howitworks, container, false)

    override fun getProgressValue(): Int = 0

    override fun onUpdatePhoneNumber(num: String) {}

    override fun onError(error: String) {}

}
