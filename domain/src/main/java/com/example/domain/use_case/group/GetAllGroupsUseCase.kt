package com.example.domain.use_case.group

import com.example.domain.repository.IGroupRepository

class GetAllGroupsUseCase(private val groupRepository: IGroupRepository) {
    operator fun invoke() = groupRepository.getAllGroups()
}