package hr.ivuksan.sherry.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import hr.ivuksan.sherry.databinding.PlayerLayoutBinding
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModel
import hr.ivuksan.sherry.viewModel.trackVM.TrackViewModelFactory

class PlayerFragment: Fragment() {

    private var queueFragmentListener: QueueFragmentListener? = null
    private var binding: PlayerLayoutBinding? = null
    private var trackId: String? = null
    lateinit var trackViewModel: TrackViewModel

    companion object {
        fun newInstance(trackId: String): PlayerFragment {
            val playerFragment = PlayerFragment()
            val args = Bundle()
            args.putString("trackId", trackId)
            playerFragment.arguments = args
            return playerFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QueueFragmentListener) {
            queueFragmentListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trackId = it.getString("trackId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlayerLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackId?.let { trackId ->
            val trackViewModelFactory = TrackViewModelFactory(requireActivity().application)
            trackViewModel = ViewModelProvider(this, trackViewModelFactory).get(TrackViewModel::class.java)
            trackViewModel.getTrack(trackId)
            trackViewModel.track.observe(viewLifecycleOwner){track ->
                if (track != null){
                    Picasso.get()
                        .load(track.album.images.firstOrNull()?.url?.toUri())
                        .into(binding!!.songThumbnail)
                    val artistList = track.artists.joinToString(", ") {it.name}
                    binding!!.songArtist.text = artistList
                    binding!!.songTitle.text = track.name
                }
            }
        }

        binding?.queueButton?.setOnClickListener {
            queueFragmentListener?.showQueueFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun updateTrack(trackId: String) {
        this.trackId = trackId
        trackViewModel.getTrack(trackId)
    }
}