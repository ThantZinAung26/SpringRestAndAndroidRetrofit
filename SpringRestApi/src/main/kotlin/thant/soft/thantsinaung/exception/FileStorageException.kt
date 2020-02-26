package thant.soft.thantsinaung.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class FileStorageException(override val message: String) : RuntimeException() {
    constructor(message: String, throwable: Throwable) : this(message)

}