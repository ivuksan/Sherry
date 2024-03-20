package hr.ivuksan.sherry.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ivuksan.sherry.viewModel.albumVM.AlbumsRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.artistVM.ArtistsRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsRecyclerViewAdapter
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.databinding.LibraryScreenBinding
import hr.ivuksan.sherry.viewModel.albumVM.AlbumClickListener
import hr.ivuksan.sherry.viewModel.albumVM.AlbumsViewModel
import hr.ivuksan.sherry.viewModel.albumVM.AlbumsViewModelFactory
import hr.ivuksan.sherry.viewModel.artistVM.ArtistsViewModel
import hr.ivuksan.sherry.viewModel.artistVM.ArtistsViewModelFactory
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistClickListener
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModel
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModelFactory

class LibraryActivity : AppCompatActivity(), AlbumClickListener, PlaylistClickListener {

    lateinit var playlistsViewModel: PlaylistsViewModel
    lateinit var albumsViewModel: AlbumsViewModel
    lateinit var artistsViewModel: ArtistsViewModel
    lateinit var binding: LibraryScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playlistViewModelFactory = PlaylistsViewModelFactory(application)
        val albumsViewModelFactory = AlbumsViewModelFactory(application)
        val artistsViewModelFactory = ArtistsViewModelFactory(application)

        playlistsViewModel = ViewModelProvider(this, playlistViewModelFactory)
            .get(PlaylistsViewModel::class.java)
        albumsViewModel = ViewModelProvider(this, albumsViewModelFactory)
            .get(AlbumsViewModel::class.java)
        artistsViewModel = ViewModelProvider(this, artistsViewModelFactory)
            .get(ArtistsViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@LibraryActivity, R.layout.library_screen)
        binding.lifecycleOwner = this

        binding.userMessage.text = resources.getString(R.string.library_title)

        val playlistsAdapter = PlaylistsRecyclerViewAdapter(this)
        val albumsAdapter = AlbumsRecyclerViewAdapter(this)
        val artistsAdapter = ArtistsRecyclerViewAdapter()

        val playlistsRecyclerView = binding.playlistsRecyclerView
        val albumsRecyclerView = binding.albumsRecyclerView
        val artistsRecyclerView = binding.artistsRecyclerView

        playlistsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        albumsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        artistsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)

        playlistsRecyclerView.adapter = playlistsAdapter
        albumsRecyclerView.adapter = albumsAdapter
        artistsRecyclerView.adapter = artistsAdapter

        binding.playlistsTitle.text = resources.getString(R.string.playlists_title)
        binding.albumsTitle.text = resources.getString(R.string.albums_title)
        binding.artistsTitle.text = resources.getString(R.string.artists_title)

        playlistsViewModel.getUsersPlaylists()
        playlistsViewModel.playlistList.observe(this){ playlists ->
            if (playlists != null){
                playlistsAdapter.setPlaylists(playlists)
            }
        }
        albumsViewModel.getUsersSavedAlbums()
        albumsViewModel.albumList.observe(this){ albums ->
            if (albums != null){
                albumsAdapter.setAlbums(albums)
            }
        }
        artistsViewModel.getUsersFollowedArtists()
        artistsViewModel.artistList.observe(this){ artists ->
            if (artists != null){
                artistsAdapter.setArtists(artists)
            }
        }

        binding.addIcon.setOnClickListener{
            val fragment = AddPlaylistFragment()
            fragment.show(supportFragmentManager, AddPlaylistFragment::class.java.simpleName)
        }

        binding.bottomNavigation.menu.getItem(2).isChecked = true

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@LibraryActivity, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this@LibraryActivity, SearchActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_library -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun onAlbumClicked(albumId: String) {
        val intent = Intent(this, AlbumActivity::class.java)
        intent.putExtra("albumId", albumId)
        startActivity(intent)
    }

    override fun onPlaylistClicked(playlistId: String) {
        val intent = Intent(this, PlaylistActivity::class.java)
        intent.putExtra("playlistId", playlistId)
        startActivity(intent)
    }
}