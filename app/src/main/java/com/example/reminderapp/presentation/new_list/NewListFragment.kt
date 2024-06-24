package com.example.reminderapp.presentation.new_list

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.model.Group
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.presentation.base.BackActionHandler
import com.example.reminderapp.presentation.base.DataReceiver
import com.example.reminderapp.databinding.FragmentNewListBinding
import com.example.reminderapp.presentation.base.BaseDataReceiveFragment
import com.example.reminderapp.presentation.base.OperationResult
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants.GROUP_KEY
import com.example.reminderapp.presentation.new_list.adapter.ListItemDecoration
import com.example.reminderapp.presentation.new_list.adapter.NewListAdapter
import com.example.reminderapp.utils.ColorsUtils
import com.example.reminderapp.utils.ImageUtils
import com.example.reminderapp.utils.actionIfChanged
import com.example.reminderapp.utils.setFocus
import com.example.reminderapp.utils.setPaddingToInset
import com.example.reminderapp.utils.setupToolbar
import com.example.reminderapp.utils.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewListFragment : BaseDataReceiveFragment(), MenuProvider {

    private var _binding: FragmentNewListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewListViewModel by viewModel()

    private lateinit var adapter: NewListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewListBinding.inflate(layoutInflater, container, false)

        edgeToEdge()
        setupToolbar(
            activity = activity as AppCompatActivity,
            toolbar = binding.createListToolbar,
            backEnable = true,
            title = requireContext().getString(R.string.add_list)
        )

        setupRecyclerView()

        setupObservers()

        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initListeners() {
        binding.newListEt.doAfterTextChanged(viewModel::onGroupNameChanged)
    }

//    private fun setupToolbar() = with(binding) {
//        val activity = (activity as MainActivity)
//        activity.setSupportActionBar(createListToolbar)
//
//        activity.supportActionBar?.apply {
//            setDisplayShowTitleEnabled(false)
//            setDisplayHomeAsUpEnabled(true)
//        }
//        createListToolbar.title = requireContext().getString(R.string.add_list)
//    }

    private fun edgeToEdge() = with(binding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            createListToolbar setPaddingToInset WindowInsetsCompat.Type.statusBars()
        }
    }

    private fun setupRecyclerView() = binding.apply {
        val colorsList = ColorsUtils(requireContext()).onlyColors
        val imagesList = ImageUtils(requireContext()).onlyImages

        adapter = NewListAdapter(
            object : NewListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    if (position < ColorsUtils(requireContext()).colorsListSize)
                        colorsList[position].color?.let { selectedColor ->
//                            selectedColorIv.circleColor = selectedColor
                            viewModel.onGroupColorChanged(selectedColor)
                        }
                    else {
                        imagesList[position - colorsList.size].image?.let { selectedItem ->
                            if (viewModel.isImageInState(selectedItem)) {
                                viewModel.onGroupImageVisibilityChanged(false)
                                viewModel.onGroupImageChanged(null)
//                                selectedColorIv.isImageVisible = false
//                                groupImage = null
                            } else {
                                viewModel.onGroupImageChanged(selectedItem)
                                viewModel.onGroupImageVisibilityChanged(true)
//                                selectedColorIv.apply {
//                                    bitmap = selectedItem
//                                    isImageVisible = true
//                                }
//                                groupImage = selectedItem
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

    private fun setupObservers() {
        observeScreenState()
        observeOperationResult()
        observeValidationResult()
    }

    private fun observeScreenState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState
                    .collect { currentState ->
                        with(binding) {
                            newListEt.actionIfChanged(currentState.groupName) {
                                newListEt.setText(currentState.groupName)
                                newListEt.setSelection(currentState.groupName.length)
                            }
                            selectedColorIv.actionIfChanged(currentState.groupColor) {
                                selectedColorIv.circleColor = currentState.groupColor
                            }
                            selectedColorIv.actionIfChanged(currentState.isImageVisible) {
                                selectedColorIv.isImageVisible = currentState.isImageVisible
                            }
                            selectedColorIv.actionIfChanged(currentState.groupImage) {
                                selectedColorIv.bitmap = currentState.groupImage
                            }
                        }
                    }
            }
        }
    }

    private fun observeOperationResult() {
        lifecycleScope.launch {
            viewModel.saveResult.collect {
                when (it) {
                    is OperationResult.Error -> Unit //TODO Добавить обработку ошибок
                    OperationResult.Loading -> Unit
                    OperationResult.NotStarted -> Unit
                    is OperationResult.Success -> {
                        setFragmentResult(
                            GROUP_KEY,
                            bundleOf(
                                GROUP_KEY to it.data
                            )
                        )
                        navigateBack()
                    }
                }
            }
        }
    }

    private fun observeValidationResult() {
        lifecycleScope.launch {
            viewModel.validationResult.collect {
                when (it) {
                    ValidationResult.IncorrectColor -> {
                        showSnackbar(
                            "Цвет должен быть выбран",
                            requireActivity().findViewById(R.id.rootView)
                        )
                    }
                    ValidationResult.IncorrectName -> {
                        binding.newListEt.setFocus(requireContext())
                        binding.newListEt.error = "Имя не может быть пустым"
                    }
                    ValidationResult.NotStarted -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(this)
    }

    override fun receiveData() {
        var receivedData: Group? = null
        arguments?.let {
            receivedData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(
                    FragmentNavigationConstants.EDITABLE_LIST,
                    Group::class.java
                )
            } else {
                it.getSerializable(
                    FragmentNavigationConstants.EDITABLE_LIST
                ) as Group?


//            receivedData?.let { data ->
//                group = data
//                groupId = data.groupId
//                binding.apply {
//                    newListTip.editText?.setText(data.groupName)
//                    selectedColorIv.circleColor = data.groupColor
//                    data.groupImage?.let { image -> selectedColorIv.bitmap = image }
//                }
//            }
            }
        }
        viewModel.onGroupIdChanged(receivedData?.groupId)
        viewModel.onGroupNameChanged(receivedData?.groupName ?: "")
        viewModel.onGroupColorChanged(receivedData?.groupColor ?: ColorsUtils(requireContext()).colors[0].color)
        viewModel.onGroupImageChanged(receivedData?.groupImage)

        viewModel.onGroupTasksCountChanged(receivedData?.tasksCount ?: 0)
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
                viewModel.saveList()
                return true
            }
        }

        return true
    }

}