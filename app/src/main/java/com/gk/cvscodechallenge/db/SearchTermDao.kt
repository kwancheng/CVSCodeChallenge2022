package com.gk.cvscodechallenge.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchTermDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSearchTerm(searchTerm: SearchTerm)

    @Query("""
        DELETE FROM search_term where term NOT IN (
            SELECT term from search_term ORDER BY usedOn DESC LIMIT :count
        )
    """)
    suspend fun keepOnlyFirst(count: Int)

    @Query("""
        SELECT * FROM search_term ORDER BY usedOn DESC LIMIT :limit     
    """)
    suspend fun getSearchTerms(limit: Int) : List<SearchTerm>
}