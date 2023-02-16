package com.sameh.chatme.ui.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sameh.chatme.data.model.User
import com.sameh.chatme.databinding.FragmentChatBinding
import com.sameh.chatme.ui.fragments.chat.adapter.MessageAdapter
import com.sameh.chatme.ui.presenter.FirebaseRealTimeChatPresenter
import com.sameh.chatme.ui.viewModel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

@AndroidEntryPoint
class ChatFragment : Fragment(), FirebaseRealTimeChatPresenter {

    private lateinit var binding: FragmentChatBinding

    private val args by navArgs<ChatFragmentArgs>()

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var messageAdapter: MessageAdapter

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String

    private val currentUserId = Firebase.auth.currentUser!!.uid

    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        chatViewModel.repo.firebaseRealTimeDatabase.firebaseRealTimeChatPresenter = this
        chatViewModel.getCurrentUser()

        senderRoom = currentUserId + args.user.id
        receiverRoom = args.user.id + currentUserId
        chatViewModel.getMessages(senderRoom)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                currentUser = it
            }
        }

        setDataInUi()
        setUpRecyclerView()

        chatViewModel.messagesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                messageAdapter.setMessagesListToAdapter(it)
                binding.recChat.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            goToLatestMessageFragment()
        }

        binding.imgBtnBack.setOnClickListener {
            goToLatestMessageFragment()
        }

        binding.btnSend.setOnClickListener {
            val message = binding.edMessage.text.toString()
            if (message.isNotBlank() && message.isNotEmpty()) {
                chatViewModel.sendMessage(message, senderRoom, receiverRoom, args.user, currentUser)
                binding.edMessage.setText("")
            } else {
                binding.edMessage.error = "Enter message"
            }
        }
    }

    private fun setDataInUi() {
        binding.tvUsername.text = args.user.username
        if (args.user.profileImgUrl != null) {
            binding.cirImgUser.load(args.user.profileImgUrl)
        }
    }

    private fun setUpRecyclerView() {
        messageAdapter = MessageAdapter(requireContext(), args.user.id)
        binding.recChat.adapter = messageAdapter
        binding.recChat.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.recChat.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 200
        }
    }

    private fun goToLatestMessageFragment() {
        val action = ChatFragmentDirections.actionChatFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    override fun ifSaveMessageToDatabaseSuccess(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            binding.recChat.scrollToPosition(messageAdapter.itemCount - 1)
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }

}