package com.rahulabrol.smsreader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rahulabrol.smsreader.R
import com.rahulabrol.smsreader.databinding.ItemEmptyBinding
import com.rahulabrol.smsreader.databinding.ItemHeaderBinding
import com.rahulabrol.smsreader.databinding.ItemSmsBinding
import com.rahulabrol.smsreader.model.DateItem
import com.rahulabrol.smsreader.model.GeneralItem
import com.rahulabrol.smsreader.model.ListItem

/**
 * Created by Rahul Abrol on 14/3/21.
 */
class SmsAdapter(val onPostClick: (GeneralItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_GENERAL = 1
        private const val TYPE_DATE = 2
    }

    private val items: MutableList<ListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_GENERAL -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    DataBindingUtil.inflate<ItemSmsBinding>(
                        inflater,
                        R.layout.item_sms,
                        parent,
                        false
                    )
                return ItemSmsViewHolder(binding)
            }
            TYPE_DATE -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding =
                    DataBindingUtil.inflate<ItemHeaderBinding>(
                        inflater,
                        R.layout.item_header,
                        parent,
                        false
                    )
                return ItemHeaderViewHolder(binding)
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

    fun addSmsList(detailList: List<ListItem>?) {
        items.clear()
        if (detailList?.isNotEmpty() == true) {
            items.addAll(detailList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemSmsViewHolder) {
            holder.bind((items[position] as GeneralItem))
        } else if (holder is ItemHeaderViewHolder) {
            holder.bind((items[position] as DateItem).date)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) {
            TYPE_EMPTY
        } else {
            if (items[position] is GeneralItem)
                TYPE_GENERAL
            else
                TYPE_DATE
        }
    }

    override fun getItemCount(): Int {
        return if (items.isEmpty()) {
            1
        } else {
            items.size
        }
    }

    inner class ItemHeaderViewHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: String) {
            binding.apply {
                this.date = date
                executePendingBindings()
            }
        }
    }

    inner class ItemSmsViewHolder(private val binding: ItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onPostClick.invoke(items[adapterPosition] as GeneralItem)
            }
        }

        fun bind(message: GeneralItem) {
            binding.apply {
                this.message = message
                executePendingBindings()
            }
        }
    }

    inner class EmptyViewHolder(binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)
}