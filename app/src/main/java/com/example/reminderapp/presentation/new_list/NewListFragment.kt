package com.example.reminderapp.presentation.new_list

import android.os.Bundle
import android.util.Log
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
import com.example.domain.model.Group
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.presentation.interfaces.BackActionInterface
import com.example.reminderapp.presentation.interfaces.DataReceiving
import com.example.reminderapp.databinding.FragmentNewListBinding
import com.example.reminderapp.presentation.base.serializer.GroupSerializer
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.new_list.adapter.ListItemDecoration
import com.example.reminderapp.presentation.new_list.adapter.NewListAdapter
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.ImageUtils
import com.example.reminderapp.utils.setFocus
import com.example.reminderapp.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewListFragment : Fragment(), MenuProvider, BackActionInterface, DataReceiving {

    private var _binding: FragmentNewListBinding? = null
    private val binding get() = _binding!!

    private lateinit var callback: OnBackPressedCallback

    private val viewModel: NewListViewModel by viewModel()

    private var group: Group? = null
    private var groupColor: Int? = null
    private var groupImage: Int? = null
    private var groupId: Int? = null

    private lateinit var adapter: NewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewListBinding.inflate(layoutInflater, container, false)

        groupColor = ColorsUtils(requireContext()).colors[0].color
        groupColor?.let { binding.selectedColorIv.circleColor = it }

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
        receiveData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() = binding.apply {
        val colorsList = ColorsUtils(requireContext()).onlyColors
        val imagesList = ImageUtils(requireContext()).onlyImages

        adapter = NewListAdapter(
            object : NewListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (position < ColorsUtils(requireContext()).colorsListSize)
                        colorsList[position].color?.let { selectedColor ->
                            selectedColorIv.circleColor = selectedColor
                            groupColor = selectedColor
                        }
                    else {
                        imagesList[position - colorsList.size].image?.let { selectedItem ->
                            if (groupImage != null && groupImage == selectedItem) {
                                selectedColorIv.isImageVisible = false
                                groupImage = null
                            } else {
                                selectedColorIv.apply {
                                    bitmap = selectedItem
                                    isImageVisible = true
                                }
                                groupImage = selectedItem
                            }
                        }
                    }
                }
            }, requireContext()
        )
        colorsRv.adapter = adapter
        colorsRv.layoutManager = GridLayoutManager(requireContext(), 6)
        colorsRv.addItemDecoration(ListItemDecoration(6, 50, true))
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
            val receivedData = it.getSerializable(
                FragmentNavigationConstants.EDITABLE_LIST,
                Group::class.java
            )
            receivedData?.let { data ->
                group = data
                groupId = data.groupId
                binding.apply {
                    newListTip.editText?.setText(data.groupName)
                    selectedColorIv.circleColor = data.groupColor
                    data.groupImage.let { image ->
                        selectedColorIv.isImageVisible = image != R.drawable.arrow_back
                    }
                    if (selectedColorIv.isImageVisible)
                        selectedColorIv.bitmap = data.groupImage
                }
            }
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
                if (isInputValid()) with(binding) {
                    viewModel.saveList(
                        id = groupId,
                        groupName = newListEt.text.toString(),
                        groupColor = binding.selectedColorIv.circleColor,
                        groupImage = groupImage ?: R.drawable.arrow_back,
                        tasksCount = if (group != null) group!!.tasksCount else 0,
                    )
                    navigateBack()
                }
                return true
            }
        }

        return true
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            val name = newListEt.text.toString()

            if (name.isNullOrEmpty()) {
                newListEt.setFocus(requireContext())
                newListEt.error = "Имя не может быть пустым"
                return false
            } else if (groupColor == null) {
                showSnackbar(
                    "Цвет должен быть выбран",
                    requireActivity().findViewById(R.id.rootView)
                )
                return false
            }

        }
        return true
    }

}