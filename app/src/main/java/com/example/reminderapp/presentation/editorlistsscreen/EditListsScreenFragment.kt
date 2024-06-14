package com.example.reminderapp.presentation.editorlistsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.reminderapp.databinding.ListEditorScreenBinding
import com.example.reminderapp.presentation.recycleradapter.GroupListRecyclerViewAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditListsScreenFragment : Fragment() {

    private lateinit var binding: ListEditorScreenBinding
    private lateinit var adapter: GroupListRecyclerViewAdapter
    private val viewModel by viewModel<ListsEditorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListEditorScreenBinding.inflate(inflater, container, false)

        binding.apply {



        }

        setupAdapterAndRecycler()
        setupObserver()

        return binding.root
    }

    private fun setupAdapterAndRecycler() = with(binding) {
        adapter = GroupListRecyclerViewAdapter(object :
        GroupListRecyclerViewAdapter.OnItemClickListener {
            override fun onRcItemClick(position: Int) {
                /** Editing list info process here */
            }
            override fun onDeleteIconClick(position: Int) {
                /** Deletion process here */
            }
        }, isAdapterForMainScreen = false)

        editListsRecyclerView.layoutManager = GridLayoutManager(context, 1)
        editListsRecyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.fetchGroupsFromDatabase()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupsListData.collect {
                    adapter.fillRecyclerWithFullItemsList(it)
                }
            }
        }
    }

}