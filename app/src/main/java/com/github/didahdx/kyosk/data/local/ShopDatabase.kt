package com.github.didahdx.kyosk.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.didahdx.kyosk.data.local.dao.CategoryDao
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.data.local.entities.ProductEntity

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Database(
    entities = [CategoryEntity::class, ProductEntity::class],
    version = 1, exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun getProductDao(): ProductDao
    abstract fun getCategoryDao(): CategoryDao
}