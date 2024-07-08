package com.itsmejcb.openai.Class.Index


import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itsmejcb.openai.R
import com.itsmejcb.openai.Class.Index.Model.Request
import com.itsmejcb.openai.Data.Adapter.ChatAdapter
import com.itsmejcb.openai.Data.Model.ChatModel
import com.itsmejcb.openai.Interface.Interface
import com.itsmejcb.openai.ViewModel.ViewModel

class MainActivity : AppCompatActivity(), Interface.View {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var editText: EditText
    private lateinit var imageView: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var mainContainer1: LinearLayout
    private lateinit var mainContainer2: LinearLayout
    private lateinit var viewModel: ViewModel
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize(savedInstanceState)
        initializeLogic()
        initViewModel()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        presenter = Presenter(this, this)
        recyclerView = findViewById(R.id.recyclerViewChat)
        mainContainer1 = findViewById(R.id.mainContainer1)
        mainContainer2 = findViewById(R.id.mainContainer2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        editText = findViewById(R.id.chatMessage)
        imageView = findViewById(R.id.imageView)
        imageView4 = findViewById(R.id.imageView4)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)
    }

    private fun initViewModel() {
        viewModel.getChatObserver().observe(this) { response ->
            response?.let {
                // Find the last message that is still loading and update it
                val position = chatAdapter.itemCount - 1
                val loadingChat = chatAdapter.chats[position]
                loadingChat.setResponse(it)
                loadingChat.isLoading = false
                chatAdapter.updateChat(position, loadingChat)
            }
        }
    }
    private fun initializeLogic() {
        chatAdapter = ChatAdapter(this, ArrayList())
        recyclerView.adapter = chatAdapter
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        imageView.setOnClickListener {
            val userMessage = editText.text.toString()
            if (userMessage.isNotEmpty()) {
                mainContainer1.visibility = android.view.View.GONE
                mainContainer2.visibility = android.view.View.VISIBLE
                val userChat = ChatModel("user", userMessage)
                chatAdapter.addChat(userChat)

                val loadingChat = ChatModel("ai", "", true)
                chatAdapter.addChat(loadingChat)

                chat()
            }
        }
        imageView4.setOnClickListener {
            editText.text.clear()
            mainContainer1.visibility = android.view.View.VISIBLE
            mainContainer2.visibility = android.view.View.GONE
            chatAdapter.clearData()
            chatAdapter.notifyDataSetChanged()
        }
    }

    private fun chat() {
        val request = Request().apply {
            text = editText.text.toString()
        }
        viewModel.chat(request)
        editText.text.clear()
    }
}
