package com.geekbrains.tests

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTotalCountTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTotalCountTextView_HasText() {
        val assertion = matches(withText(context.getString(R.string.results_count)))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun activityTotalCountTextView_isInvisible() {
        val assertion = matches(withEffectiveVisibility(Visibility.INVISIBLE))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun activityToDetailsActivityButton_NotNull() {
        scenario.onActivity {
            val toDetailsActivityButton = it.findViewById<Button>(R.id.toDetailsActivityButton)
            TestCase.assertNotNull(toDetailsActivityButton)
        }
    }

    @Test
    fun activityToDetailsActivityButton_HasText() {
        val assertion = matches(withText(context.getString(R.string.to_details)))
        onView(withId(R.id.toDetailsActivityButton)).check(assertion)
    }

    @Test
    fun activityToDetailsActivityButton_isClickable() {
        val assertion = matches(isClickable())
        onView(withId(R.id.toDetailsActivityButton)).check(assertion)
    }

    @Test
    fun activityProgressBar_NotNull() {
        scenario.onActivity {
            val progressBar = it.findViewById<ProgressBar>(R.id.progressBar)
            TestCase.assertNotNull(progressBar)
        }
    }

    @Test
    fun activityProgressBar_isGone() {
        val assertion = matches(withEffectiveVisibility(Visibility.GONE))
        onView(withId(R.id.progressBar)).check(assertion)
    }

    @Test
    fun activitySearch_IsNotNull() {
        scenario.onActivity {
            val searchEditText = it.findViewById<TextView>(R.id.searchEditText)
            TestCase.assertNotNull(searchEditText)
        }
    }

    @Test
    fun activitySearch_IsCompletelyDisplayed() {
        val assertion = matches(isCompletelyDisplayed())
        onView(withId(R.id.searchEditText)).check(assertion)
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        onView(isRoot()).perform(delay())
        onView(withId(R.id.totalCountTextView)).check(matches(withText("Number of results: 2655")))
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}
