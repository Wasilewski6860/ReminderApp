package com.example.domain.use_case

import com.example.domain.repository.TaskRepository

typealias DeleteGroupBaseUseCase = BaseUseCase<Int, Unit>

class DeleteGroupUseCase(private val taskRepository: TaskRepository) : DeleteGroupBaseUseCase {
    override suspend operator fun invoke(groupId: Int) = taskRepository.deleteGroup(groupId)
}
