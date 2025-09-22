package com.example.sumup.businessLogic

import com.example.sumup.data.repository.UserAuthRepository
import com.example.sumup.data.repository.SummaryRepository
import com.example.sumup.data.repository.SummaryItem

class GetUserSummaries(
    private val authRepo: UserAuthRepository,
    private val summaryRepo: SummaryRepository
) {
    suspend fun execute(): Result<List<SummaryItem>> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            summaryRepo.getUserSummaries(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
