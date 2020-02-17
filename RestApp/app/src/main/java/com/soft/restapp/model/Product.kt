package com.soft.restapp.model

import com.google.gson.annotations.SerializedName

data class Product(var name: String, @SerializedName("available") var isAvailable: Boolean) {

    var id: Long = 0L
    constructor(): this("", false)
}