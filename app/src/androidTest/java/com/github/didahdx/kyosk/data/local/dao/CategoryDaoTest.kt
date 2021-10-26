package com.github.didahdx.kyosk.data.local.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.didahdx.kyosk.data.local.ShopDatabase
import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.data.local.entities.ProductEntity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Daniel Didah on 10/26/21.
 */
class CategoryDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var shopDatabase: ShopDatabase
    private lateinit var categoryDao: CategoryDao
    lateinit var productDao: ProductDao
    lateinit var appContext: Context
    private val allCategory = ArrayList<CategoryEntity>()
    private val allProducts = ArrayList<ProductEntity>()

    @Before
    fun setup() {
        appContext = ApplicationProvider.getApplicationContext()
        shopDatabase =
            Room.inMemoryDatabaseBuilder(appContext, ShopDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        categoryDao = shopDatabase.getCategoryDao()
        productDao= shopDatabase.getProductDao()
    }

    @Before
    fun setupDataset() {
        val categoryEntity = CategoryEntity("INV4OTC", "Soap")
        val categoryEntity2 = CategoryEntity("INV41TC", "Cooking Oil")
        val categoryEntity3 = CategoryEntity("INV42TC", "Maize Flour")
        val categoryEntity4 = CategoryEntity("INV43TC", "Wheat Flour")
        val categoryEntity5 = CategoryEntity("INV45TC", "Rice")
        allCategory.add(categoryEntity)
        allCategory.add(categoryEntity2)
        allCategory.add(categoryEntity3)
        allCategory.add(categoryEntity4)
        allCategory.add(categoryEntity5)

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

        allProducts.add(product)
        allProducts.add(product2)
        allProducts.add(product3)
        allProducts.add(product4)
        allProducts.add(product5)
    }

    @Test
    fun insert() {
        categoryDao.insert(allCategory).test()
        val productResult = categoryDao.getAllCategories().blockingFirst()
        assertEquals(5, productResult.size)
    }

    @Test
    fun getCategoriesAndProductsAll(){
        categoryDao.insert(allCategory).test()
        productDao.insert(allProducts).test()
        val categoryWithProducts= categoryDao.getCategoriesAndProducts("All").blockingFirst()
        val product=categoryWithProducts.flatMap { it.productEntity}
        assertEquals(5,product.size)
    }

    @Test
    fun getCategoriesAndProductsBySpecificCategory(){
        categoryDao.insert(allCategory).test()
        productDao.insert(allProducts).test()
        val categoryWithProducts= categoryDao.getCategoriesAndProducts("INV41TC").blockingFirst()
        val category=categoryWithProducts.map { it.categoryEntity}.first()
        val product=categoryWithProducts.flatMap { it.productEntity}
        assertEquals("INV41TC",category.code)
        assertEquals(2,product.size)
    }


    @After
    fun closeDb() {
        shopDatabase.close()
    }
}