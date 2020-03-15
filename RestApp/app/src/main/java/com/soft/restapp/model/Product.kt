package com.soft.restapp.model

import com.google.gson.annotations.SerializedName

data class Product(var name: String,
                   var description: String,
                   var photoPath: String,
                   @SerializedName("available") var isAvailable: Boolean) {

    var id: Long = 0L
    /*constructor(name: String, description: String, isAvailable: Boolean) : this() {
        this.name = name
        this.description = description
        this.isAvailable = isAvailable
    }*/
}