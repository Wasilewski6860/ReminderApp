package com.example.domain.use_case.group

import com.example.domain.model.Group
import com.example.domain.repository.IGroupRepository

class InsertGroupUseCase(private val groupRepository: IGroupRepository) {
    suspend operator fun invoke(group: Group) = groupRepository.addGroup(group)
}