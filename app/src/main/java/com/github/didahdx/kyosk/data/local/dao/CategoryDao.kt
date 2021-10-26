package com.github.didahdx.kyosk.data.local.dao

import androidx.room.*
import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.data.local.entities.CategoryProductsRelation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryEntity: CategoryEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categoryEntity: List<CategoryEntity>): Completable

    @Transaction
    @Query("SELECT * FROM CategoryEntity WHERE code=(case when :code='All' then code else :code end)  GROUP BY description ORDER BY description")
    fun getCategoriesAndProducts(code:String): Observable<List<CategoryProductsRelation>>


    @Transaction
    @Query("SELECT * FROM CategoryEntity GROUP BY description ORDER BY description")
    fun getAllCategories(): Observable<List<CategoryEntity>>

}