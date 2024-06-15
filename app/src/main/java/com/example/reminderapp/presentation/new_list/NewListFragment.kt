package com.example.reminderapp.presentation.new_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.presentation.interfaces.BackActionInterface
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.databinding.FragmentNewListBinding
import com.example.reminderapp.presentation.new_list.adapter.ColorListAdapter
import com.example.reminderapp.presentation.new_list.adapter.ColorListItemDecoration
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.ColorsUtils
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewListFragment : Fragment(), MenuProvider, BackActionInterface, DataReceiving {


    companion object {
        fun newInstance() = NewListFragment()
    }

    private var _binding: FragmentNewListBinding? = null
    private val binding get() = _binding!!
    private lateinit var colorListAdapter: ColorListAdapter

    private lateinit var callback: OnBackPressedCallback


    private val viewModel: NewListViewModel by viewModel()

    var selectedColor: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewListBinding.inflate(layoutInflater, container, false)
        val activity = (activity as MainActivity)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.setToolbarTitleAndTitleColor("Добавить список")
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        colorListAdapter.submitList(ColorsUtils(requireContext()).onlyColors)

    }

    private fun setupRecyclerView() = binding.colorsRv.apply {
        colorListAdapter = ColorListAdapter(
            onItemClickListener = object : ColorListAdapter.OnItemClickListener {
                override fun onClickItem(colorItem: ColorItem) {
                    selectedColor = colorItem.color
                    colorItem.color?.let {
                        binding.selectedColorIv.circleColor = it
                    }
                }
            }
        )
        adapter = colorListAdapter
        layoutManager = GridLayoutManager(requireContext(), 6)
        this.addItemDecoration(ColorListItemDecoration(6, 50, true))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }

    override fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    override fun receiveData() {
        arguments?.let {
            // TODO fill all fields with that data
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.create_task_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                navigateBack()
                return true
            }
            R.id.action_save -> {
                // TODO add list saving method here

                return true
            }
        }

        return true
    }

}