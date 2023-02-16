package com.sameh.chatme.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.databinding.FragmentLoginBinding
import com.sameh.chatme.ui.alertdialog.LoadingAlertDialog
import com.sameh.chatme.ui.presenter.FirebaseAuthLoginPresenter
import com.sameh.chatme.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), FirebaseAuthLoginPresenter {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var loadingAlertDialog: LoadingAlertDialog

    private val currentUser = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel.repo.firebaseAuthentication.firebaseAuthLoginPresenter = this

        loadingAlertDialog = LoadingAlertDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUserDoNotHaveAccount.setOnClickListener {
            goToRegisterFragment()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            val validation = verifyDataFromUser(email, password)

            if (validation) {
                loginViewModel.singInWithEmailPassword(email, password)
                loadingAlertDialog.showLoadingAlertDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(currentUser != null && currentUser.isEmailVerified){
            goToLatestMessagesFragment()
        }
    }

    private fun verifyDataFromUser(email: String, password: String): Boolean {

        if (email.isEmpty()) {
            binding.edEmail.error = Constants.REQUIRED_FIELD
        }
        if (password.isEmpty()) {
            binding.edPassword.error = Constants.REQUIRED_FIELD
        }

        return (email.isNotEmpty() && password.isNotEmpty())
    }

    private fun goToRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun showSnackBarSendVerify() {
        val snackBar = Snackbar.make(
            requireContext(),
            requireView(),
            "You should verify your account to able to sign in chat me app",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Send Verify") {
            loginViewModel.sendEmailVerificationLogin()
            loadingAlertDialog.showLoadingAlertDialog()
        }
        snackBar.show()
    }

    private fun goToLatestMessagesFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToLatestMessagesFragment()
        findNavController().navigate(action)
    }

    override fun ifSingInWithEmailPasswordComplete(ifComplete: Boolean, state: String) {
        if (ifComplete) {
            loginViewModel.checkEmailVerification()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
            loadingAlertDialog.hideLoadingAlertDialog()
        }
    }

    override fun ifEmailVerificationLogin(ifVerified: Boolean) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifVerified) {
            goToLatestMessagesFragment()
        } else {
            showSnackBarSendVerify()
        }
    }

    override fun ifSendEmailVerificationSentSuccessLogin(ifSuccess: Boolean, state: String) {
        loadingAlertDialog.hideLoadingAlertDialog()
        if (ifSuccess) {
            Toast.makeText(requireContext(), "sent, check your email", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), state, Toast.LENGTH_SHORT).show()
        }
    }

}