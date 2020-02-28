package thant.soft.thantsinaung.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "folder")
data class StorageProperties(
        val location: String
)