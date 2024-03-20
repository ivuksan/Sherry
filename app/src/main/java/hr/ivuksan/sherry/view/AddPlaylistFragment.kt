package hr.ivuksan.sherry.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import hr.ivuksan.sherry.databinding.AddPlaylistFragmentBinding
import hr.ivuksan.sherry.viewModel.userVM.UserViewModel
import hr.ivuksan.sherry.viewModel.userVM.UserViewModelFactory
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModel
import hr.ivuksan.sherry.viewModel.playlistVM.PlaylistsViewModelFactory

class AddPlaylistFragment: DialogFragment() {

    private var binding: AddPlaylistFragmentBinding? = null
    lateinit var userViewModel: UserViewModel
    lateinit var playlistsViewModel: PlaylistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistViewModelFactory = PlaylistsViewModelFactory(requireActivity().application)
        val userViewModelFactory = UserViewModelFactory(requireActivity().application)

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
        playlistsViewModel = ViewModelProvider(this, playlistViewModelFactory).get(PlaylistsViewModel::class.java)

        userViewModel.getUser()

        binding?.confirmButton?.setOnClickListener {
            userViewModel.user.observe(viewLifecycleOwner){ user ->
                if (user != null) {
                    playlistsViewModel.createPlaylist(user.id, binding!!.playlistNameEditText.text.toString())
                    restartActivity()
                }
            }
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val params: LayoutParams? = dialog?.window?.attributes
        params?.width = LayoutParams.MATCH_PARENT
        params?.height = LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme)
    }

    private fun restartActivity() {
        val intent = Intent(requireActivity(), LibraryActivity::class.java)
        intent.addFlags(
               Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
        requireActivity().finish()
    }
}