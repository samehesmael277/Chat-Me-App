package com.sameh.chatme.ui.fragments.newmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sameh.chatme.data.model.User
import com.sameh.chatme.databinding.FragmentNewMessageBinding
import com.sameh.chatme.ui.alertdialog.LoadingAlertDialog
import com.sameh.chatme.ui.fragments.newmessage.adapter.NewMessageAdapter
import com.sameh.chatme.ui.fragments.newmessage.adapter.OnUserClickListener
import com.sameh.chatme.ui.presenter.FirebaseRetrieveFromFireStoreNewMessagePresenter
import com.sameh.chatme.ui.viewModel.NewMessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject

@AndroidEntryPoint
class NewMessageFragment : Fragment(), FirebaseRetrieveFromFireStoreNewMessagePresenter,
    OnUserClickListener {

    private lateinit var binding: FragmentNewMessageBinding

    private val newMessageViewModel: NewMessageViewModel by viewModels()

    @Inject
    lateinit var newMessageAdapter: NewMessageAdapter

    private lateinit var loadingAlertDialog: LoadingAlertDialog

    private val currentUser = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMessageBinding.inflate(inflater, container, false)

        newMessageViewModel.repo.firebaseRetrieveFromFireStore.firebaseRetrieveFromFireStoreNewMessagePresenter =
            this

        loadingAlertDialog = LoadingAlertDialog(requireContext())

        newMessageViewModel.getAllUser()
        loadingAlertDialog.showLoadingAlertDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        newMessageViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                newMessageAdapter.setData(it)
            }
            loadingAlertDialog.hideLoadingAlertDialog()
        }

        binding.imgBtnBack.setOnClickListener {
            goToLatestMessagesFragment()
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    newMessageViewModel.searchOnUsers(query)

                    newMessageViewModel.searchUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            newMessageAdapter.setData(it as ArrayList<User>)
                        }
                    }
                } else {
                    newMessageViewModel.getAllUser()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    newMessageViewModel.searchOnUsers(newText)

                    newMessageViewModel.searchUsersLiveData.observe(viewLifecycleOwner) {
                        if (it != null) {
                            newMessageAdapter.setData(it as ArrayList<User>)
                        }
                    }
                } else {
                    newMessageViewModel.getAllUser()
                }
                return true
            }
        })

    }

    private fun setUpRecyclerView() {
        binding.usersRecView.adapter = newMessageAdapter
        newMessageAdapter.onClickListener = this
        binding.usersRecView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.usersRecView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }
    }

    private fun goToLatestMessagesFragment() {
        val action = NewMessageFragmentDirections.actionNewMessageFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    private fun goToChatFragment(user: User) {
        val action = NewMessageFragmentDirections.actionNewMessageFragmentToChatFragment(user)
        findNavController().navigate(action)
    }

    override fun ifRetrieveFromFirebaseSuccess(isSuccess: Boolean, state: String) {
        if (!isSuccess) {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    override fun onUserClickListener(user: User) {
        if (user.email == currentUser!!.email) {
            Toast.makeText(requireContext(), "You", Toast.LENGTH_SHORT).show()
        } else {
            goToChatFragment(user)
        }
    }

}