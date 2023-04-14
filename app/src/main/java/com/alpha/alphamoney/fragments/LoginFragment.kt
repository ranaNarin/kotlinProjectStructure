package com.alpha.alphamoney.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alpha.alphamoney.R
import com.alpha.alphamoney.databinding.FragmentLoginBinding
import com.alpha.alphamoney.utils.NetworkResult
import com.alpha.alphamoney.viewModel.AuthViewModel
import com.alpha.alphamoney.models.UserRequest
import com.alpha.alphamoney.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get()= _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)//inflater.inflate(R.layout.fragment_login, container, false)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener{
           // findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            val userRequest=  getUserRequest()
            val validateUser=validateUserInput(userRequest)
            if(validateUser.first){
                authViewModel.loginUser(userRequest)
            }
            else{
                binding.txtError.text=validateUser.second
            }
        }
        binding.btnSignUp.setOnClickListener{

            //findNavController().navigate(R.id.logi)

            findNavController().popBackStack()
        }

        bindObserver()
    }


    private fun getUserRequest(): UserRequest {

        val email = binding.txtEmail.text.toString()
        val password= binding.txtPassword.text.toString()

        return UserRequest(email, password, "")
    }

    private fun validateUserInput(userRequest: UserRequest): Pair<Boolean, String> {

        return authViewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password, true)
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible=false
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible=true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}