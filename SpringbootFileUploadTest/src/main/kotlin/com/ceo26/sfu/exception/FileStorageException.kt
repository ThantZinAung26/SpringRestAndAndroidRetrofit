package com.ceo26.sfu.exception

class FileStorageException : RuntimeException {

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