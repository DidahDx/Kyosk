package com.github.didahdx.kyosk.data.remote

import com.github.didahdx.kyosk.ApiServiceAbstract
import com.github.didahdx.kyosk.data.remote.api.ShopApiServices
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Daniel Didah on 10/26/21.
 */
class ShopApiServiceServicesTest :ApiServiceAbstract<ShopApiServices>() {
    lateinit var apiService: ShopApiServices

    @Before
    fun setUp() {
        apiService = createService(ShopApiServices::class.java)
    }

    @Test
    fun getCategories() {
        enqueueResponse("categories.json")
        val categories = apiService.getCategories().blockingFirst().first().categories
        assertEquals(categories.size, 5)
    }

    @Test
    fun getProducts() {
        enqueueResponse("items.json",)
        val products = apiService.getProducts().blockingFirst()
        assertEquals(products.size, 50)
    }

}