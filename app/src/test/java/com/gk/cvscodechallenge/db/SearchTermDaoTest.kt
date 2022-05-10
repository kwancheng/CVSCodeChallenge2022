package com.gk.cvscodechallenge.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertWithMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class SearchTermDaoTest {
    private lateinit var db: CVSCodeChallengeDB
    private lateinit var searchTermDao: SearchTermDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room
            .inMemoryDatabaseBuilder(context, CVSCodeChallengeDB::class.java)
            .allowMainThreadQueries()
            .build()

        searchTermDao = db.searchTermDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Given: A search term is reused
     * When: SearchTermDao.insertOrUpdateSearchTerm is called
     * Then: validate that the existing search term has an updated timestamp.
     *
     * Note:
     * - This test utilizes getSearchTerms to retrieve. False negatives can occur if getSearchTerms's
     *   tests also fails.
     */
    @Test
    fun `test insertOrUpdateSearchTerm updates an existing term with an updated timestamp`():Unit = runTest {
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term1", 0))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term2", 1))

        // Update existing term
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term1", 2))

        val results = searchTermDao.getSearchTerms(3) // asking more than what we have to get everything

        assertWithMessage("searchTermDao.insertOrUpdateSearchTerm implementation error, method did not overwrite an existing terms's usedOn timestamp. Expecting search term \"term1\" at index 0")
            .that(results[0].term)
            .isEqualTo("term1")

        assertWithMessage("searchTermDao.insertOrUpdateSearchTerm implementation error, method did not overwrite an existing terms's usedOn timestamp. Expecting search term \"term2\" at index 1")
            .that(results[1].term)
            .isEqualTo("term2")
    }

    /**
     * Given: A known set of search terms and their orders.
     * When: SearchTermDao.getSearchTerms() is called.
     * Then: Validate that search terms are returned with the most recent terms earlier in the
     * result array. Only up to the limit specified.
     */
    @Test
    fun `test getSearchTerms returns the most recent search terms up to the specified limit`(): Unit = runTest {
        val limitBy = 5 // there are 6 test search terms, it should limit to 5

        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term1", 0))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term2", 1))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term3", 3))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term4", 2))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term5", 4))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term6", 5))

        val searchTerms = searchTermDao.getSearchTerms(limitBy)

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not limited to the specified limit.")
            .that(searchTerms.size)
            .isEqualTo(limitBy)

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not ordered by most recent first. Expecting search term \"term6\" at index 0")
            .that(searchTerms[0].term)
            .isEqualTo("term6")

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not ordered by most recent first. Expecting search term \"term5\" at index 1")
            .that(searchTerms[1].term)
            .isEqualTo("term5")

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not ordered by most recent first. Expecting search term \"term3\" at index 2")
            .that(searchTerms[2].term)
            .isEqualTo("term3")

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not ordered by most recent first. Expecting search term \"term4\" at index 3")
            .that(searchTerms[3].term)
            .isEqualTo("term4")

        assertWithMessage("searchTermDao.getSearchTerm implementation error, results are not ordered by most recent first. Expecting search term \"term2\" at index 4")
            .that(searchTerms[4].term)
            .isEqualTo("term2")
    }

    /**
     * Given: A set of search terms
     * When: SearchTermDao.keepOnlyFirst is called.
     * Then: Validate that only the expected number of search terms are returned.
     *
     * Note:
     * - The test will be using getSearchTerms method with a limit that is larger than the expected
     *   number of available rows, to simulate a query without a limit.
     * - There is a possibility of false negatives because of the dependency on searchTermDao.getSearchTerms
     *   to retrieve the results. Check getSearchTerm's test results first.
     */
    @Test
    fun `test keepOnlyFirst deletes all search terms and keep the most recent`(): Unit = runTest {
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term1", 0))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term2", 1))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term3", 3))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term4", 2))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term5", 4))
        searchTermDao.insertOrUpdateSearchTerm(SearchTerm("term6", 5))

        val limitBy = 3
        searchTermDao.keepOnlyFirst(limitBy)

        val searchTerms = searchTermDao.getSearchTerms(limitBy + 2)
        assertWithMessage("searchTermDao.keepOnlyFirst implementation error, unexpected number of rows are returned.")
            .that(searchTerms.size)
            .isEqualTo(limitBy)

        assertWithMessage("searchTermDao.keepOnlyFirst implementation error, deletion should keep the most recent search terms. Expecting search term \"term6\" at index 0")
            .that(searchTerms[0].term)
            .isEqualTo("term6")

        assertWithMessage("searchTermDao.keepOnlyFirst implementation error, deletion should keep the most recent search terms. Expecting search term \"term5\" at index 1")
            .that(searchTerms[1].term)
            .isEqualTo("term5")

        assertWithMessage("searchTermDao.keepOnlyFirst implementation error, deletion should keep the most recent search terms. Expecting search term \"term3\" at index 2")
            .that(searchTerms[2].term)
            .isEqualTo("term3")
    }
}