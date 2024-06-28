package com.example.domain.use_case.group

import com.example.domain.repository.IGroupRepository

class GetGroupUseCase(private val groupRepository: IGroupRepository) {
    operator fun invoke(id: Int) = groupRepository.getGroup(id)
}