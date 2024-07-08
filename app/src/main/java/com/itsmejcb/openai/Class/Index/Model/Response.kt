package com.itsmejcb.openai.Class.Index.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    override fun toString(): String {
        return "Response(message=$message, response=$response)"
    }
}