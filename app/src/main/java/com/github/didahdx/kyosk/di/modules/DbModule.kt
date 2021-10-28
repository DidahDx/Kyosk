package com.github.didahdx.kyosk.di.modules

import android.app.Application
import androidx.room.Room
import com.github.didahdx.kyosk.common.Constants
import com.github.didahdx.kyosk.data.local.ShopDatabase
import com.github.didahdx.kyosk.data.local.dao.CategoryDao
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Module
class DbModule {

    @Provides
    @Singleton
    fun provideShopDatabase(application: Application): ShopDatabase {
        return Room.databaseBuilder(
            application,
            ShopDatabase::class.java, Constants.SHOP_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(shopDatabase: ShopDatabase): CategoryDao {
        return shopDatabase.getCategoryDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(shopDatabase: ShopDatabase): ProductDao {
        return shopDatabase.getProductDao()
    }
}