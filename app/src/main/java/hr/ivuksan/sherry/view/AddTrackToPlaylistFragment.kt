package hr.ivuksan.sherry.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ivuksan.sherry.databinding.AddTrackToPlaylistFragmentBinding
import hr.ivuksan.sherry.viewModel.searchVM.AddClickListener
import hr.ivuksan.sherry.viewModel.searchVM.SearchToAddRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.searchVM.SearchViewModel
import hr.ivuksan.sherry.viewModel.searchVM.SearchViewModelFactory
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModel
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModelFactory

class AddTrackToPlaylistFragment: Fragment(), AddClickListener {

    private var binding: AddTrackToPlaylistFragmentBinding? = null
    lateinit var searchViewModel: SearchViewModel
    lateinit var trackViewModel: TrackViewModel
    lateinit var playlistId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTrackToPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = arguments?.getString("playlistId", "").toString()

        val searchViewModelFactory = SearchViewModelFactory(requireActivity().application)
        searchViewModel = ViewModelProvider(this, searchViewModelFactory).get(SearchViewModel::class.java)
        val trackViewModelFactory = TrackViewModelFactory(requireActivity().application)
        trackViewModel = ViewModelProvider(this, trackViewModelFactory).get(TrackViewModel::class.java)

        val trackRecyclerViewAdapter = SearchToAddRecyclerViewAdapter(this)
        val trackRecyclerView = binding?.trackRecyclerView

        trackRecyclerView?.layoutManager = LinearLayoutManager(context)
        trackRecyclerView?.adapter = trackRecyclerViewAdapter

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchViewModel.searchForTracks(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchViewModel.tracksResponse.observe(viewLifecycleOwner){ tracksResponse ->
            if (tracksResponse != null){
                trackRecyclerViewAdapter.setTracks(tracksResponse.tracks.items)
            }
        }
    }

    override fun onAddTrackClicked(trackId: String) {
        trackViewModel.addTrackToPlaylist(playlistId, trackId)
        restartActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun restartActivity() {
        val intent = Intent(requireActivity(), PlaylistActivity::class.java)
        intent.putExtra("playlistId", playlistId)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        requireActivity().finish()
    }
}