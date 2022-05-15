package com.example.shikimoriandroid.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.databinding.FilterCheckBoxItemBinding
import com.example.shikimoriandroid.databinding.FilterRadioItemBinding
import com.example.shikimoriandroid.data.model.FilterItem

class FilterRecyclerAdapter(
    private val values: List<FilterItem>,
    private val layout: Int
) : RecyclerView.Adapter<FilterRecyclerAdapter.FilterItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return FilterItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterItemViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size

    inner class FilterItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = when (layout) {
            R.layout.filter_check_box_item -> FilterCheckBoxItemBinding.bind(view)
            R.layout.filter_radio_item -> FilterRadioItemBinding.bind(view)
            else -> FilterCheckBoxItemBinding.bind(view)
        }

        fun bind(item: FilterItem) {
            when (binding) {
                is FilterCheckBoxItemBinding -> {
                    binding.checkbox.isChecked = item.checked
                    binding.checkboxText.text = item.text
                }
                is FilterRadioItemBinding -> {
                    binding.radio.isChecked = item.checked
                    binding.radioText.text = item.text
                }
            }
        }
    }


}