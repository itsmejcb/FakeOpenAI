package com.itsmejcb.openai.Data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.itsmejcb.openai.Data.Model.ChatModel
import com.itsmejcb.openai.R

class ChatAdapter(private val context: Context, val chats: ArrayList<ChatModel>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.custom_view_chat, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]

        if (chat.getRole() == "user") {
            holder.textView1.text = chat.getResponse()
            holder.linearLayout1.visibility = View.VISIBLE
            holder.linearLayout2.visibility = View.GONE
            holder.linearLayout3.visibility = View.GONE
        } else {
            if (chat.isLoading) {
                holder.linearLayout1.visibility = View.GONE
                holder.linearLayout2.visibility = View.GONE
                holder.linearLayout3.visibility = View.VISIBLE
                holder.shimmer_view_container.startShimmer()
            } else {
                holder.textView2.text = chat.getResponse()
                holder.linearLayout1.visibility = View.GONE
                holder.linearLayout2.visibility = View.VISIBLE
                holder.linearLayout3.visibility = View.GONE
                holder.shimmer_view_container.stopShimmer()
            }
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun addChat(chat: ChatModel) {
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    fun updateChat(position: Int, chat: ChatModel) {
        chats[position] = chat
        notifyItemChanged(position)
    }
    fun clearData() {
        chats.clear()
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView1: TextView = v.findViewById(R.id.textView1)
        val textView2: TextView = v.findViewById(R.id.textView2)
        val linearLayout1: LinearLayout = v.findViewById(R.id.linearLayout1)
        val linearLayout2: LinearLayout = v.findViewById(R.id.linearLayout2)
        val linearLayout3: LinearLayout = v.findViewById(R.id.linearLayout3)
        val shimmer_view_container: ShimmerFrameLayout = v.findViewById(R.id.shimmer_view_container)
    }
}