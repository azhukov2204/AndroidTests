package com.geekbrains.tests

import android.content.Context
import android.os.Build
import android.os.Looper.getMainLooper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.view.search.MainActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    private val mainActivityScenario: ActivityScenario<MainActivity> by lazy { ActivityScenario.launch(MainActivity::class.java) }
    private val context: Context by lazy { ApplicationProvider.getApplicationContext() }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun close() {
        mainActivityScenario.close()
    }

    @Test
    fun activity_AssertNotNull() {
        mainActivityScenario.onActivity {
            assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, mainActivityScenario.state)
    }

    @Test
    fun activityProgressBar_IsGone() {
        mainActivityScenario.onActivity {
            assertEquals(View.GONE, it.findViewById<ProgressBar>(R.id.progressBar).visibility)
        }
    }

    @Test
    fun activityEditText_IsVisible() {
        mainActivityScenario.onActivity {
            assertEquals(View.VISIBLE, it.findViewById<EditText>(R.id.searchEditText).visibility)
        }
    }

    @Test
    fun activityButton_IsVisible() {
        mainActivityScenario.onActivity {
            assertEquals(View.VISIBLE, it.findViewById<Button>(R.id.toDetailsActivityButton).visibility)
        }
    }

    @Test
    fun searchEditText_SetTextTest() {
        val queryString = "android"
        mainActivityScenario.onActivity {
            it.findViewById<EditText>(R.id.searchEditText).let { searchEditText ->
                searchEditText.setText(queryString, TextView.BufferType.EDITABLE)
                assertEquals(queryString, searchEditText.text.toString())
            }
        }
    }

    @Test
    fun searchEditText_SetText_checkProgressBarVisibility() {
        val queryString = "android"
        mainActivityScenario.onActivity {
            it.findViewById<EditText>(R.id.searchEditText).apply {
                setText(queryString, TextView.BufferType.EDITABLE)
                onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            }
            shadowOf(getMainLooper()).idle()
            assertEquals(View.VISIBLE, it.findViewById<ProgressBar>(R.id.progressBar).visibility)
        }
    }

    @Test
    fun searchEditText_SetBlankText_checkToast() {
        val blankQueryString = ""
        mainActivityScenario.onActivity {
            it.findViewById<EditText>(R.id.searchEditText).apply {
                setText(blankQueryString, TextView.BufferType.EDITABLE)
                onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            }
            shadowOf(getMainLooper()).idle()
            assertEquals(context.getString(R.string.enter_search_word), ShadowToast.getTextOfLatestToast())
        }
    }
}
