package hr.ivuksan.sherry.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ivuksan.sherry.R
import hr.ivuksan.sherry.databinding.HomeScreenBinding
import hr.ivuksan.sherry.viewModel.toolbarVM.ToolbarViewModel
import hr.ivuksan.sherry.viewModel.albumVM.*
import hr.ivuksan.sherry.viewModel.artistVM.ArtistsViewModel
import hr.ivuksan.sherry.viewModel.artistVM.ArtistsViewModelFactory
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistClickListener
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModel
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModelFactory
import hr.ivuksan.sherry.viewModel.toolbarVM.ToolbarViewModelFactory
import java.util.Random

class HomeActivity : AppCompatActivity(), AlbumClickListener, PlaylistClickListener {

    lateinit var toolbarViewModel: ToolbarViewModel
    lateinit var playlistsViewModel: PlaylistsViewModel
    lateinit var albumsViewModel: AlbumsViewModel
    lateinit var artistsViewModel: ArtistsViewModel
    lateinit var binding: HomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbarViewModelFactory = ToolbarViewModelFactory(application)
        val playlistViewModelFactory = PlaylistsViewModelFactory(application)
        val albumsViewModelFactory = AlbumsViewModelFactory(application)
        val artistsViewModelFactory = ArtistsViewModelFactory(application)

        toolbarViewModel = ViewModelProvider(this, toolbarViewModelFactory)
            .get(ToolbarViewModel::class.java)
        playlistsViewModel = ViewModelProvider(this, playlistViewModelFactory)
            .get(PlaylistsViewModel::class.java)
        albumsViewModel = ViewModelProvider(this, albumsViewModelFactory)
            .get(AlbumsViewModel::class.java)
        artistsViewModel = ViewModelProvider(this, artistsViewModelFactory)
            .get(ArtistsViewModel::class.java)

        binding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.home_screen)
        binding.lifecycleOwner = this

        toolbarViewModel.getWelcomeMessage()
        toolbarViewModel.welcomeMessage.observe(this) { welcomeMessage ->
            if (welcomeMessage != null){
                binding.userMessage.text = welcomeMessage
            }
        }

        val playlistsAdapter = PlaylistsRecyclerViewAdapter(this)
        val albumsAdapter = NewReleasesRecyclerViewAdapter(this)
        val artistsAdapter = ArtistsAlbumsRecyclerViewAdapter(this)

        val playlistsRecyclerView = binding.featuredPlaylistsRecyclerView
        val albumsRecyclerView = binding.newReleasesRecyclerView
        val artistsRecyclerView = binding.evolutionOfArtistRecyclerView

        playlistsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        albumsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        artistsRecyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)

        playlistsRecyclerView.adapter = playlistsAdapter
        albumsRecyclerView.adapter = albumsAdapter
        artistsRecyclerView.adapter = artistsAdapter

        binding.newReleasesTitle.text = resources.getString(R.string.new_releases_title)
        binding.featuredPlaylistsTitle.text = resources.getString(R.string.featured_playlists_title)
        binding.artistsTitle.text = resources.getString(R.string.artists_title)

        albumsViewModel.getNewReleases()
        albumsViewModel.newReleasesList.observe(this){ newReleases ->
            if (newReleases != null) {
                albumsAdapter.setNewReleases(newReleases)
            }
        }

        playlistsViewModel.getFeaturedPlaylists()
        playlistsViewModel.featuredPlaylistsList.observe(this){ featuredPlaylists ->
            if (featuredPlaylists != null) {
                playlistsAdapter.setPlaylists(featuredPlaylists.playlists)
            }
        }

        artistsViewModel.getUsersFollowedArtists()
        artistsViewModel.artistList.observe(this){ artists ->
            if (artists != null) {
                val randomIndex = Random().nextInt(artists.artists?.items?.size ?: 0)
                artistsViewModel.getArtistsAlbums(artists.artists?.items?.get(randomIndex))
                val formattedText = resources.getString(
                    R.string.artists_albums_title_with_name,
                    resources.getString(R.string.artists_albums_title),
                    artists.artists?.items?.get(randomIndex)?.name
                )
                binding.artistsTitle.text = formattedText
            }
        }

        artistsViewModel.artistsAlbums.observe(this){ artistsAlbums ->
            if (artistsAlbums != null){
                artistsAdapter.setArtistsAlbums(artistsAlbums)
            }
        }

        binding.bottomNavigation.menu.getItem(0).isChecked = true

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this@HomeActivity, SearchActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_library -> {
                    startActivity(Intent(this@HomeActivity, LibraryActivity::class.java))
                    finish()
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