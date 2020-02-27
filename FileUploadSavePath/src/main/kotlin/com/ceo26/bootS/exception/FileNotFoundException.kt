package com.ceo26.bootS.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class FileNotFoundException : RuntimeException {

    override var message: String?
        get() = super.message
    lateinit var throwable: Throwable

    constructor(message: String): super() {
        this.message = message
    }

    constructor(message: String, cause: Throwable) : super() {
        this.message = message
        this.throwable = cause
    }

}
