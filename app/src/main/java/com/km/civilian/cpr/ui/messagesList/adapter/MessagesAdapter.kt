package com.km.civilian.cpr.ui.messagesList.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.km.civilian.cpr.R
import com.km.civilian.cpr.model.Message

class MessagesAdapter(
    private val messages: MutableList<Message>,
    private val messageClickListener: MessageClickListener
) : RecyclerView.Adapter<MessagesViewHolder>(), ActionMode.Callback {

    private var multiSelect = false
    private var selectedItems = mutableSetOf<Int>()
    private var actionMode: ActionMode? = null

    init {
        setHasStableIds(true)
    }

    fun updateMessageList(newList: List<Message>) {
        val diffCallback = MessageDiffCallback(messages, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        messages.clear()
        messages.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        )
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) =
        holder.bindView(
            messages[position],
            messageClickListener,
            selectedItems.contains(messages[position].id),
            ::toggleMessageSelected,
            multiSelect
        )

    override fun getItemId(position: Int): Long = messages[position].id.toLong()


    /**
     * If multi select is active then toggle the selection of [message] by adding or removing it
     * from [selectedItems]. Update ui by notifying the adapter the [message] has changed and invalidating
     * the action mode which updates the title with number of selections.
     */
    fun toggleMessageSelected(message: Message) {

        if (multiSelect) {
            if (selectedItems.contains(message.id)) selectedItems.remove(message.id)
            else selectedItems.add(message.id)

            notifyItemChanged(messages.indexOfFirst { it.id == message.id })

            actionMode?.invalidate()
        }

    }

    /**
     * Set the adapter internal multi selection state.
     */
    fun setMultiSelectMode(multiSelect: Boolean) {
        this.multiSelect = multiSelect
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // Inflate the menu resource providing context menu items
        val inflater: MenuInflater? = mode?.menuInflater
        inflater?.inflate(R.menu.message_contextual_action_menu, menu)
        notifyDataSetChanged()
        this.actionMode = mode

        return true
    }

    /**
     * Update the menu title with the number of items selected in [selectedItems]
     */
    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.title = "${selectedItems.size} items"
        return true
    }

    /**
     * Called when a menu item from the action menu is clicked
     */
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        // On delete click, call the listeners' onDelete function.
        if (item?.itemId == R.id.action_delete) {
            messageClickListener.onDelete(messages.filter { selectedItems.contains(it.id) })
            mode?.finish()
        }

        // On select all click, toggle the selection checkboxes based on current state.
        if (item?.itemId == R.id.action_select_all) {
            toggleSelectAll()
        }
        return true
    }

    /**
     * Remove the action mode by setting [multiSelect] to false, clearing the [selectedItems] list
     * and setting the [actionMode] object to null.
     *
     * Update ui by notifying the adapter the data set has changed.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroyActionMode(mode: ActionMode?) {
        setMultiSelectMode(false)
        selectedItems.clear()
        this.actionMode = null

        notifyDataSetChanged()
    }

    /**
     * Select all items if not all items are already selected.
     * If all items were already selected, remove all selections.
     *
     * Update the ui by invalidating the action mode and notifying the adapter that the data set has changed.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun toggleSelectAll() {
        if (selectedItems.size != messages.size) {
            selectedItems.clear()
            selectedItems.addAll(messages.map { it.id })
        } else {
            selectedItems.clear()
        }

        notifyDataSetChanged()
        actionMode?.invalidate()

    }

}