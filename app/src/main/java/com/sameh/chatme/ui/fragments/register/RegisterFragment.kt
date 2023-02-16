package com.sameh.chatme.ui.fragments.register

import android.app.Activity
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
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.databinding.FragmentRegisterBinding
import com.sameh.chatme.ui.alertdialog.LoadingAlertDialog
import com.sameh.chatme.ui.presenter.FirebaseAuthRegisterPresenter
import com.sameh.chatme.ui.presenter.FirebaseFireStoreRegisterPresenter
import com.sameh.chatme.ui.presenter.FirebaseStorageRegisterPresenter
import com.sameh.chatme.ui.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(),
    FirebaseAuthRegisterPresenter,
    FirebaseStorageRegisterPresenter,
    FirebaseFireStoreRegisterPresenter {

    private lateinit var binding: FragmentRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var loadingAlertDialog: LoadingAlertDialog

    private var selectedUri: Uri? = null
    private var currentEmail: String? = null
    private var currentUsername: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        registerViewModel.repo.firebaseAuthentication.firebaseAuthRegisterPresenter = this
        registerViewModel.repo.firebaseStorageSaveDataRegister.firebaseStorageRegisterPresenter = this
        registerViewModel.repo.firebaseFireStoreSaveData.firebaseFireStoreRegisterPresenter = this

        loadingAlertDialog = LoadingAlertDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.crcImgUser.setOnClickListener {
            openPhotoGalleryToSelectImage()
        }

        binding.tvUserHaveAlreadyAccount.setOnClickListener {
            goToLoginFragment()
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.edUsername.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val verifyPassword = binding.edVerifyPassword.text.toString()

            val validation = verifyDataFormUser(username, email, password, verifyPassword)
            val matchPasswords = verifyMatchPasswords(password, verifyPassword)

            if (validation && matchPasswords) {
                currentEmail = email
                currentUsername = username
                registerViewModel.createNewUserAccount(email, password)
                loadingAlertDialog.showLoadingAlertDialog()
            }
        }

    }

    private fun verifyDataFormUser(
        username: String,
        email: String,
        password: String,
        verifyPassword: String
    ): Boolean {

        if (username.isEmpty() && username.isBlank()) {
            binding.edUsername.error = Constants.REQUIRED_FIELD
        }
        if (email.isEmpty() && email.isBlank()) {
            binding.edEmail.error = Constants.REQUIRED_FIELD
        }
        if (password.isEmpty() && password.isBlank()) {
            binding.edPassword.error = Constants.REQUIRED_FIELD
        }
        if (verifyPassword.isEmpty() && verifyPassword.isBlank()) {
            binding.edVerifyPassword.error = Constants.REQUIRED_FIELD
        }

        return (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty())
    }

    private fun verifyMatchPasswords(password: String, verifyPassword: String): Boolean {
        return if (password == verifyPassword) {
            true
        } else {
            Toast.makeText(requireContext(), "Passwords don't match", Toast.LENGTH_SHORT).show()
            false
        }
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

                binding.crcImgUser.load(uri)
                selectedUri = uri

            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun goToLoginFragment() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    override fun ifCreateNewUserAccountSuccess(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            registerViewModel.sendEmailVerificationRegister()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    override fun ifSendEmailVerificationSuccessRegister(ifSuccess: Boolean, state: String) {
        if (ifSuccess) {
            if (selectedUri != null) {
                registerViewModel.uploadPhotoToFirebaseStorage(selectedUri!!)
            } else {
                registerViewModel.insertUserToFireStoreDB(
                    currentUsername!!,
                    currentEmail!!,
                    null,
                    null
                )
            }
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    override fun ifImageUploadedSuccess(ifSuccess: Boolean, state: String, uri: Uri?, imageId: String?) {
        if (ifSuccess) {
            registerViewModel.insertUserToFireStoreDB(
                currentUsername!!,
                currentEmail!!,
                uri.toString(),
                imageId
            )
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    override fun ifUserInsertedSuccess(ifSuccess: Boolean, state: String) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifSuccess) {
            Toast.makeText(requireContext(), "Successful, verify your account and login", Toast.LENGTH_SHORT)
                .show()
            goToLoginFragment()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }

}