package hr.ivuksan.sherry.view

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.databinding.SearchScreenBinding
import hr.ivuksan.sherry.viewModel.searchVM.SearchRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.searchVM.SearchViewModel
import hr.ivuksan.sherry.viewModel.searchVM.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    lateinit var searchViewModel: SearchViewModel
    lateinit var binding: SearchScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val searchViewModelFactory = SearchViewModelFactory(application)
        searchViewModel = ViewModelProvider(this, searchViewModelFactory)
            .get(SearchViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@SearchActivity, R.layout.search_screen)
        binding.lifecycleOwner = this

        val searchRecyclerViewAdapter = SearchRecyclerViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.adapter = searchRecyclerViewAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchViewModel.searchForItem(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        searchViewModel.searchResponse.observe(this){searchList ->
            if (searchList != null){
                searchRecyclerViewAdapter.setSearchResponses(searchList)
            }

        }

        binding.bottomNavigation.menu.getItem(1).isChecked = true

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@SearchActivity, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_search -> {
                    true
                }
                R.id.navigation_library -> {
                    startActivity(Intent(this@SearchActivity, LibraryActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}