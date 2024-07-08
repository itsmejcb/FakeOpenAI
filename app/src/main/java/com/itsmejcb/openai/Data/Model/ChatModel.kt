package com.itsmejcb.openai.Data.Model

data class ChatModel(
    private val role: String,
    private var response: String,
    var isLoading: Boolean = false
) {
    fun getRole() = role
    fun getResponse() = response
    fun setResponse(newResponse: String) {
        response = newResponse
    }
}
