package com.ceo26.bootS.controller

import com.ceo26.bootS.model.FileResponse
import com.ceo26.bootS.service.FilePropertiesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api")
class FileController() {

    @Autowired
    lateinit var  filePropertiesService: FilePropertiesService

    @GetMapping("/fileList")
    fun listAllFile(model: Model): String {
        model.addAttribute("files", filePropertiesService.loadAll().map {
            path ->  ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(path.fileName.toString())
                .toUriString()
        }.collect(Collectors.toList()))
        return "listFiles"
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val resource = filePropertiesService.loadAsResource(filename)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.filename + "\"")
                .body(resource)
    }

    @PostMapping("/upload")
    @ResponseBody
    fun uploadFile(@RequestParam("file") file: MultipartFile): FileResponse {
        val name = filePropertiesService.storeFile(file)
        val uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(name)
                .toUriString()
        return FileResponse(name, uri, file.contentType.toString(), file.size)
    }

    @PostMapping("/upload/multipart")
    @ResponseBody
    fun multiUploadFile(@RequestParam("files") files: Array<MultipartFile>): List<FileResponse> {
        return Arrays.stream(files)
                .map { file -> uploadFile(file) }
                .collect(Collectors.toList())
    }

}