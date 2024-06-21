package com.example.reminderapp.presentation.editorlistsscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.model.Group
import com.example.reminderapp.MainActivity
import com.example.reminderapp.R
import com.example.reminderapp.databinding.ListEditorScreenBinding
import com.example.reminderapp.presentation.base.UiState
import com.example.reminderapp.presentation.base.serializer.GroupSerializer
import com.example.reminderapp.presentation.interfaces.BackActionInterface
import com.example.reminderapp.presentation.navigation.FragmentNavigationConstants
import com.example.reminderapp.presentation.new_list.NewListFragment
import com.example.reminderapp.presentation.recycleradapter.GroupListRecyclerViewAdapter
import com.example.reminderapp.utils.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditListsScreenFragment : Fragment(), MenuProvider, BackActionInterface {

    private lateinit var binding: ListEditorScreenBinding
    private lateinit var adapter: GroupListRecyclerViewAdapter
    private val viewModel by viewModel<ListsEditorViewModel>()
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListEditorScreenBinding.inflate(inflater, container, false)

        val activity = (activity as MainActivity)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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
        callback.remove()
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
                navigateToNewListFragment(bundle)
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

    override fun navigateBack() {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToNewListFragment(args: Bundle? = null) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_anim,
                R.anim.slide_out_anim,
                R.anim.slide_in_anim,
                R.anim.slide_out_anim
            )
            .replace(
                R.id.fragmentContainerView,
                NewListFragment().apply {
                args?.let { arguments = it }
            })
            .addToBackStack(FragmentNavigationConstants.TO_EDIT_LISTS_FRAGMENT)
            .commit()
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
