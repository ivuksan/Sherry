package hr.ivuksan.sherry.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.ivuksan.sherry.databinding.QueueLayoutBinding
import hr.ivuksan.sherry.viewModel.queueVM.QueueRecyclerViewAdapter
import hr.ivuksan.sherry.viewModel.queueVM.QueueViewModel
import hr.ivuksan.sherry.viewModel.queueVM.QueueViewModelFactory

class QueueFragment: Fragment() {

    private var binding: QueueLayoutBinding? = null
    lateinit var queueViewModel: QueueViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QueueLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val queueViewModelFactory = QueueViewModelFactory(requireActivity().application)
        queueViewModel = ViewModelProvider(this, queueViewModelFactory).get(QueueViewModel::class.java)

        val queueRecyclerViewAdapter = QueueRecyclerViewAdapter()
        val queueRecyclerView = binding?.queueRecyclerView

        queueRecyclerView?.layoutManager = LinearLayoutManager(context)
        queueRecyclerView?.adapter = queueRecyclerViewAdapter

        queueViewModel.getRecentlyPlayedTracks()
        queueViewModel.recentlyPlayedList.observe(viewLifecycleOwner){tracks ->
            if (tracks != null) {
                queueViewModel.getRecommendations(tracks.items)
            }
        }
        queueViewModel.recommendationTracks.observe(viewLifecycleOwner){recommendationTracks ->
            if (recommendationTracks != null) {
                queueRecyclerViewAdapter.setTracks(recommendationTracks.tracks)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}