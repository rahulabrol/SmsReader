package com.rahulabrol.smsreader.ui.smsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.rahulabrol.smsreader.HomeViewModel
import com.rahulabrol.smsreader.R
import com.rahulabrol.smsreader.base.DataBindingFragment
import com.rahulabrol.smsreader.databinding.FragmentSmsDetailBinding
import com.rahulabrol.smsreader.model.GeneralItem
import com.rahulabrol.smsreader.model.ListItem
import com.rahulabrol.smsreader.model.Message
import com.rahulabrol.smsreader.ui.adapter.SmsDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rahul Abrol on 14/3/21.
 */
@AndroidEntryPoint
class SmsDetailFragment : DataBindingFragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var binding: FragmentSmsDetailBinding

    private val postDetailAdapter by lazy(LazyThreadSafetyMode.PUBLICATION) {
        SmsDetailAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        val post = arguments?.getParcelable<Message>(POST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = binding<FragmentSmsDetailBinding>(
            inflater,
            R.layout.fragment_sms_detail,
            container
        ).apply {
            lifecycleOwner = this@SmsDetailFragment
            adapter = postDetailAdapter
            viewModel = homeViewModel
        }
        return binding.root
    }

    companion object {
        private const val POST = "post"
        fun getDataBundle(message: GeneralItem) = Bundle().apply {
            putParcelable(POST, message)
        }
    }
}