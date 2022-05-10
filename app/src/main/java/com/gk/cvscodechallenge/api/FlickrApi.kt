package com.gk.cvscodechallenge.api

import com.gk.cvscodechallenge.api.dto.PhotosPublicResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun fetchImageFeed(@Query("tags") tags: String): PhotosPublicResponse
}