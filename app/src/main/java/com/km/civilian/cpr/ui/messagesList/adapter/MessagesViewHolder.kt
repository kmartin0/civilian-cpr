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
            false -> setupNormalView(message)
        }

    }

    /**
     * Sets up the base view elements.
     * Sets the date, text and the message icon.
     */
    private fun setupBaseView(message: Message) {
        itemView.tvDate.text =
            DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(message.date)
        itemView.tvMessage.text = this.message?.text

        itemView.ivType.setImageResource(message.getTypeDrawableRes())
    }

    /**
     * Sets up the view for when the multi select action mode is active.
     * Sets the following ui elements:
     * - replace the message type icon with a checkbox and set it's check status
     * - makes the entire view clickable and initialize it's callback.
     * - removes menu and route navigation click.
     */
    private fun setupMultiSelectView(
        message: Message,
        isSelected: Boolean,
        selectItem: (Message) -> Unit
    ) {
        itemView.cbSelect.isChecked = isSelected
        itemView.ivMaps.setOnClickListener(null)
        itemView.tvMessage.setOnCreateContextMenuListener(null)
        itemView.viewSelect.setOnClickListener { selectItem(message) }
        itemView.viewSelect.visibility = View.VISIBLE
        itemView.ivType.visibility = View.INVISIBLE
        itemView.cbSelect.visibility = View.VISIBLE
    }

    /**
     * Sets up the view for when the multi select action mode is not active.
     * Sets the following ui elements:
     * - Add navigation callback for map icon if the message type is known.
     * - Add context menu listener on the message text.
     * - Make the message type icon visible.
     */
    private fun setupNormalView(message: Message) {
        if (message.getType() != MessageType.UNKNOWN) itemView.ivMaps.setOnClickListener {
            listener?.onNavigationClick(message)
        } else itemView.ivMaps.setOnClickListener(null)
        itemView.tvMessage.setOnCreateContextMenuListener(this)
        itemView.viewSelect.setOnClickListener(null)
        itemView.viewSelect.visibility = View.INVISIBLE
        itemView.ivType.visibility = View.VISIBLE
        itemView.cbSelect.visibility = View.INVISIBLE
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