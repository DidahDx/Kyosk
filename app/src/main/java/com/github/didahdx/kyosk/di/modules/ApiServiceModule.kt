package com.github.didahdx.kyosk.di.modules

import android.app.Application
import com.github.didahdx.kyosk.common.Constants
import com.github.didahdx.kyosk.data.local.dao.CategoryDao
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import com.github.didahdx.kyosk.data.remote.NetworkAvailability
import com.github.didahdx.kyosk.data.remote.api.ShopApiServices
import com.github.didahdx.kyosk.data.repository.ShopRepository
import com.github.didahdx.kyosk.data.repository.ShopRepositoryImpl
import com.github.didahdx.kyosk.di.FragmentScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Module
class ApiServiceModule {


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }


    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.END_POINT)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ShopApiServices {
        return retrofit.create(ShopApiServices::class.java)
    }


    @Provides
    fun provideShopRepository(shopApiServices: ShopApiServices,
                              categoryDao: CategoryDao,
                              productDao: ProductDao,networkAvailability: NetworkAvailability):ShopRepository{
        return ShopRepositoryImpl(shopApiServices, categoryDao, productDao,networkAvailability)
    }
}