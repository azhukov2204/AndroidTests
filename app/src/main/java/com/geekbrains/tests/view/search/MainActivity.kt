package com.geekbrains.tests.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.tests.R
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.presenter.search.ScreenState
import com.geekbrains.tests.presenter.search.SearchViewModel
import com.geekbrains.tests.view.details.DetailsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity(), ViewSearchContract {

    private val adapter = SearchResultAdapter()
    private var totalCount: Int = 0

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUI()
        searchViewModel.subscribeToLiveData().observe(this) { onStateChange(it) }
    }

    private fun setUI() {
        toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun setQueryListener() {
        searchTextInputLayout.setEndIconOnClickListener { doSearch() }

        searchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@OnEditorActionListener doSearch()
            }
            false
        })
    }

    private fun doSearch(): Boolean {
        val query = searchEditText.text.toString()
        return if (query.isNotBlank()) {
            searchViewModel.searchGitHub(query)
            true
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.enter_search_word),
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    override fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    ) {
        with(totalCountTextView) {
            visibility = View.VISIBLE
            text =
                String.format(Locale.getDefault(), getString(R.string.results_count), totalCount)
        }

        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun onStateChange(screenState: ScreenState) {
        when (screenState) {
            is ScreenState.Working -> {
                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                progressBar.visibility = View.GONE
                with(totalCountTextView) {
                    visibility = View.VISIBLE
                    text =
                        String.format(
                            Locale.getDefault(),
                            getString(R.string.results_count),
                            totalCount
                        )
                }

                this.totalCount = totalCount!!
                adapter.updateResults(searchResponse.searchResults!!)
            }
            is ScreenState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
