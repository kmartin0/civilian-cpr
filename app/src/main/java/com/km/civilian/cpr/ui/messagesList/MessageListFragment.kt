package com.km.civilian.cpr.ui.messagesList

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.km.civilian.cpr.R
import com.km.civilian.cpr.base.BaseMVVMFragment
import com.km.civilian.cpr.databinding.FragmentMessageListBinding
import com.km.civilian.cpr.model.Message
import com.km.civilian.cpr.ui.messagesList.adapter.MessageClickListener
import com.km.civilian.cpr.ui.messagesList.adapter.MessageItemDecoration
import com.km.civilian.cpr.ui.messagesList.adapter.MessagesAdapter
import com.km.civilian.cpr.util.MapsUtils
import kotlinx.android.synthetic.main.fragment_message_list.*


class MessageListFragment : BaseMVVMFragment<FragmentMessageListBinding, MessageListViewModel>(),
    MessageClickListener {

    private val messages = arrayListOf<Message>()
    private val messagesAdapter = MessagesAdapter(messages, this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    /**
     * Initialize the recycler view.
     */
    private fun initViews() {
        rvMessages.adapter = messagesAdapter
        rvMessages.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessages.addItemDecoration(MessageItemDecoration(requireContext()))
        registerForContextMenu(rvMessages)
    }

    /**
     * Initialize the observers.
     */
    private fun initObservers() {
        // Observe the messages LiveData. Re-add them to the messages list, notify the adapter and scroll to the last message.
        viewModel.messages.observe(viewLifecycleOwner, Observer {
            messages.clear()
            messages.addAll(it)
            messagesAdapter.notifyDataSetChanged()
            rvMessages.scrollToPosition(messages.size.minus(1))
        })
    }

    /**
     * Open Google Maps with a route for [message].
     */
    override fun onNavigationClick(message: Message) {
        startActivity(MapsUtils.getMapsIntentForMessage(message))
    }

    /**
     * Delete [messages] from the database.
     */
    override fun onDelete(messages: List<Message>) {
        viewModel.deleteMessages(messages)

        // Open snackbar with success message and undo option.
        Snackbar.make(rvMessages, getString(R.string.message_deleted_message), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) { _ ->
                viewModel.insertMessages(messages)
            }
    }

    /**
     * Start the delete action mode by setting it in the adapter and starting the
     * parent activity's action mode. Also set the current [message] selected.
     */
    override fun onStartDeleteActionMode(message: Message) {
        messagesAdapter.setMultiSelectMode(true)
        requireActivity().startActionMode(messagesAdapter)
        messagesAdapter.setMessageSelected(message)
    }

    /**
     * Start share intent for [message] text
     */
    override fun onShare(message: Message) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message.text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.message_share))
        startActivity(shareIntent)
    }

    /**
     * Copy [message] text to clipboard.
     */
    override fun onCopy(message: Message) {
        (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText(getString(R.string.msg_clipboard_label), message.text)

        )
        Toast.makeText(
            requireContext(),
            getString(R.string.copy_success_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun initViewModelBinding() {
        binding.viewModel = viewModel
    }

    override fun getVMClass(): Class<MessageListViewModel> = MessageListViewModel::class.java

    override fun getLayoutId(): Int = R.layout.fragment_message_list

}