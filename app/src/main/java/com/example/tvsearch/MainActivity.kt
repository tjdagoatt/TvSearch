package com.example.tvsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etQuery: EditText
    private lateinit var btnSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var showsAdapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etQuery = findViewById(R.id.etQuery)
        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerViewShows)

        recyclerView.layoutManager = LinearLayoutManager(this)
        showsAdapter = ShowsAdapter(mutableListOf())
        recyclerView.adapter = showsAdapter

        btnSearch.setOnClickListener {
            startSearch()
        }

        etQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startSearch()
                true
            } else {
                false
            }
        }
    }

    private fun startSearch() {
        val query = etQuery.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE

        val call = RetrofitClient.api.searchShows(query)
        call.enqueue(object : Callback<List<SearchResult>> {
            override fun onResponse(
                call: Call<List<SearchResult>>,
                response: Response<List<SearchResult>>
            ) {
                progressBar.visibility = ProgressBar.GONE

                if (response.isSuccessful) {
                    val results = response.body() ?: emptyList()
                    val shows = results.map { it.show }
                    showsAdapter.updateData(shows)

                    if (shows.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "No results found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<SearchResult>>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(
                    this@MainActivity,
                    "Failed to load shows: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
