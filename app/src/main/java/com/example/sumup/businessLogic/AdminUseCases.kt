package com.example.sumup.businessLogic

import com.example.sumup.data.repository.UserProfileRepository
import com.example.sumup.data.repository.Users

class ListUsersByLevel(
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(level: String): Result<List<Users>> =
        profileRepo.listUsersByLevel(level)
}

class SearchUsers(
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(query: String, level: String? = null): Result<List<Users>> =
        profileRepo.searchUsers(query, level)
}

class DeleteUserAccount(
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(userId: String): Result<Unit> =
        profileRepo.disableUser(userId)
}


