package com.soft.restapp.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val API_BASE_URL = "https://192.168.211.250/api/product"

    fun factoryService(): ProductService {

        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder.client(httpClient.build())
            .build().create(ProductService::class.java)

    }

}