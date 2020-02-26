package com.ceo26.sfu.exception

class FileNotFoundException : RuntimeException {

    override var message: String
    lateinit var throwable: Throwable

    constructor(message: String) : super() {
        this.message = message
    }

    constructor(message: String, cause: Throwable) : super() {
        this.message = message
        this.throwable = cause
    }


}