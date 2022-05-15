package com.example.shikimoriandroid.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shikimoriandroid.R
import com.example.shikimoriandroid.ui.adapters.FilterRecyclerAdapter
import com.example.shikimoriandroid.databinding.FragmentFilterPagerItemBinding
import com.example.shikimoriandroid.data.model.FilterItem

class FilterPagerItemFragment : Fragment() {

    private var _binding: FragmentFilterPagerItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var filterList: MutableList<FilterItem>
    private var position = 0
    private var layout = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterPagerItemBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        filterList = mutableListOf(
            FilterItem(false, "what"),
            FilterItem(false, "what1"),
            FilterItem(false, "what2"),
            FilterItem(false, "what3"),
            FilterItem(true, "what4"),
            FilterItem(false, "what5"),
            FilterItem(false, "what6")
        )

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            position = getInt(ARG_OBJECT)
        }
        if (position == 0) {
            layout = R.layout.filter_radio_item
        } else layout = R.layout.filter_check_box_item
        val adapter = FilterRecyclerAdapter(filterList, layout)
        val layoutManager = LinearLayoutManager(activity)
        binding.filterRecycler.adapter = adapter
        binding.filterRecycler.layoutManager = layoutManager
        return binding.root
    }

    companion object {
        val ARG_OBJECT = "ARG"
    }

}