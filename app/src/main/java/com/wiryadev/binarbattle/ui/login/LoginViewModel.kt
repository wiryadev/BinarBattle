package com.wiryadev.binarbattle.ui.login

import androidx.lifecycle.*
import com.wiryadev.binarbattle.entity.LoginResponse
import com.wiryadev.binarbattle.network.ApiClient
import com.wiryadev.binarbattle.network.NetworkUtil
import com.wiryadev.binarbattle.pref.SessionPreference
import com.wiryadev.binarbattle.pref.UserSession
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: SessionPreference,
) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _loginResponse: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginResponse: LiveData<LoginResponse> get() = _loginResponse

    fun getUser(): LiveData<UserSession> {
        return pref.getUserSession().asLiveData()
    }

    fun login(
        email: String,
        password: String,
    ) {
        val body = NetworkUtil.createRequestBody(
            "email" to email,
            "password" to password,
        )
        ApiClient.getApiService().login(requestBody = body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<LoginResponse> {

                override fun onSubscribe(d: Disposable) {
                    _loading.postValue(true)
                }

                override fun onNext(t: LoginResponse) {
                    _loginResponse.postValue(t)
                }

                override fun onError(e: Throwable) {
                    _loading.postValue(false)
                }

                override fun onComplete() {
                    _loading.postValue(false)
                }
            })
    }

    fun saveUser(userSession: UserSession) {
        viewModelScope.launch {
            pref.saveUserSession(userSession)
        }
    }
}

class LoginViewModelFactory(
    private val pref: SessionPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(pref = pref) as T
    }
}