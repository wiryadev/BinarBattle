package com.wiryadev.binarbattle.ui.home

import androidx.lifecycle.*
import com.wiryadev.binarbattle.entity.CommonResponse
import com.wiryadev.binarbattle.entity.UpdateResponse
import com.wiryadev.binarbattle.network.ApiClient
import com.wiryadev.binarbattle.pref.SessionPreference
import com.wiryadev.binarbattle.pref.UserSession
import com.wiryadev.binarbattle.reduceFileImage
import com.wiryadev.binarbattle.ui.login.LoginViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class HomeViewModel(
    private val pref: SessionPreference,
) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData(null)
    val error: LiveData<Throwable?> get() = _error

    private val _authResponse: MutableLiveData<CommonResponse> = MutableLiveData()
    val authResponse: LiveData<CommonResponse> get() = _authResponse

    private val _updateResponse: MutableLiveData<UpdateResponse> = MutableLiveData()
    val updateResponse: LiveData<UpdateResponse> get() = _updateResponse

    private val _file: MutableLiveData<File?> = MutableLiveData(null)
    val file: LiveData<File?> get() = _file

    fun assignFile(newFile: File) {
        _file.value = newFile
    }

    var token = ""

    fun getUser(): LiveData<UserSession> {
        return pref.getUserSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.deleteUserSession()
        }
    }

    fun auth(
        token: String
    ) {
        ApiClient.getApiService().auth(token = "Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CommonResponse> {
                override fun onSubscribe(d: Disposable) {
                    _error.postValue(null)
                    _loading.postValue(true)
                }

                override fun onNext(t: CommonResponse) {
                    _authResponse.postValue(t)
                }

                override fun onError(e: Throwable) {
                    _loading.postValue(false)
                    _error.postValue(e)
                }

                override fun onComplete() {
                    _loading.postValue(false)
                }
            })
    }

    fun upload(
        token: String,
        username: String,
        email: String,
        file: File,
    ) {
        val requestUsername = username.toRequestBody("text/plain".toMediaType())
        val requestEmail = email.toRequestBody("text/plain".toMediaType())
        val requestImageFile = reduceFileImage(file).asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        ApiClient.getApiService()
            .update(
                token = "Bearer $token",
                username = requestUsername,
                email = requestEmail,
                file = imageMultipart,
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<UpdateResponse> {
                override fun onSubscribe(d: Disposable) {
                    _error.postValue(null)
                    _loading.postValue(true)
                }

                override fun onNext(t: UpdateResponse) {
                    _updateResponse.postValue(t)
                }

                override fun onError(e: Throwable) {
                    _loading.postValue(false)
                    _error.postValue(e)
                }

                override fun onComplete() {
                    _loading.postValue(false)
                }
            })
    }

}

class HomeViewModelFactory(
    private val pref: SessionPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(pref = pref) as T
    }
}