package com.gk.cvscodechallenge.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gk.cvscodechallenge.R
import com.gk.cvscodechallenge.repository.FlickrRepository
import com.gk.cvscodechallenge.repository.SearchStatus
import com.gk.cvscodechallenge.testsupport.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchFragmentTest {
    @get:Rule var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var mockFlickrRepository: FlickrRepository

    private lateinit var mockLoadStatusFlow: MutableStateFlow<SearchStatus>

    @Before
    fun setup() {
        hiltRule.inject()
        mockLoadStatusFlow = MutableStateFlow<SearchStatus>(SearchStatus.Ready())
        every { mockFlickrRepository.loadStatus } returns mockLoadStatusFlow.asStateFlow()
    }

    @Test
    fun forDevelopment(): Unit = runBlocking {
        launchFragmentInHiltContainer<SearchFragment>().use {
            val searchTermSpinner = onView(withId(R.id.search_term_text_view))
            searchTermSpinner
                .perform(click())
                .perform(typeText("search term"))

            delay(TimeUnit.SECONDS.toMillis(10))
        }
    }

    /**
     * Given: LoadStatus is [SearchStatus.Ready]
     * When: UI is visible
     * Then: Validate that the:
     * - empty state label is shown
     * - search result list is not visible
     * - progress indicator is hidden
     */
    @Test
    fun test_UI_in_Ready_state(): Unit = runBlocking {
        launchFragmentInHiltContainer<SearchFragment>().use {
            onView(withId(R.id.empty_state_label)).check(matches(
                isDisplayed()
            ))

            onView(withId(R.id.search_results_list)).check(matches(
                not(isDisplayed())
            ))

            onView(withId(R.id.loading_indicator)).check(matches(
                not(isDisplayed())
            ))

            onView(withId(R.id.search_button)).check(matches(isEnabled()))

            delay(TimeUnit.SECONDS.toMillis(10))
        }
    }

    /**
     * Given: LoadStatus is [SearchStatus.Loading]
     * When: UI is visible
     * Then: Validate that the:
     * - empty state label is NOT shown
     * - search result list is visible
     * - progress indicator is shown
     * - search button is NOT enabled
     */
    @Test
    fun test_UI_in_Loading_state(): Unit = runBlocking {
        mockLoadStatusFlow.value = SearchStatus.Loading("test term")

        launchFragmentInHiltContainer<SearchFragment>().use {
            onView(withId(R.id.empty_state_label)).check(matches(not(isDisplayed())))
            onView(withId(R.id.search_results_list)).check(matches(isDisplayed()))
            onView(withId(R.id.loading_indicator)).check(matches(isDisplayed()))
            onView(withId(R.id.search_button)).check(matches(not(isEnabled())))

            delay(TimeUnit.SECONDS.toMillis(10))
        }
    }

    /**
     * Given: LoadStatus is [SearchStatus.LoadError]
     * When: UI is visible
     * Then: Validate that the:
     * - an alert box is shown with the error message
     */
    @Test
    fun test_UI_in_LoadError_state(): Unit = runBlocking {
        mockLoadStatusFlow.value = SearchStatus.LoadError("Test Error Message")

        launchFragmentInHiltContainer<SearchFragment>().use {
            delay(TimeUnit.SECONDS.toMillis(20))
        }
    }
}