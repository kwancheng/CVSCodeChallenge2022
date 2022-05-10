package com.gk.cvscodechallenge.api

import com.google.common.truth.Truth.assertWithMessage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
class FlickrApiTest {
    @get:Rule val mockWebServer = MockWebServer()

    private lateinit var mockFlickrApi: FlickrApi

    @OptIn(ExperimentalSerializationApi::class)
    @Before
    fun setup() {
        mockFlickrApi = Retrofit
            .Builder()
            .client(OkHttpClient())
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json { isLenient = true }.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(FlickrApi::class.java)
    }

    /**
     * Given: An Proper Json Response
     * When: FlickrApi.fetchImageFeed
     * Then: Validate that items are returned
     */
    @Test
    fun `test fetchImageFeed returns expected number of items`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestData.body)
                .setResponseCode(200)
        )

        val response = mockFlickrApi.fetchImageFeed("Point")

        assertWithMessage("FlickrApi.fetchImageFeed failed retrieve the expected number of items. Take a look at the TestData.body json and verify it has not been changed.")
            .that(response.items?.size)
            .isEqualTo(20)
    }
}

object TestData {
    val body =
        """
        {
		"title": "Recent Uploads tagged porcupine",
		"link": "https:\/\/www.flickr.com\/photos\/tags\/porcupine\/",
		"description": "",
		"modified": "2022-05-09T12:43:22Z",
		"generator": "https:\/\/www.flickr.com",
		"items": [
	   {
			"title": "Pennsylvania Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/rvaughn\/52061347331\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52061347331_e421761d33_m.jpg"},
			"date_taken": "2022-05-01T10:30:25-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/rvaughn\/\">Rob Vaughn (Instagram @robvaughn_)<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/rvaughn\/52061347331\/\" title=\"Pennsylvania Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52061347331_e421761d33_m.jpg\" width=\"240\" height=\"160\" alt=\"Pennsylvania Porcupine\" \/><\/a><\/p> <p>@robvaughn_<\/p>",
			"published": "2022-05-09T12:43:22Z",
			"author": "nobody@flickr.com (\"Rob Vaughn (Instagram @robvaughn_)\")",
			"author_id": "89214582@N00",
			"tags": "wildlife benezette porcupine pa"
	   },
	   {
			"title": "Pennsylvania Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/rvaughn\/52061580659\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52061580659_0db33688b2_m.jpg"},
			"date_taken": "2022-05-01T10:30:32-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/rvaughn\/\">Rob Vaughn (Instagram @robvaughn_)<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/rvaughn\/52061580659\/\" title=\"Pennsylvania Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52061580659_0db33688b2_m.jpg\" width=\"240\" height=\"135\" alt=\"Pennsylvania Porcupine\" \/><\/a><\/p> <p>@robvaughn_<\/p>",
			"published": "2022-05-09T12:43:22Z",
			"author": "nobody@flickr.com (\"Rob Vaughn (Instagram @robvaughn_)\")",
			"author_id": "89214582@N00",
			"tags": "wildlife benezette porcupine pa"
	   },
	   {
			"title": "PSL_8193.Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/lennop\/52060369999\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52060369999_c5a688aa8a_m.jpg"},
			"date_taken": "2022-05-08T09:02:06-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/lennop\/\">lennop75<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/lennop\/52060369999\/\" title=\"PSL_8193.Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52060369999_c5a688aa8a_m.jpg\" width=\"159\" height=\"240\" alt=\"PSL_8193.Porcupine\" \/><\/a><\/p> ",
			"published": "2022-05-08T23:28:35Z",
			"author": "nobody@flickr.com (\"lennop75\")",
			"author_id": "101466438@N04",
			"tags": "porcupine"
	   },
	   {
			"title": "IMG_5887 Porc-\u00e9pic, Roberval",
			"link": "https:\/\/www.flickr.com\/photos\/127461478@N08\/52050756520\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52050756520_05a9f95994_m.jpg"},
			"date_taken": "2022-05-04T08:29:27-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/127461478@N08\/\">joro5072<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/127461478@N08\/52050756520\/\" title=\"IMG_5887 Porc-\u00e9pic, Roberval\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52050756520_05a9f95994_m.jpg\" width=\"240\" height=\"176\" alt=\"IMG_5887 Porc-\u00e9pic, Roberval\" \/><\/a><\/p> <p>Porcupine.<\/p>",
			"published": "2022-05-04T18:10:07Z",
			"author": "nobody@flickr.com (\"joro5072\")",
			"author_id": "127461478@N08",
			"tags": "animal nature mammif\u00e8re porc\u00e9pic porcupine"
	   },
	   {
			"title": "IMG_5918 Porc-\u00e9pic, Roberval",
			"link": "https:\/\/www.flickr.com\/photos\/127461478@N08\/52049216967\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52049216967_fd2efd76cb_m.jpg"},
			"date_taken": "2022-05-04T08:30:30-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/127461478@N08\/\">joro5072<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/127461478@N08\/52049216967\/\" title=\"IMG_5918 Porc-\u00e9pic, Roberval\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52049216967_fd2efd76cb_m.jpg\" width=\"240\" height=\"234\" alt=\"IMG_5918 Porc-\u00e9pic, Roberval\" \/><\/a><\/p> <p>Porcupine.<\/p>",
			"published": "2022-05-04T18:10:13Z",
			"author": "nobody@flickr.com (\"joro5072\")",
			"author_id": "127461478@N08",
			"tags": "animal nature mammif\u00e8re porc\u00e9pic porcupine"
	   },
	   {
			"title": "IMG_5880 Porc-\u00e9pic, Roberval",
			"link": "https:\/\/www.flickr.com\/photos\/127461478@N08\/52049206222\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52049206222_38622ca2ae_m.jpg"},
			"date_taken": "2022-05-04T08:29:06-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/127461478@N08\/\">joro5072<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/127461478@N08\/52049206222\/\" title=\"IMG_5880 Porc-\u00e9pic, Roberval\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52049206222_38622ca2ae_m.jpg\" width=\"240\" height=\"170\" alt=\"IMG_5880 Porc-\u00e9pic, Roberval\" \/><\/a><\/p> <p>Porcupine.<\/p>",
			"published": "2022-05-04T18:10:07Z",
			"author": "nobody@flickr.com (\"joro5072\")",
			"author_id": "127461478@N08",
			"tags": "animal nature mammif\u00e8re porc\u00e9pic porcupine"
	   },
	   {
			"title": "IMG_5923 Porc-\u00e9pic, Roberval",
			"link": "https:\/\/www.flickr.com\/photos\/127461478@N08\/52049222142\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52049222142_91ea4fd1ef_m.jpg"},
			"date_taken": "2022-05-04T08:30:48-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/127461478@N08\/\">joro5072<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/127461478@N08\/52049222142\/\" title=\"IMG_5923 Porc-\u00e9pic, Roberval\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52049222142_91ea4fd1ef_m.jpg\" width=\"240\" height=\"156\" alt=\"IMG_5923 Porc-\u00e9pic, Roberval\" \/><\/a><\/p> <p>Porcupine.<\/p>",
			"published": "2022-05-04T18:10:14Z",
			"author": "nobody@flickr.com (\"joro5072\")",
			"author_id": "127461478@N08",
			"tags": "animal nature mammif\u00e8re porc\u00e9pic porcupine"
	   },
	   {
			"title": "IMG_5913 Porc-\u00e9pic, Roberval",
			"link": "https:\/\/www.flickr.com\/photos\/127461478@N08\/52050766545\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52050766545_c8681d4aa3_m.jpg"},
			"date_taken": "2022-05-04T08:30:17-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/127461478@N08\/\">joro5072<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/127461478@N08\/52050766545\/\" title=\"IMG_5913 Porc-\u00e9pic, Roberval\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52050766545_c8681d4aa3_m.jpg\" width=\"240\" height=\"165\" alt=\"IMG_5913 Porc-\u00e9pic, Roberval\" \/><\/a><\/p> <p>Porcupine.<\/p>",
			"published": "2022-05-04T18:10:11Z",
			"author": "nobody@flickr.com (\"joro5072\")",
			"author_id": "127461478@N08",
			"tags": "animal nature mammif\u00e8re porc\u00e9pic porcupine"
	   },
	   {
			"title": "Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/129524245@N08\/52048696189\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52048696189_f821e05f38_m.jpg"},
			"date_taken": "2022-05-01T17:26:08-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/129524245@N08\/\">NicoleW0000<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/129524245@N08\/52048696189\/\" title=\"Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52048696189_f821e05f38_m.jpg\" width=\"240\" height=\"160\" alt=\"Porcupine\" \/><\/a><\/p> ",
			"published": "2022-05-03T21:38:37Z",
			"author": "nobody@flickr.com (\"NicoleW0000\")",
			"author_id": "129524245@N08",
			"tags": "animal porcupine tree wildlife nature outdoors ontario canada"
	   },
	   {
			"title": "3J5A9664-Edit",
			"link": "https:\/\/www.flickr.com\/photos\/195494610@N07\/52061675883\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52061675883_5a4a04c7df_m.jpg"},
			"date_taken": "2016-06-27T09:23:07-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/195494610@N07\/\">BobY.51<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/195494610@N07\/52061675883\/\" title=\"3J5A9664-Edit\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52061675883_5a4a04c7df_m.jpg\" width=\"240\" height=\"160\" alt=\"3J5A9664-Edit\" \/><\/a><\/p> ",
			"published": "2022-05-09T15:56:36Z",
			"author": "nobody@flickr.com (\"BobY.51\")",
			"author_id": "195494610@N07",
			"tags": "porcupine"
	   },
	   {
			"title": "IO0A7325",
			"link": "https:\/\/www.flickr.com\/photos\/195494610@N07\/52061658651\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52061658651_701d12c8d6_m.jpg"},
			"date_taken": "2020-08-06T10:21:51-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/195494610@N07\/\">BobY.51<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/195494610@N07\/52061658651\/\" title=\"IO0A7325\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52061658651_701d12c8d6_m.jpg\" width=\"240\" height=\"160\" alt=\"IO0A7325\" \/><\/a><\/p> ",
			"published": "2022-05-09T15:58:05Z",
			"author": "nobody@flickr.com (\"BobY.51\")",
			"author_id": "195494610@N07",
			"tags": "porcupine"
	   },
	   {
			"title": "3J5A9669-Edit",
			"link": "https:\/\/www.flickr.com\/photos\/195494610@N07\/52060606962\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52060606962_a1cdfbd3f6_m.jpg"},
			"date_taken": "2016-06-27T09:23:25-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/195494610@N07\/\">BobY.51<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/195494610@N07\/52060606962\/\" title=\"3J5A9669-Edit\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52060606962_a1cdfbd3f6_m.jpg\" width=\"240\" height=\"160\" alt=\"3J5A9669-Edit\" \/><\/a><\/p> ",
			"published": "2022-05-09T15:56:37Z",
			"author": "nobody@flickr.com (\"BobY.51\")",
			"author_id": "195494610@N07",
			"tags": "porcupine"
	   },
	   {
			"title": "IO0A7344",
			"link": "https:\/\/www.flickr.com\/photos\/195494610@N07\/52060615262\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52060615262_dd0f6f9ecd_m.jpg"},
			"date_taken": "2020-08-06T10:22:24-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/195494610@N07\/\">BobY.51<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/195494610@N07\/52060615262\/\" title=\"IO0A7344\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52060615262_dd0f6f9ecd_m.jpg\" width=\"160\" height=\"240\" alt=\"IO0A7344\" \/><\/a><\/p> ",
			"published": "2022-05-09T15:58:06Z",
			"author": "nobody@flickr.com (\"BobY.51\")",
			"author_id": "195494610@N07",
			"tags": "porcupine"
	   },
	   {
			"title": "3J5A9968-Edit",
			"link": "https:\/\/www.flickr.com\/photos\/195494610@N07\/52062143090\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52062143090_3d495bd07b_m.jpg"},
			"date_taken": "2015-06-11T18:51:31-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/195494610@N07\/\">BobY.51<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/195494610@N07\/52062143090\/\" title=\"3J5A9968-Edit\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52062143090_3d495bd07b_m.jpg\" width=\"240\" height=\"160\" alt=\"3J5A9968-Edit\" \/><\/a><\/p> ",
			"published": "2022-05-09T15:56:47Z",
			"author": "nobody@flickr.com (\"BobY.51\")",
			"author_id": "195494610@N07",
			"tags": "porcupine"
	   },
	   {
			"title": "PSL_8197.Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/lennop\/52060136451\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52060136451_b6e653583b_m.jpg"},
			"date_taken": "2022-05-08T09:04:16-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/lennop\/\">lennop75<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/lennop\/52060136451\/\" title=\"PSL_8197.Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52060136451_b6e653583b_m.jpg\" width=\"159\" height=\"240\" alt=\"PSL_8197.Porcupine\" \/><\/a><\/p> ",
			"published": "2022-05-08T23:28:35Z",
			"author": "nobody@flickr.com (\"lennop75\")",
			"author_id": "101466438@N04",
			"tags": "porcupine"
	   },
	   {
			"title": "Nom Noms!",
			"link": "https:\/\/www.flickr.com\/photos\/129524245@N08\/52053101222\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52053101222_7101b7ea00_m.jpg"},
			"date_taken": "2022-05-04T18:41:06-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/129524245@N08\/\">NicoleW0000<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/129524245@N08\/52053101222\/\" title=\"Nom Noms!\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52053101222_7101b7ea00_m.jpg\" width=\"240\" height=\"173\" alt=\"Nom Noms!\" \/><\/a><\/p> <p>I probably could have gotten a less obstructed view from a different angle but I just happened to find him while looking for something else &amp; I didn't want to disturb his mealtime munching on these new tender Spring leaves.<\/p>",
			"published": "2022-05-06T13:42:11Z",
			"author": "nobody@flickr.com (\"NicoleW0000\")",
			"author_id": "129524245@N08",
			"tags": "animal porcupine wildlife nature outdoors spring canada"
	   },
	   {
			"title": "G08A8654.jpg",
			"link": "https:\/\/www.flickr.com\/photos\/wcdumonts\/52046386406\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52046386406_0cb1415bd3_m.jpg"},
			"date_taken": "2022-05-01T14:47:33-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/wcdumonts\/\">Mark Dumont<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/wcdumonts\/52046386406\/\" title=\"G08A8654.jpg\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52046386406_0cb1415bd3_m.jpg\" width=\"235\" height=\"240\" alt=\"G08A8654.jpg\" \/><\/a><\/p> ",
			"published": "2022-05-03T01:23:06Z",
			"author": "nobody@flickr.com (\"Mark Dumont\")",
			"author_id": "23661161@N02",
			"tags": "ricco mammal zoo mark dumont porcupine cincinnati"
	   },
	   {
			"title": "G08A8706.jpg",
			"link": "https:\/\/www.flickr.com\/photos\/wcdumonts\/52045342087\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52045342087_a4912c774f_m.jpg"},
			"date_taken": "2022-05-01T14:48:30-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/wcdumonts\/\">Mark Dumont<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/wcdumonts\/52045342087\/\" title=\"G08A8706.jpg\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52045342087_a4912c774f_m.jpg\" width=\"198\" height=\"240\" alt=\"G08A8706.jpg\" \/><\/a><\/p> ",
			"published": "2022-05-03T01:22:49Z",
			"author": "nobody@flickr.com (\"Mark Dumont\")",
			"author_id": "23661161@N02",
			"tags": "mammal ricco zoo mark dumont porcupine cincinnati"
	   },
	   {
			"title": "G08A8663.jpg",
			"link": "https:\/\/www.flickr.com\/photos\/wcdumonts\/52046638064\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52046638064_a1cfee1a40_m.jpg"},
			"date_taken": "2022-05-01T14:47:38-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/wcdumonts\/\">Mark Dumont<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/wcdumonts\/52046638064\/\" title=\"G08A8663.jpg\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52046638064_a1cfee1a40_m.jpg\" width=\"208\" height=\"240\" alt=\"G08A8663.jpg\" \/><\/a><\/p> ",
			"published": "2022-05-03T01:22:58Z",
			"author": "nobody@flickr.com (\"Mark Dumont\")",
			"author_id": "23661161@N02",
			"tags": "ricco mammal zoo mark dumont porcupine cincinnati"
	   },
	   {
			"title": "Porcupine",
			"link": "https:\/\/www.flickr.com\/photos\/158350095@N03\/52044140148\/",
			"media": {"m":"https:\/\/live.staticflickr.com\/65535\/52044140148_934a00f3de_m.jpg"},
			"date_taken": "2022-04-12T11:39:30-08:00",
			"description": " <p><a href=\"https:\/\/www.flickr.com\/people\/158350095@N03\/\">hermann.kl<\/a> posted a photo:<\/p> <p><a href=\"https:\/\/www.flickr.com\/photos\/158350095@N03\/52044140148\/\" title=\"Porcupine\"><img src=\"https:\/\/live.staticflickr.com\/65535\/52044140148_934a00f3de_m.jpg\" width=\"240\" height=\"160\" alt=\"Porcupine\" \/><\/a><\/p> ",
			"published": "2022-05-02T05:52:38Z",
			"author": "nobody@flickr.com (\"hermann.kl\")",
			"author_id": "158350095@N03",
			"tags": "deutschland germany nrw leverkusen zoo wildparkreuschenberg stachelschwein porcupine"
	   }
        ]
    }    
        """.trimIndent()
}