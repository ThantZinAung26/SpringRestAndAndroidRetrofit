package com.ceo26.sfu.responseEntity

data class FileResponseEntity(
        var fileName: String,
        var fileDownloadUrl: String,
        var size: Long
)