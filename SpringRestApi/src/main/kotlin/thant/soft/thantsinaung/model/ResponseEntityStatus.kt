package thant.soft.thantsinaung.model

data class ResponseEntityStatus<T>(val boolean: Boolean, val message: String, val entity: T?)
