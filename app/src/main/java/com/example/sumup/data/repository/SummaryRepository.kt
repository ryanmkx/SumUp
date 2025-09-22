package com.example.sumup.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

// Data class representing a summary in Firestore
data class Summary(
    val summaryId: String = "",
    val userId: String = "",
    val originalText: String = "",
    val summaryText: String = "",
    val createdAt: Timestamp? = null
)

// Domain model for summaries (not tied to Firebase)
data class SummaryItem(
    val summaryId: String,
    val originalText: String,
    val summaryText: String,
    val createdAt: Timestamp?
)

// Repository interface for summary operations
interface SummaryRepository {
    suspend fun getUserSummaries(userId: String): Result<List<SummaryItem>>
    suspend fun createSummary(summary: Summary): Result<Unit>
    suspend fun getSummaryById(summaryId: String): Result<SummaryItem>
    suspend fun deleteSummary(summaryId: String): Result<Unit>
}

// Firestore implementation of SummaryRepository
class FirestoreSummaryRepository(private val db: FirebaseFirestore) : SummaryRepository {
    
    override suspend fun getUserSummaries(userId: String): Result<List<SummaryItem>> {
        return try {
            val snapshot = db.collection("Summaries")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val summaries = snapshot.documents.mapNotNull { document ->
                val summary = document.toObject<Summary>()
                summary?.let {
                    SummaryItem(
                        summaryId = it.summaryId,
                        originalText = it.originalText,
                        summaryText = it.summaryText,
                        createdAt = it.createdAt
                    )
                }
            }

            // Sort by createdAt in descending order (newest first) in memory
            val sortedSummaries = summaries.sortedByDescending { it.createdAt }

            Result.success(sortedSummaries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createSummary(summary: Summary): Result<Unit> {
        return try {
            db.collection("Summaries").document(summary.summaryId).set(summary).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSummaryById(summaryId: String): Result<SummaryItem> {
        return try {
            val snapshot = db.collection("Summaries").document(summaryId).get().await()
            val summary = snapshot.toObject<Summary>()
                ?: return Result.failure(Exception("Summary not found"))

            Result.success(
                SummaryItem(
                    summaryId = summary.summaryId,
                    originalText = summary.originalText,
                    summaryText = summary.summaryText,
                    createdAt = summary.createdAt
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteSummary(summaryId: String): Result<Unit> {
        return try {
            db.collection("Summaries").document(summaryId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
