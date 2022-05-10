package com.gk.cvscodechallenge.testsupport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gk.cvscodechallenge.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(intent.getIntExtra("androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            R.style.Theme_CVSCodeChallenge))
        super.onCreate(savedInstanceState)
    }
}