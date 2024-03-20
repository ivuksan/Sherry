package hr.ivuksan.sherry.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.viewModel.trackVM.TrackClickListener
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModel
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModelFactory
import hr.ivuksan.sherry.databinding.PlaylistScreenBinding
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistTracksRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModel
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModelFactory

class PlaylistActivity: AppCompatActivity(), TrackClickListener, QueueFragmentListener {

    lateinit var binding: PlaylistScreenBinding
    lateinit var playlistsViewModel: PlaylistsViewModel
    lateinit var trackViewModel: TrackViewModel
    var playerFragment: PlayerFragment? = null
    lateinit var deviceId: String
    lateinit var tracksAdapter: PlaylistTracksRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistViewModelFactory = PlaylistsViewModelFactory(application)
        val trackViewModelFactory = TrackViewModelFactory(application)
        playlistsViewModel = ViewModelProvider(this, playlistViewModelFactory)
            .get(PlaylistsViewModel::class.java)
        trackViewModel = ViewModelProvider(this, trackViewModelFactory)
            .get(TrackViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@PlaylistActivity, R.layout.playlist_screen)
        binding.lifecycleOwner = this

        tracksAdapter = PlaylistTracksRecyclerViewAdapter(this)
        val tracksRecyclerView = binding.tracklistRecyclerView

        tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        tracksRecyclerView.adapter = tracksAdapter

        trackViewModel.getAvailableDevices()
        trackViewModel.deviceList.observe(this){ deviceList ->
            if (deviceList.devices.size == 1){
                deviceId = deviceList.devices.first().id
            }else{
                deviceId = deviceList.devices.first().id
            }
        }

        playlistsViewModel.getPlaylist(intent.extras?.getString("playlistId") ?: "")
        playlistsViewModel.playlist.observe(this){ playlist ->
            if (playlist != null) {
                val imageUrl: Uri? = playlist.images?.firstOrNull()?.url?.toUri()
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.playlistCover)
                binding.playlistTitle.text = playlist.name
                if (playlist.description.isEmpty()){
                    binding.playlistDescription.text = ""
                }else {
                    binding.playlistDescription.text = playlist.description
                }
                if (playlist.tracks.items.isNotEmpty()) {
                    tracksAdapter.setTracks(playlist.tracks.items)
                    binding.addToPlaylistIcon.visibility = View.VISIBLE
                    binding.addToPlaylistIcon.setOnClickListener{
                        showAddToPlaylistFragment(intent.extras?.getString("playlistId") ?: "")
                    }
                }else{
                    binding.tracklistRecyclerView.visibility = View.GONE
                    binding.addToPlaylistButton.visibility = View.VISIBLE
                    binding.addToPlaylistButton.setOnClickListener{
                        showAddToPlaylistFragment(intent.extras?.getString("playlistId") ?: "")
                    }
                }
            }
        }

        binding.backArrow.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onTrackClicked(trackId: String) {
        if (playerFragment == null){
            binding.playerContainer.visibility = View.VISIBLE
            playerFragment = PlayerFragment.newInstance(trackId)
            supportFragmentManager.beginTransaction()
                .replace(binding.playerContainer.id, playerFragment!!)
                .commit()
        }else {
            playerFragment?.updateTrack(trackId)
        }
        trackViewModel.playSong(trackId, deviceId)
    }

    override fun showQueueFragment() {
        val queueFragment = QueueFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.playlistLayout.id, queueFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showAddToPlaylistFragment(playlistId: String){
        val bundle = Bundle()
        bundle.putString("playlistId", playlistId)
        val addToPlaylistFragment = AddTrackToPlaylistFragment()
        addToPlaylistFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(binding.playlistLayout.id, addToPlaylistFragment)
            .addToBackStack(null)
            .commit()
    }
}