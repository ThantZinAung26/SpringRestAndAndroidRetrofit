package com.soft.restapp.service

import com.soft.restapp.model.Product
import com.soft.restapp.model.StatusResponseEntity
import retrofit2.Call
import retrofit2.http.*

interface ProductService {

    @GET("/products")
    fun getProductList(): Call<List<Product>>
    @PUT("/add")
    fun addProductItem(@Body product: Product): Call<StatusResponseEntity<Product>>
    @DELETE("/{id}")
    fun deleteProduct(@Path("id") id: Long): Call<StatusResponseEntity<Product>>
    @POST("/available")
    fun productAvailable(@Body available: Boolean, @Query("id") id: Long): Call<StatusResponseEntity<Boolean>>

}