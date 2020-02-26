package com.ceo26.sfu.controller

import com.ceo26.sfu.model.FileModel
import com.ceo26.sfu.responseEntity.FileResponseEntity
import com.ceo26.sfu.service.FileModelService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api")
class FileModelController {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(FileModelController::class.java)
    }

    @Autowired
    lateinit var service: FileModelService

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): FileResponseEntity {
        val dbFile: FileModel = service.storeFile(file)
        val downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(dbFile.id.toString())
                .toUriString()

        return FileResponseEntity(dbFile.fileName,downloadUri, file.size)
    }

    @GetMapping("/downloadFile/{fileId}")
    fun downloadFile(@PathVariable fileId: Long): ResponseEntity<Resource> {
        val dbFile = service.getFile(fileId)

        return ResponseEntity.ok()
                //.contentType(MediaType.parseMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\""+dbFile.fileName+"\"")
                .body(ByteArrayResource(dbFile.fileData))
    }



}