package com.github.didahdx.kyosk.data.remote.api

import com.github.didahdx.kyosk.data.remote.dto.CategoryListDto
import com.github.didahdx.kyosk.data.remote.dto.ProductItemDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

/**
 * Created by Daniel Didah on 10/22/21.
 */
interface ShopApiServices {

    @GET("categories")
    fun getCategories(): Observable<List<CategoryListDto>>

    @GET("items")
    fun getProducts(): Observable<List<ProductItemDto>>
}