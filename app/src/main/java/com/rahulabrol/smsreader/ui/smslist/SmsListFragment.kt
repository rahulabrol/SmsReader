package com.rahulabrol.smsreader.ui.smslist

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.rahulabrol.smsreader.HomeViewModel
import com.rahulabrol.smsreader.R
import com.rahulabrol.smsreader.base.DataBindingFragment
import com.rahulabrol.smsreader.databinding.FragmentSmsListBinding
import com.rahulabrol.smsreader.model.DateItem
import com.rahulabrol.smsreader.model.GeneralItem
import com.rahulabrol.smsreader.model.ListItem
import com.rahulabrol.smsreader.model.Message
import com.rahulabrol.smsreader.ui.adapter.SmsAdapter
import com.rahulabrol.smsreader.ui.smsdetail.SmsDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.math.abs


/**
 * Created by Rahul Abrol on 14/3/21.
 */
@AndroidEntryPoint
class SmsListFragment : DataBindingFragment() {

    @VisibleForTesting
    val homeViewModel: HomeViewModel by viewModels()
    lateinit var binding: FragmentSmsListBinding

    private val postAdapter by lazy(LazyThreadSafetyMode.PUBLICATION) {
        SmsAdapter { post ->
            val bundle = SmsDetailFragment.getDataBundle(post)
            findNavController().navigate(
                R.id.action_smsListFragment_to_smsDetailFragment, bundle
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = binding<FragmentSmsListBinding>(
            inflater,
            R.layout.fragment_sms_list,
            container
        ).apply {
            lifecycleOwner = this@SmsListFragment
            viewModel = homeViewModel
            adapter = postAdapter
        }
        return binding.root
    }

    override fun setUpViews() {
        super.setUpViews()
        Permissions.check(
            requireActivity() /*context*/,
            Manifest.permission.READ_SMS,
            null /*options*/,
            object : PermissionHandler() {
                override fun onGranted() {

                    Permissions.check(
                        requireActivity() /*context*/,
                        Manifest.permission.RECEIVE_SMS,
                        null /*options*/,
                        object : PermissionHandler() {
                            override fun onGranted() {
                                update()
                            }

                            override fun onDenied(
                                context: Context?,
                                deniedPermissions: java.util.ArrayList<String>?
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    "Permission denied",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })

                }

                override fun onDenied(
                    context: Context?,
                    deniedPermissions: java.util.ArrayList<String>?
                ) {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun update() {
        val smsList = readSmsFromPhone()
        val extractedList = homeViewModel.getExtractedList(smsList)
        postAdapter.addSmsList(extractedList)
    }

    private fun readSmsFromPhone(): MutableList<Message> {
        val smsList: MutableList<Message> = ArrayList()
        val c: Cursor? = requireContext().contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val totalSMS: Int
        if (c != null) {
            totalSMS = c.count
            if (c.moveToFirst()) {
                for (j in 0 until totalSMS) {
                    val smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val dateFormat = Date(java.lang.Long.valueOf(smsDate))
                    var type: String
                    when (c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)).toInt()) {
                        Telephony.Sms.MESSAGE_TYPE_INBOX -> type = "inbox"
                        Telephony.Sms.MESSAGE_TYPE_SENT -> type = "sent"
                        Telephony.Sms.MESSAGE_TYPE_OUTBOX -> type = "outbox"
                        else -> {
                        }
                    }
                    val message = Message(number, body, dateFormat)
                    smsList.add(message)
                    c.moveToNext()
                }
            }
            c.close()
        } else {
            Toast.makeText(requireContext(), "No message to show!", Toast.LENGTH_SHORT).show()
        }
        return smsList
    }

    companion object {
        const val TAG = "SmsListFragment"
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 110
    }

}