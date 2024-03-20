package hr.ivuksan.sherry.view

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
import hr.ivuksan.sherry.databinding.AlbumScreenBinding
import hr.ivuksan.sherry.viewModel.albumVM.AlbumsViewModel
import hr.ivuksan.sherry.viewModel.albumVM.AlbumsViewModelFactory
import hr.ivuksan.sherry.viewModel.albumVM.AlbumTracksRecyclerViewAdapter

class AlbumActivity: AppCompatActivity(), TrackClickListener, QueueFragmentListener {

    lateinit var binding: AlbumScreenBinding
    lateinit var albumsViewModel: AlbumsViewModel
    lateinit var trackViewModel: TrackViewModel
    var playerFragment: PlayerFragment? = null
    lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val albumsViewModelFactory = AlbumsViewModelFactory(application)
        val trackViewModelFactory = TrackViewModelFactory(application)
        albumsViewModel = ViewModelProvider(this, albumsViewModelFactory)
            .get(AlbumsViewModel::class.java)
        trackViewModel = ViewModelProvider(this, trackViewModelFactory)
            .get(TrackViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@AlbumActivity, R.layout.album_screen)
        binding.lifecycleOwner = this

        val tracksAdapter = AlbumTracksRecyclerViewAdapter(this)
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

        albumsViewModel.getAlbum(intent.extras?.getString("albumId") ?: "")
        albumsViewModel.album.observe(this){ album ->
            if (album != null) {
                Picasso.get()
                    .load(album.images.firstOrNull()?.url?.toUri())
                    .into(binding.albumCover)
                binding.albumTitle.text = album.name
                val artistList = album.artists.joinToString(", ") {it.name}
                binding.albumArtists.text = artistList
                tracksAdapter.setTracks(album.tracks.items)
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
            .replace(binding.albumLayout.id, queueFragment)
            .addToBackStack(null)
            .commit()
    }
}