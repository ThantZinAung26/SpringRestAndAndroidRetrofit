package com.ceo26.bootS.exception

class StorageException : RuntimeException {

    override var message: String?
        get() = super.message

    lateinit var throwable: Throwable

    constructor(message: String) : super() {
        this.message = message
    }

    constructor(message: String, cause: Throwable): super() {
        this.message = message
        this.throwable = cause
    }
}
