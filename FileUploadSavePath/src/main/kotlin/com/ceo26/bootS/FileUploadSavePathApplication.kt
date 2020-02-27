package com.ceo26.bootS

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableConfigurationProperties(FileProperties::class)
class FileUploadSavePathApplication

fun main(args: Array<String>) {
	runApplication<FileUploadSavePathApplication>(*args)
}