package com.km.civilian.cpr.ui.messagesList.adapter

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.civilian.cpr.R
import com.km.civilian.cpr.databinding.ItemMessageBinding
import com.km.civilian.cpr.enum.MessageType
import com.km.civilian.cpr.model.Message
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

        val binding = ItemMessageBinding.bind(itemView)

        setupBaseView(binding)

        when (multiSelect) {
            true -> setupMultiSelectView(message, binding, isSelected, selectItem)
            false -> setupNormalView(binding)
        }
    }

    /**
     * Sets up the base view elements which do not change during multi or regular view switch.
     */
    private fun setupBaseView(binding: ItemMessageBinding) {
        with(binding) {
            message?.let {
                tvDate.text =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM)
                        .format(it.date)
                tvMessage.text = message?.text

                ivType.setImageResource(it.getTypeDrawableRes())
                tvMessage.setOnCreateContextMenuListener(this@MessagesViewHolder)

                if (it.getType() != MessageType.UNKNOWN) {
                    ivMaps.setOnClickListener { _ -> listener?.onNavigationClick(it) }
                    ivMaps.visibility = View.VISIBLE
                } else {
                    ivMaps.setOnClickListener(null)
                    ivMaps.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * Sets up the view for when the multi select action mode is active.
     */
    private fun setupMultiSelectView(
        message: Message,
        binding: ItemMessageBinding,
        isSelected: Boolean,
        toggleItemSelect: (Message) -> Unit
    ) {
        with(binding) {
            // Remove message icon
            ivType.visibility = View.INVISIBLE

            // Add checkbox, state and listener
            cbSelect.isChecked = isSelected
            cbSelect.visibility = View.VISIBLE

            // Add overlay so user can select entire item
            viewSelect.setOnClickListener {
                toggleItemSelect(message)
            }
            viewSelect.visibility = View.VISIBLE
        }
    }

    /**
     * Sets up the view for when the multi select action mode is not active.
     */
    private fun setupNormalView(binding: ItemMessageBinding) {
        with(binding) {
            // Add message icon.
            ivType.visibility = View.VISIBLE

            // Remove Checkbox.
            cbSelect.visibility = View.INVISIBLE
            cbSelect.setOnCheckedChangeListener(null)

            // Remove item checkbox listener overlay.
            viewSelect.setOnClickListener(null)
            viewSelect.visibility = View.INVISIBLE
        }
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