package com.example.domain.use_case

import com.example.domain.model.Group
import com.example.domain.model.Task
import com.example.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

typealias GetAllGroupsBaseUseCase = BaseUseCase<Unit, Flow<List<Group>>>

class GetAllGroupsUseCase(private val taskRepository: TaskRepository) : GetAllGroupsBaseUseCase {
    override suspend fun invoke(params: Unit)  = taskRepository.getAllGroups()
}
