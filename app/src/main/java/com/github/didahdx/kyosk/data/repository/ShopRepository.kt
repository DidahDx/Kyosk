package com.github.didahdx.kyosk.data.repository

import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.remote.dto.CategoryListDto
import com.github.didahdx.kyosk.data.remote.dto.ProductItemDto
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Daniel Didah on 10/22/21.
 */
interface ShopRepository {
    fun getCategories(): Observable<List<CategoryListDto>>
    fun getProducts(): Observable<List<ProductItemDto>>
    fun getAllProducts(code: String): Observable<Resources<List<RecyclerViewItems>>>
    fun fetchAll(code: String): Observable<Resources<List<RecyclerViewItems>>>
    fun getAllProductsDb(code: String): Observable<Resources<List<RecyclerViewItems>>>
    fun clear()
}