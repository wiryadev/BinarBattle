package com.wiryadev.binarbattle.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiryadev.binarbattle.entity.CommonResponse
import com.wiryadev.binarbattle.network.ApiClient
import com.wiryadev.binarbattle.network.NetworkUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RegisterViewModel : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error: MutableLiveData<Throwable> = MutableLiveData(null)
    val error: LiveData<Throwable> get() = _error

    private val _commonResponse: MutableLiveData<CommonResponse> = MutableLiveData()
    val commonResponse: LiveData<CommonResponse> get() = _commonResponse

    fun register(
        email: String,
        username: String,
        password: String,
    ) {
        val body = NetworkUtil.createRequestBody(
            "email" to email,
            "username" to username,
            "password" to password,
        )

        ApiClient.getApiService().register(requestBody = body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CommonResponse> {
                override fun onSubscribe(d: Disposable) {
                    _error.postValue(null)
                    _loading.postValue(true)
                }

                override fun onNext(t: CommonResponse) {
                    _commonResponse.postValue(t)
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