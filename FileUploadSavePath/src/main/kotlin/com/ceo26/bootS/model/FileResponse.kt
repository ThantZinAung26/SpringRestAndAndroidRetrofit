package com.ceo26.bootS.model

data class FileResponse(var name: String,
                        var uri: String,
                        var type: String,
                        var size: Long) {
}