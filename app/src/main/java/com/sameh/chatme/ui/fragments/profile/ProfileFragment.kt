package com.sameh.chatme.ui.fragments.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.sameh.chatme.R
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.User
import com.sameh.chatme.databinding.FragmentProfileBinding
import com.sameh.chatme.ui.alertdialog.LoadingAlertDialog
import com.sameh.chatme.ui.presenter.FirebaseFireStoreSaveDataProfilePresenter
import com.sameh.chatme.ui.presenter.FirebaseStorageProfilePresenter
import com.sameh.chatme.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), FirebaseStorageProfilePresenter,
    FirebaseFireStoreSaveDataProfilePresenter {

    private lateinit var binding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var loadingAlertDialog: LoadingAlertDialog

    private var currentUser: User? = null

    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        loadingAlertDialog = LoadingAlertDialog(requireContext())

        profileViewModel.repo.firebaseStorageProfile.firebaseStorageProfilePresenter = this
        profileViewModel.repo.firebaseFireStoreSaveData.firebaseFireStoreSaveDataProfilePresenter = this
        profileViewModel.getCurrentUser()

        loadingAlertDialog.showLoadingAlertDialog()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.currentUserLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                currentUser = it
                setUserDataInUi()
            }
            loadingAlertDialog.hideLoadingAlertDialog()
        }

        binding.cirImageUser.setOnClickListener {
            openPhotoGalleryToSelectImage()
        }

        binding.btnSave.setOnClickListener {
            if (!binding.edUsername.text.isNullOrEmpty()) {
                loadingAlertDialog.showLoadingAlertDialog()
                if (selectedUri != null) {
                    profileViewModel.uploadPhotoToFirebaseStorageProfile(selectedUri!!)
                } else {
                    updateUser(null, null)
                }
            } else {
                binding.edUsername.error = Constants.REQUIRED_FIELD
            }
        }
        binding.cirImageUser.setOnLongClickListener {
            confirmDeleteProfileImage()
            true
        }

        binding.imgBtnBack.setOnClickListener {
            goToLatestMessagesFragment()
        }

    }

    private fun setUserDataInUi() {
        if (currentUser?.profileImgUrl != null) {
            binding.cirImageUser.load(currentUser?.profileImgUrl)
        } else {
            binding.cirImageUser.setImageResource(R.drawable.no_user_image)
        }
        binding.tvEmail.text = currentUser?.email
        binding.edUsername.setText(currentUser?.username)
    }

    private fun goToLatestMessagesFragment() {
        val action = ProfileFragmentDirections.actionProfileFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    private fun openPhotoGalleryToSelectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncherOfSelectPhoto.launch(intent)
    }

    private var resultLauncherOfSelectPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                val uri = intent!!.data

                binding.cirImageUser.load(uri)
                selectedUri = uri

            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun updateUser(uri: Uri?, imageId: String?) {
        if (uri != null && imageId != null) {
            val id = currentUser!!.id
            val username = binding.edUsername.text.toString()
            val email = currentUser!!.email
            val profileImage = uri.toString()
            val user = User(id, username, email, profileImage, imageId)
            profileViewModel.updateUserInFireStoreDB(user)
        } else {
            val id = currentUser!!.id
            val username = binding.edUsername.text.toString()
            val email = currentUser!!.email
            val profileImage = currentUser!!.profileImgUrl
            val profileImageId = currentUser!!.profileImageId
            val user = User(id, username, email, profileImage, profileImageId)
            profileViewModel.updateUserInFireStoreDB(user)
        }
    }

    private fun confirmDeleteProfileImage() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Profile Image")
        builder.setMessage("Are you sure to delete your profile image ?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteProfileImage()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.show()
    }

    private fun deleteProfileImage() {
        if (currentUser!!.profileImgUrl != null) {
            loadingAlertDialog.showLoadingAlertDialog()
            profileViewModel.deleteImageFromFireStorageProfile(currentUser!!.profileImageId!!)
            profileViewModel.updateUserInFireStoreDB(
                user = User(
                    currentUser!!.id,
                    currentUser!!.username,
                    currentUser!!.email,
                    null,
                    null
                )
            )
            binding.cirImageUser.setImageResource(R.drawable.no_user_image)
        } else {
            Toast.makeText(requireContext(), "You don't have profile image", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun isUploadPhotoSuccessful(
        isSuccess: Boolean,
        statue: String,
        uri: Uri?,
        imageId: String?
    ) {
        if (isSuccess) {
            if (currentUser?.profileImgUrl != null) {
                profileViewModel.deleteImageFromFireStorageProfile(currentUser!!.profileImageId!!)
            }
            updateUser(uri!!, imageId)
        } else {
            Toast.makeText(requireContext(), statue, Toast.LENGTH_SHORT).show()
        }
    }

    override fun isUpdateUserFromFireStoreSuccess(isSuccess: Boolean, state: String) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (isSuccess) {
            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
        profileViewModel.getCurrentUser()
    }

}

/*
fun reloadFragment() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        parentFragmentManager.beginTransaction().detach(this).commitNow()
        parentFragmentManager.beginTransaction().attach(this).commitNow()
        //requireFragmentManager().beginTransaction().attach(this).commitNow()
    } else {
        parentFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }
}
 */