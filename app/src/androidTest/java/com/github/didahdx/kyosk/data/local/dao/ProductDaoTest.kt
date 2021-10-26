package com.github.didahdx.kyosk.data.local.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.didahdx.kyosk.data.local.ShopDatabase
import com.github.didahdx.kyosk.data.local.entities.ProductEntity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

/**
 * Created by Daniel Didah on 10/26/21.
 */
class ProductDaoTest {
    lateinit var shopDatabase: ShopDatabase
    lateinit var productDao: ProductDao
    lateinit var appContext: Context

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        appContext = ApplicationProvider.getApplicationContext()
        shopDatabase =
            Room.inMemoryDatabaseBuilder(appContext, ShopDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        productDao = shopDatabase.getProductDao()
    }

    @Test
    fun insert() {
        val product = ProductEntity(
            "INV4OTC",
            "Kibuyu Bar Soap 1kg",
            2,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Kibuyu_bar_1kg.png.png",
            159,
            "Kibuyu Bar Soap 1kg"
        )

        productDao.insert(product).test()
        val productResult = productDao.getAllProducts().blockingFirst().first()
        assertEquals(product, productResult)
    }


    @Test
    fun getAllProducts() {
        val product = ProductEntity(
            "INV4OTC",
            "Kibuyu Bar Soap 1kg",
            2,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Kibuyu_bar_1kg.png.png",
            159,
            "Kibuyu Bar Soap 1kg"
        )
        val product2 = ProductEntity(
            "INV4OTC",
            "Jamaa Bar Soap 1kg",
            1,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Jamaa_bar_1kg.png",
            215,
            "Jamaa Bar Soap 1kg"
        )
        val product3 = ProductEntity(
            "INV4OTC",
            "Sunlight Bar Soap 160g",
            3,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Sunlight_160g.png",
            220,
            "Sunlight Bar Soap 160g"
        )

        val listItems = ArrayList<ProductEntity>()
        listItems.add(product)
        listItems.add(product2)
        listItems.add(product3)
        productDao.insert(listItems).test()
        val productResult = productDao.getAllProducts().blockingFirst()
        assertEquals(3, productResult.size)
    }

    @Test
    fun getAllProductsByCategories() {
        val product = ProductEntity(
            "INV4OTC",
            "Kibuyu Bar Soap 1kg",
            2,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Kibuyu_bar_1kg.png.png",
            159,
            "Kibuyu Bar Soap 1kg"
        )
        val product2 = ProductEntity(
            "INV4OTC",
            "Jamaa Bar Soap 1kg",
            1,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Jamaa_bar_1kg.png",
            215,
            "Jamaa Bar Soap 1kg"
        )
        val product3 = ProductEntity(
            "INV4OTC",
            "Sunlight Bar Soap 160g",
            3,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Sunlight_160g.png",
            220,
            "Sunlight Bar Soap 160g"
        )
        val product4 = ProductEntity(
            "INV41TC",
            "Avena Cooking Oil 10ltr",
            9,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Oil_Avena_10ltr.png",
            460,
            "Avena Cooking Oil 10ltr"
        )
        val product5 = ProductEntity(
            "INV41TC",
            "Avena Cooking Oil 1ltr",
            10,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Oil_Avena_1ltr.png",
            748,
            "Avena Cooking Oil 1ltr"
        )

        val listItems = ArrayList<ProductEntity>()
        listItems.add(product)
        listItems.add(product2)
        listItems.add(product3)
        listItems.add(product4)
        listItems.add(product5)
        productDao.insert(listItems).test()

        val productResult = productDao.getAllProductsByCategory("Categories").blockingFirst()
        assertEquals(5, productResult.size)
    }

    @Test
    fun getAllProductsByCategorySpecificID() {
        val product = ProductEntity(
            "INV4OTC",
            "Kibuyu Bar Soap 1kg",
            2,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Kibuyu_bar_1kg.png.png",
            159,
            "Kibuyu Bar Soap 1kg"
        )
        val product2 = ProductEntity(
            "INV4OTC",
            "Jamaa Bar Soap 1kg",
            1,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Jamaa_bar_1kg.png",
            215,
            "Jamaa Bar Soap 1kg"
        )
        val product3 = ProductEntity(
            "INV4OTC",
            "Sunlight Bar Soap 160g",
            3,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Sunlight_160g.png",
            220,
            "Sunlight Bar Soap 160g"
        )
        val product4 = ProductEntity(
            "INV41TC",
            "Avena Cooking Oil 10ltr",
            9,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Oil_Avena_10ltr.png",
            460,
            "Avena Cooking Oil 10ltr"
        )
        val product5 = ProductEntity(
            "INV41TC",
            "Avena Cooking Oil 1ltr",
            10,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Oil_Avena_1ltr.png",
            748,
            "Avena Cooking Oil 1ltr"
        )

        val listItems = ArrayList<ProductEntity>()
        listItems.add(product)
        listItems.add(product2)
        listItems.add(product3)
        listItems.add(product4)
        listItems.add(product5)
        productDao.insert(listItems).test()

        val productResult = productDao.getAllProductsByCategory("INV4OTC").blockingFirst()
        assertEquals(3, productResult.size)
    }

    @Test
    fun getAllProductsWithId() {
        val product = ProductEntity(
            "INV4OTC",
            "Kibuyu Bar Soap 1kg",
            2,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Kibuyu_bar_1kg.png.png",
            159,
            "Kibuyu Bar Soap 1kg"
        )
        val product2 = ProductEntity(
            "INV4OTC",
            "Jamaa Bar Soap 1kg",
            1,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Jamaa_bar_1kg.png",
            215,
            "Jamaa Bar Soap 1kg"
        )
        val product3 = ProductEntity(
            "INV4OTC",
            "Sunlight Bar Soap 160g",
            3,
            "https://storage.googleapis.com/kyosk_candidate_images/images/Soap_Sunlight_160g.png",
            220,
            "Sunlight Bar Soap 160g"
        )

        val listItems = ArrayList<ProductEntity>()
        listItems.add(product)
        listItems.add(product2)
        listItems.add(product3)
        productDao.insert(listItems).test()
        val productResult = productDao.getAllProductsWithId(3).blockingFirst()
        assertEquals(product3, productResult)
    }

    @After
    fun closeDb() {
        shopDatabase.close()
    }
}