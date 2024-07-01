package com.example.reminderapp.presentation.edit_lists

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.model.Group
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentEditListsBinding
import com.example.reminderapp.presentation.base.NavigationFragment
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.navigation.FragmentNavigationConstants
import com.example.reminderapp.navigation.NavigationDestinations
import com.example.reminderapp.presentation.adapter.GroupListRecyclerViewAdapter
import com.example.reminderapp.utils.setPaddingToInset
import com.example.reminderapp.utils.setupToolbar
import com.example.reminderapp.utils.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditListsFragment : NavigationFragment(), MenuProvider {

    override var backstackTag: String = FragmentNavigationConstants.TO_EDIT_LISTS_FRAGMENT

    private lateinit var binding: FragmentEditListsBinding
    private lateinit var adapter: GroupListRecyclerViewAdapter
    private val viewModel by viewModel<EditListsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditListsBinding.inflate(inflater, container, false)

        edgeToEdge()
        setupToolbar(
            activity = activity as AppCompatActivity,
            toolbar = binding.editListToolbar,
            backEnable = true
        )

        setupAdapterAndRecycler()
        setupObserver()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(this)
    }

    private fun edgeToEdge() = with(binding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            editListToolbar setPaddingToInset WindowInsetsCompat.Type.statusBars()
        }
    }

    private fun setupAdapterAndRecycler() = with(binding) {
        adapter = GroupListRecyclerViewAdapter(object :
            GroupListRecyclerViewAdapter.OnItemClickListener {
            override fun onRcItemClick(group: Group) {
                /** Editing list info process here */
                val bundle = Bundle().apply {
                    putSerializable(
                        FragmentNavigationConstants.EDITABLE_LIST,
                        group
                    )
                }
                navigateTo(NavigationDestinations.ToNewListFragment, bundle)
            }
            override fun onDeleteIconClick(group: Group) {
                viewModel.deleteGroup(group.groupId)
                adapter.deleteItem(group)
            }
        }, isDeleteIconVisible = true)

        editListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        editListsRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.fetchData()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE
                            adapter.submitList(it.data)
                        }
                        is UiState.Loading -> {
                            binding.contentLayout.visibility = View.GONE
                            binding.loadingLayout.visibility = View.VISIBLE
                        }
                        is UiState.Error -> {
                            binding.contentLayout.visibility = View.VISIBLE
                            binding.loadingLayout.visibility = View.GONE
                            showSnackbar(it.message, requireActivity().findViewById(R.id.rootView))
                        }
                    }
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) { /** STUB **/ }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                navigateBack()
                return true
            }
        }
        return true
    }

}
