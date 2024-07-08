package com.itsmejcb.openai.Class.Index.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Request {
    @SerializedName("text")
    @Expose
    var text: String? = null

    override fun toString(): String {
        return "Request(text=$text)"
    }
}