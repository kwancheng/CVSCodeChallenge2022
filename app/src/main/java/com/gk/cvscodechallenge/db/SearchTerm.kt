package com.gk.cvscodechallenge.db

import androidx.room.Entity

@Entity(tableName = "search_term", primaryKeys = ["term"])
data class SearchTerm(
    val term: String,
    val usedOn: Long
)