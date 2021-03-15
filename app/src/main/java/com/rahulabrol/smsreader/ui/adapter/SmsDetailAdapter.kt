package com.rahulabrol.smsreader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rahulabrol.smsreader.R
import com.rahulabrol.smsreader.databinding.ItemEmptyBinding
import com.rahulabrol.smsreader.databinding.ItemSmsDetailBinding
import com.rahulabrol.smsreader.model.Message

/**
 * Created by Rahul Abrol on 14/3/21.
 */
class SmsDetailAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_CLASS = 1
    }

    private val items: MutableList<Message> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_CLASS -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    DataBindingUtil.inflate<ItemSmsDetailBinding>(
                        inflater,
                        R.layout.item_sms_detail,
                        parent,
                        false
                    )
                return ItemPostDetailViewHolder(binding)
            }
            else -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    DataBindingUtil.inflate<ItemEmptyBinding>(
                        inflater,
                        R.layout.item_empty,
                        parent,
                        false
                    )
                return EmptyViewHolder(binding)
            }
        }
    }

    fun addPostList(detailList: List<Message>?) {
        items.clear()
        if (detailList?.isNotEmpty() == true) {
            items.addAll(detailList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemPostDetailViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) {
            VIEW_TYPE_EMPTY
        } else {
            VIEW_TYPE_CLASS
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) {
            1
        } else {
            items.size
        }
    }

    inner class ItemPostDetailViewHolder(private val binding: ItemSmsDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Message) {
            binding.apply {
                this.post = post
                executePendingBindings()
            }
        }
    }

    inner class EmptyViewHolder(binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)
}