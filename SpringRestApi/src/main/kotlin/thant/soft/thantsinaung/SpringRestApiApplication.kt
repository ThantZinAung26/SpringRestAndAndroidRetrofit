package thant.soft.thantsinaung

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import thant.soft.thantsinaung.config.StorageProperties

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class SpringRestApiApplication

fun main(args: Array<String>) {
	runApplication<SpringRestApiApplication>(*args)
}
