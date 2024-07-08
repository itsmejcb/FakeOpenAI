package com.itsmejcb.openai.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itsmejcb.openai.Class.Index.Model.Request
import com.itsmejcb.openai.Class.Index.Model.Response
import com.itsmejcb.openai.Data.Repository.API
import com.itsmejcb.openai.Data.Repository.RetroInstance
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class ViewModel : ViewModel() {
    var chatLiveData: MutableLiveData<String?> = MutableLiveData()

    fun getChatObserver(): MutableLiveData<String?> {
        return chatLiveData
    }

    @SuppressLint("CheckResult")
    fun chat(userRequest: Request) {
        val retroService = RetroInstance.getRetroInstance().create(API::class.java)
        retroService.chat(userRequest.text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getChatRx())
    }

    private fun getChatRx(): Observer<Response> {
        return object : Observer<Response> {
            override fun onSubscribe(d: Disposable) {
                Log.d("Sign IN", "onSubscribe: ")
            }

            override fun onNext(t: Response) {
                chatLiveData.postValue(t.response) // Assuming t.response is a string
                Log.d("test", "test1: $t")
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("Fetch Response", "HTTP Error occurred: ${e.code()} ${e.message()} - $errorBody")
                    }
                    is SocketTimeoutException -> {
                        Log.e("Fetch Response", "Timeout Error: ${e.message}")
                    }
                    is IOException -> {
                        Log.e("Fetch Response", "Network Error: ${e.message}")
                    }
                    else -> {
                        Log.e("Fetch Response", "Unknown Error: ${e.message}")
                    }
                }
                chatLiveData.postValue(null)
            }

            override fun onComplete() {
                Log.d("Sign IN", "onComplete: ")
            }
        }
    }
}