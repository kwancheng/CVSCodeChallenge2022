package com.gk.cvscodechallenge.repository

import com.gk.cvscodechallenge.api.FlickrApi
import com.gk.cvscodechallenge.api.dto.PhotosPublicResponse
import com.gk.cvscodechallenge.db.SearchTerm
import com.gk.cvscodechallenge.db.SearchTermDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface FlickrRepository {
    val loadStatus: Flow<SearchStatus>
    val recentSearchUpdates: Flow<List<String>>
    suspend fun searchTerm(term: String?)
    suspend fun primeRecentSearchUpdates()
}

class FlickrRepositoryImpl @Inject constructor(
    private val api: FlickrApi,
    private val searchTermDao: SearchTermDao
): FlickrRepository
{
    private val _loadStatus = MutableStateFlow<SearchStatus>(SearchStatus.Ready())
    override val loadStatus: Flow<SearchStatus> = _loadStatus

    private val _recentSearchUpdates = MutableStateFlow<List<String>>(listOf())
    override val recentSearchUpdates: Flow<List<String>> = _recentSearchUpdates

    override suspend fun searchTerm(term: String?) {
        if (term.isNullOrBlank()) {
            _loadStatus.value = SearchStatus.LoadError("Search term is blank.")
        } else {
            val result = runCatching {
                api.fetchImageFeed(term)
            }

            _loadStatus.value = SearchStatus.Loading(term)
            result
                .onSuccess { it ->
                    searchTermDao.insertOrUpdateSearchTerm(SearchTerm(term, System.currentTimeMillis()))
                    searchTermDao.keepOnlyFirst(5)
                    _recentSearchUpdates.value = searchTermDao.getSearchTerms(5).map { searchTerm -> searchTerm.term }
                    _loadStatus.value = SearchStatus.LoadComplete(it)
                }
                .onFailure {
                    _loadStatus.value = SearchStatus.LoadError(it.message ?: "Api error, no message found.")
                }
        }
    }

    override suspend fun primeRecentSearchUpdates() {
        _recentSearchUpdates.value = searchTermDao.getSearchTerms(5).map { it.term }
    }
}

sealed class SearchStatus {
    data class Ready(val noop: Unit? = null): SearchStatus()
    data class Loading(val searchTerm: String) : SearchStatus()
    data class LoadComplete(val response: PhotosPublicResponse) : SearchStatus()
    data class LoadError(val errorMsg: String): SearchStatus()
}