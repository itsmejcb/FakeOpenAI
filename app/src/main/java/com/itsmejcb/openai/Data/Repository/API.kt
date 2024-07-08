package com.itsmejcb.openai.Data.Repository

import com.itsmejcb.openai.Class.Index.Model.Response
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface API {
    @GET("openchat.php")
    fun chat(
        @Query("text") text: String?
    ): Observable<Response>
}