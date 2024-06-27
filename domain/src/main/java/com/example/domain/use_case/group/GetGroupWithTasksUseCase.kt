package com.example.domain.use_case.group

import com.example.domain.repository.IGroupRepository

class GetGroupWithTasksUseCase(private val groupRepository: IGroupRepository) {
    operator fun invoke(id: Int) = groupRepository.getGroupWithTasks(id)
}