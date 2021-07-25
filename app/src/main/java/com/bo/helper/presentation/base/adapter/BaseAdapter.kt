package com.bo.helper.presentation.base.adapter

import android.util.SparseBooleanArray
import androidx.core.util.contains
import androidx.core.util.keyIterator
import androidx.recyclerview.widget.RecyclerView
import com.bo.helper.presentation.base.adapter.viewholder.BaseViewHolder
import com.bo.helper.presentation.base.listener.OnCheckBoxSelectionListener
import com.bo.helper.presentation.base.listener.OnSelectionListener
import kotlin.math.absoluteValue

abstract class BaseAdapter<In, Vh : BaseViewHolder<In>>(
    var list: MutableList<In> = arrayListOf(),
    var onSelectionListener: OnSelectionListener<In>? = null
) : RecyclerView.Adapter<Vh>(), OnCheckBoxSelectionListener<In> {

    private var forceUpdate: Boolean = false
    private val selectedItems = SparseBooleanArray()
    var maxSelectedItems = -1
    var isInChoiceMode: Boolean = true
        set(value) {
            field = value
            list.takeIf { !value }
                ?.let {
                    selectedItems.clear()
                    notifyItemRangeChanged(0, it.size, false)
                }
            if (!value) {
                selectedItems.clear()
                notifyItemRangeChanged(0, list.size, false)
            }
        }
    var selectedType: SelectedType = SelectedType.None

    enum class SelectedType {
        None,
        Single,
        Multi
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        getItem(position)?.let { holder.bind(it, selectedItems.get(position), {}) }
    }

    override fun onBindViewHolder(holder: Vh, position: Int, payloads: MutableList<Any>) {
        val isSelected = payloads.takeIf { it.isNotEmpty() }?.get(0) as? Boolean
            ?: selectedItems.contains(position)
        getItem(position)?.let { holder.bind(it, isSelected, {}) }
    }

    override fun selectionClick(vh: BaseViewHolder<In>, checked: Boolean) {
        val position = vh.absoluteAdapterPosition
        when {
            selectedType == SelectedType.None -> return
            selectedType == SelectedType.Single -> {
                clearSelectedState()
                if (checked) selectedItems.put(position, checked)
                vh.isChecked = checked
                notifyItemChanged(position, checked)
                updateSelectionListener(position, checked)
            }
            selectedType == SelectedType.Multi && maxSelectedItems == -1 || maxSelectedItems >= selectedItems.size() && checked.not() -> {
                selectedItems.delete(position)
                if (checked) selectedItems.put(position, true)
                vh.isChecked = checked
                val isInChoiceMode = selectedItems.size() != 0
                if (this.isInChoiceMode != isInChoiceMode) {
                    this.isInChoiceMode = isInChoiceMode
                }
                notifyItemChanged(position, checked)
                updateSelectionListener(position, checked)
            }
            maxSelectedItems > selectedItems.size() -> {
                selectedItems.delete(position)
                vh.isChecked = checked
                if (checked) selectedItems.put(position, true)
                val isInChoiceMode = selectedItems.size() != 0
                if (this.isInChoiceMode != isInChoiceMode) {
                    this.isInChoiceMode = isInChoiceMode
                }
                notifyItemChanged(position, checked)
                updateSelectionListener(position, checked)
            }
        }
    }

    private fun setSelectedElement(position: Int, checked: Boolean) {
        selectedItems.put(position, checked)
        updateSelectionListener(position, checked)
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateSelectionListener(position: Int, checked: Boolean) {
        if (!forceUpdate) {
            onSelectionListener?.let {
                it.onItemSelected(list[position], checked)
                it.onItemsSelected(getSelectedItems())
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItems(): List<In> = list

    open fun setItems(list: List<In>?) {
        list?.let {
            this.list = it.toMutableList()
            notifyDataSetChanged()
        }
    }

    fun updateItems(list: List<In>?) {
        list?.let {
            this.list.clear()
            this.list.addAll(it)
        }
        notifyDataSetChanged()
    }

    open fun getItem(position: Int): In? = list.getOrNull(position)

    fun getSelectedItems(): List<In> {
        val items = arrayListOf<In>()
        for (i in 0 until selectedItems.size()) {
            val data = list[selectedItems.keyAt(i)]
            data?.let { items.add(it) }
        }
        return items
    }

    fun setSelectedItems(categories: List<In>) {
        forceUpdate = true
        categories.forEach { item ->
            list.indexOf(item)
                .takeIf { it > -1 }
                ?.let {
                    setSelectedElement(it, true)
                }
        }
        forceUpdate = false
    }

    fun getIdsSelectedItems(): List<Int> {
        val items = arrayListOf(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun clearSelectedState() {
        for (position in selectedItems.keyIterator()) {
            notifyItemChanged(position.absoluteValue, false)
        }
        selectedItems.clear()
    }

    fun clearSelectedStateAll() {
        val selection = getIdsSelectedItems()
        selectedItems.clear()
        for (position in selection) {
            notifyItemChanged(position, false)
            updateSelectionListener(position, false)
        }
    }

    open fun updateItem(item: In, position: Int) {
        list[position] = item
        notifyItemChanged(position)
    }

    open fun updateItem(item: In) = findIndex(item)?.let { updateItem(item, it) }

    open fun removeItem(item: In) {
        findIndex(item)?.let { position ->
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    open fun changeItem(item: In, payload: Any) {
        findIndex(item)?.let { notifyItemChanged(it, payload) }
    }

    open fun addItem(position: Int, item: In) {
        list.add(position, item)
        notifyItemInserted(position)
    }

    open fun addItems(position: Int, items: List<In>) {
        list.addAll(position, items)
        notifyItemRangeInserted(position, list.size)
    }

    open fun addItems(items: MutableList<In>) {
        val index = list.lastIndex
        list.addAll(items)
        notifyItemRangeChanged(index, list.size)
    }

    private fun findIndex(item: In): Int? =
        list.indexOfFirst { it == item }.takeIf { it != RecyclerView.NO_POSITION }
}