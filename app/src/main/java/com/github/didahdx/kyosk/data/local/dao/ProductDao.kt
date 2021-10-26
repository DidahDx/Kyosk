package com.github.didahdx.kyosk.data.local.dao

import androidx.room.*
import com.github.didahdx.kyosk.data.local.entities.ProductEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productEntity: ProductEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productEntity: List<ProductEntity>): Completable

    @Transaction
    @Query("SELECT * FROM ProductEntity")
    fun getAllProducts(): Observable<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM ProductEntity WHERE category=(case when :category='Categories' then category else :category end)")
    fun getAllProductsByCategory(category: String): Observable<List<ProductEntity>>

    @Transaction
    @Query("SELECT * FROM ProductEntity WHERE id=:id")
    fun getAllProductsWithId(id: Int): Observable<ProductEntity>
}