package com.alpha.alphamoney.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alpha.alphamoney.api.UserAPI
import com.alpha.alphamoney.utils.Constants.TAG
import com.alpha.alphamoney.utils.NetworkResult
import com.alpha.alphamoney.models.UserRequest
import com.alpha.alphamoney.models.UserResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
    get()= _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {

        Log.e(TAG, "handle response : "+response.body().toString())
        if(response.isSuccessful && response.body()!=null){

            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody() != null){
            try {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }catch (jsonExc : JSONException)
            {
                Log.e(TAG, "exception : ${jsonExc.message}")
                _userResponseLiveData.postValue(NetworkResult.Error(jsonExc.message))
            }

        }
        else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }


    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }
}