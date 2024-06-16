package com.example.reminderapp.presentation.new_list

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.presentation.interfaces.BackActionInterface
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.databinding.FragmentNewListBinding
import com.example.reminderapp.presentation.new_list.adapter.ColorListAdapter
import com.example.reminderapp.presentation.new_list.adapter.ImageListAdapter
import com.example.reminderapp.presentation.new_list.adapter.ListItemDecoration
import com.example.reminderapp.utils.ColorItem
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.ImageItem
import com.example.reminderapp.utils.ImageUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewListFragment : Fragment(), MenuProvider, BackActionInterface, DataReceiving {

    private var _binding: FragmentNewListBinding? = null
    private val binding get() = _binding!!
    private lateinit var colorListAdapter: ColorListAdapter
    private lateinit var imageListAdapter: ImageListAdapter

    private lateinit var callback: OnBackPressedCallback

    private val viewModel: NewListViewModel by viewModel()

    var selectedColor: Int? = null
    var selectedImage: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        setupRecyclerView()
        colorListAdapter.submitList(ColorsUtils(requireContext()).onlyColors)
        imageListAdapter.submitList(ImageUtils(requireContext()).onlyImages)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner , Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() = binding.apply {
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
        colorsRv.adapter = colorListAdapter
        colorsRv.layoutManager = GridLayoutManager(requireContext(), 6)
        colorsRv.addItemDecoration(ListItemDecoration(6, 50, true))

        imageListAdapter = ImageListAdapter(
            object : ImageListAdapter.OnItemClickListener {
                override fun onClickItem(imageItem: ImageItem) {
                    selectedImage = imageItem.image
                }
            }
        )
        imagesRecyclerView.adapter = imageListAdapter
        imagesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
        imagesRecyclerView.addItemDecoration(ListItemDecoration(6, 50, true))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(this)
        (activity as MainActivity).setToolbarTitleAndTitleColor("")
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