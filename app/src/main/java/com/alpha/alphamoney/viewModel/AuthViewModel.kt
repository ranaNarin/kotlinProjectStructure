package com.alpha.alphamoney.viewModel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.alphamoney.repository.UserRepository
import com.alpha.alphamoney.utils.NetworkResult
import com.alpha.alphamoney.models.UserRequest
import com.alpha.alphamoney.models.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel(){

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials
                (username : String,
                 emailAddress : String,
                 password : String,
                 isLogin :Boolean
    ) :
            Pair<Boolean, String>{

        var result= Pair(true, "")
        if(!isLogin && TextUtils.isEmpty(username) ||
            TextUtils.isEmpty(emailAddress) ||
            TextUtils.isEmpty(password)){
            result = Pair(false, "please provide the credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false, "please provide valid email address")
        }
        else if (password.length <= 5){
            result = Pair(false, "password length should be greater then 5")
        }
        return result
    }
}