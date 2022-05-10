package com.gk.cvscodechallenge.repository

import app.cash.turbine.test
import com.gk.cvscodechallenge.api.FlickrApi
import com.gk.cvscodechallenge.api.dto.PhotosPublicResponse
import com.google.common.truth.Truth.assertWithMessage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlickrRepositoryImplTest {
    private lateinit var flickrRepositoryImpl: FlickrRepositoryImpl
    private lateinit var mockApi: FlickrApi

    @Before
    fun setup() {
        mockApi = mockk()
        flickrRepositoryImpl = FlickrRepositoryImpl(mockApi)
    }

    /**
     * Given: repository is just initialized
     * When: observing the loadStatus
     * Then: validate the initial value is SearchStatus.Ready
     */
    @Test
    fun `test validates initial value in loadStatus`() = runTest {
        flickrRepositoryImpl.loadStatus.test {
            val status = awaitItem()
            assertWithMessage("FlickrRepositoryImpl implementation error. Initial value in the flow was not set to Ready.")
                .that(status)
                .isInstanceOf(SearchStatus.Ready::class.java)
        }
    }

    /**
     * Given: A blank search term
     * When: calling [FlickrRepository.searchTerm]
     * Then: validate that an [SearchStatus.LoadError] with the message "Search term is blank." is
     * emitted from the loadStatus flow.
     */
    @Test
    fun `test searchTerm emits an error on a blank search term`() = runTest {
        flickrRepositoryImpl.loadStatus.test {
            awaitItem() // consume and ignore the initial ready status

            // Testing the blank term
            flickrRepositoryImpl.searchTerm("")

            val status = awaitItem()
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting a SearchStatus.LoadError when a blank search term is submitted.")
                .that(status)
                .isInstanceOf(SearchStatus.LoadError::class.java)

            val loadError = status as SearchStatus.LoadError
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting a SearchStatus.LoadError.msg is \"Search term is blank.\"")
                .that(loadError.errorMsg)
                .isEqualTo("Search term is blank.")
        }
    }

    /**
     * Given: A null search term
     * When: calling [FlickrRepository.searchTerm]
     * Then: validate that an [SearchStatus.LoadError] with the message "Search term is blank." is
     * emitted from the loadStatus flow.
     */
    @Test
    fun `test searchTerm emits an error on a null search term`() = runTest {
        flickrRepositoryImpl.loadStatus.test {
            awaitItem() // consume and ignore the initial ready status

            // Testing the null term
            flickrRepositoryImpl.searchTerm(null)
            val status = awaitItem()
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting a SearchStatus.LoadError when a blank search term is submitted.")
                .that(status)
                .isInstanceOf(SearchStatus.LoadError::class.java)

            val loadError = status as SearchStatus.LoadError
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting a SearchStatus.LoadError.msg is \"Search term is blank.\"")
                .that(loadError.errorMsg)
                .isEqualTo("Search term is blank.")
        }
    }

    /**
     * Given: There is an error on [FlickrApi.fetchImageFeed]
     * When: calling [FlickrRepository.searchTerm]
     * Then: Validate the error message returned in [SearchStatus.LoadError.errorMsg] is "Failed to retrieve images from api."
     *
     * Note: this test is more to ensure when there is an exception that the message is returned.
     */
    @Test
    fun `test searchTerm emits an error when there is an error on the api`() = runTest {
        coEvery { mockApi.fetchImageFeed(any()) } throws Exception("Failed to retrieve images from api.")

        flickrRepositoryImpl.loadStatus.test {
            awaitItem() // consume and ignore the initial ready status

            flickrRepositoryImpl.searchTerm("test term")
            awaitItem() // consume and ignore the Loading status.
            val status = awaitItem()

            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting SearchStatus.LoadError when there is an error on the api")
                .that(status)
                .isInstanceOf(SearchStatus.LoadError::class.java)

            val loadError = status as SearchStatus.LoadError
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting a SearchStatus.LoadError.msg is \"Failed to retrieve images from api.\"")
                .that(loadError.errorMsg)
                .isEqualTo("Failed to retrieve images from api.")
        }
    }

    /**
     * Given: a valid search term
     * When: calling [FlickrRepository.searchTerm]
     * Then: Validate that the [SearchStatus.Loading] then followed by [SearchStatus.LoadComplete]
     *
     * Note: we are not testing if the api can parse the response json correctly. For this test we
     * are just returning a blank generic responses.
     */
    @Test
    fun `test searchTerm emits Loading status when a search term is submitted`() = runTest {
        coEvery { mockApi.fetchImageFeed(any()) } returns PhotosPublicResponse.createEmpty()

        flickrRepositoryImpl.loadStatus.test {
            val searchTerm = "test term"
            awaitItem() // consume and ignore the initial ready status

            flickrRepositoryImpl.searchTerm(searchTerm)
            var status = awaitItem()

            // validate that we receiving Loading status first
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting SearchStatus.Loading when a valid term is submitted while the results are returned.")
                .that(status)
                .isInstanceOf(SearchStatus.Loading::class.java)

            val loadingStatus = status as SearchStatus.Loading
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting SearchStatus.Loading.searchTerm to be returned in the property.")
                .that(loadingStatus.searchTerm)
                .isEqualTo(searchTerm)

            // validate that LoadComplete is fired afterwards
            status = awaitItem()
            assertWithMessage("FlickrRepositoryImpl.searchTerm implementation error. Expecting SearchStatus.LoadComplete after a SearchStatus.Loading.")
                .that(status)
                .isInstanceOf(SearchStatus.LoadComplete::class.java)

            // note we don't need to validate the response in SearchStatus.LoadComplete
        }
    }
}