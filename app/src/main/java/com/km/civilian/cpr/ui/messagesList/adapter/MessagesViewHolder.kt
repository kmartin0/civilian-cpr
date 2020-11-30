package com.km.civilian.cpr.ui.messagesList.adapter

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.civilian.cpr.R
import com.km.civilian.cpr.enum.MessageType
import com.km.civilian.cpr.model.Message
import kotlinx.android.synthetic.main.item_message.view.*
import java.text.DateFormat

class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnCreateContextMenuListener {

    private var listener: MessageClickListener? = null
    private var message: Message? = null

    /**
     * Bind the view to the Message.
     *
     * @param message Message containing the view data.
     * @param messageClickListener listener for interaction callbacks.
     * @param isSelected Boolean to define if the message is selected.
     * @param selectItem Callback for when the view is selected.
     * @param multiSelect Boolean to define if the multi select action mode is active.
     */
    fun bindView(
        message: Message,
        messageClickListener: MessageClickListener,
        isSelected: Boolean,
        selectItem: (Message) -> Unit,
        multiSelect: Boolean
    ) {
        this.listener = messageClickListener
        this.message = message

        setupBaseView(message)

        when (multiSelect) {
            true -> setupMultiSelectView(message, isSelected, selectItem)
            false -> setupNormalView()
        }
    }

    /**
     * Sets up the base view elements which do not change during multi or regular view switch.
     */
    private fun setupBaseView(message: Message) {
        itemView.tvDate.text =
            DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(message.date)
        itemView.tvMessage.text = this.message?.text

        itemView.ivType.setImageResource(message.getTypeDrawableRes())
        itemView.tvMessage.setOnCreateContextMenuListener(this)

        if (message.getType() != MessageType.UNKNOWN) {
            itemView.ivMaps.setOnClickListener { listener?.onNavigationClick(message) }
            itemView.ivMaps.visibility = View.VISIBLE
        } else {
            itemView.ivMaps.setOnClickListener(null)
            itemView.ivMaps.visibility = View.INVISIBLE
        }
    }

    /**
     * Sets up the view for when the multi select action mode is active.
     */
    private fun setupMultiSelectView(
        message: Message,
        isSelected: Boolean,
        toggleItemSelect: (Message) -> Unit
    ) {
        // Remove message icon
        itemView.ivType.visibility = View.INVISIBLE

        // Add checkbox, state and listener
        itemView.cbSelect.isChecked = isSelected
        itemView.cbSelect.visibility = View.VISIBLE
        itemView.cbSelect.setOnCheckedChangeListener { _, _ -> toggleItemSelect(message) }

        // Add overlay so user can select entire item
        itemView.viewSelect.setOnClickListener {
            itemView.cbSelect.isChecked = !itemView.cbSelect.isChecked
        }
        itemView.viewSelect.visibility = View.VISIBLE
    }

    /**
     * Sets up the view for when the multi select action mode is not active.
     */
    private fun setupNormalView() {
        // Add message icon.
        itemView.ivType.visibility = View.VISIBLE

        // Remove Checkbox.
        itemView.cbSelect.visibility = View.INVISIBLE
        itemView.cbSelect.setOnCheckedChangeListener(null)

        // Remove item checkbox listener overlay.
        itemView.viewSelect.setOnClickListener(null)
        itemView.viewSelect.visibility = View.INVISIBLE
    }

    /**
     * Connect the callbacks to the menu item listeners.
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val inflater = MenuInflater(v?.context)
        inflater.inflate(R.menu.message_context_menu, menu)

        menu?.apply {
            message?.let { msg ->
                findItem(R.id.copy)?.setOnMenuItemClickListener {
                    listener?.onCopy(msg)
                    true
                }

                findItem(R.id.delete)?.setOnMenuItemClickListener {
                    listener?.onStartDeleteActionMode(msg)
                    true
                }

                findItem(R.id.share)?.setOnMenuItemClickListener {
                    listener?.onShare(msg)
                    true
                }
            }

        }
    }

}