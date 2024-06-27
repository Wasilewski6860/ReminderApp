package com.example.domain.use_case.group

import com.example.domain.repository.IGroupRepository

class DeleteGroupUseCase(private val groupRepository: IGroupRepository) {
    suspend operator fun invoke(groupId: Int) = groupRepository.deleteGroup(groupId)
}