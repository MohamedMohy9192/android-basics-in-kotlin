package com.example.android.marsphotos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

// Retrofit needs the base URI for the web service, and a converter factory to build a web services API.
private val retrofit = Retrofit.Builder()
    // The converter tells Retrofit what to do with the data it gets back from the web service.
    // In this case, you want Retrofit to fetch a JSON response from the web service, and return it as a String
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * an interface that defines how Retrofit talks to the web server using HTTP requests.
 */
interface MarsApiService {
    /**
     * @GET annotation to tell Retrofit that this is GET request,
     * and specify endpoint, for that web service method. In this case the endpoint is called photos.
     * */
    @GET("photos")
    suspend fun getPhotos(): String
}

object MarsApi {
    val service: MarsApiService by lazy {
        retrofit.create()
    }
}
