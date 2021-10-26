package com.github.didahdx.kyosk

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by stevehechio on 8/5/21
 */

@RunWith(JUnit4::class)
open class ApiServiceAbstract<T> {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val server = MockWebServer()


    fun createService(clazz: Class<T>) :T {
        return  Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(clazz)
    }

    @Throws(IOException::class)
    fun enqueueResponse(fileName: String?) {
        val inputStream = Objects.requireNonNull(ApiServiceAbstract::class.java.classLoader)
            .getResourceAsStream(String.format("api_responses/%s", fileName))
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        server.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }

    @After
    fun closeServer() {
        server.shutdown()
    }

    @Test
    fun sampleTest(){
        assertEquals("test","test")
    }
}